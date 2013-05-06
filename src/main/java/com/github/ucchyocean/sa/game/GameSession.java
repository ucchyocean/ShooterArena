/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import com.github.ucchyocean.sa.ChatMode;
import com.github.ucchyocean.sa.PlayerExpHandler;
import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.Utility;
import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;

/**
 * @author ucchy
 * ゲームセッション
 */
public class GameSession {

    /** アナウンスメッセージフォーマット */
    private static final String FORMAT_ANNOUNCE = ChatColor.GREEN + "[%s] %s";

    /** チームチャットフォーマット */
    private static final String TEAM_CHAT_FORMAT = Utility.replaceColorCode("&a[%s&a]<%s&r&a> %s");

    /** チーム情報フォーマット */
    private static final String TEAM_INFORMATION_FORMAT = Utility.replaceColorCode("&a[%s&a] %s");

    /** 参加プレイヤー */
    private ArrayList<String> players;

    /** 観客プレイヤー */
    private ArrayList<String> listeners;

    /** ゲームモード */
    public MatchMode mode;

    /** ゲームフェイズ */
    public GamePhase phase;

    /** ゲームロガー */
    private GameLogger logger;

    /** アリーナ名 */
    public String arena;

    /** 赤チームのメンバーリスト */
    private ArrayList<String> teamRedNames;

    /** 青チームのメンバーリスト */
    private ArrayList<String> teamBlueNames;

    /** ゲームタイマー */
    private GameTimer timer;

    /** ゲームスコア */
    private GameScore score;

    /**
     * コンストラクタ
     */
    public GameSession(Arena arena) {

        this.players = new ArrayList<String>();
        this.listeners = new ArrayList<String>();
        this.mode = arena.getMode();
        this.logger = new GameLogger(arena.getName());
        this.arena = arena.getName();
        this.score = new GameScore(mode.life);
        teamRedNames = new ArrayList<String>();
        teamBlueNames = new ArrayList<String>();

        runPreparePhase();
    }

    /**
     * セッションを準備中にする
     */
    public void runPreparePhase() {

        this.phase = GamePhase.PREPARE;

        String message = "アリーナ " + arena + " で、新規ゲームセッションが作成されました。";
        annouceToAll(message);
   }

    /**
     * セッションを対戦メンバー募集中にする
     */
    public void runMatchMakingPhase() {

        this.phase = GamePhase.MATCH_MAKING;

        String mode = ArenaManager.getArena(arena).getMode().toJapanese();
        String message = "アリーナ " + arena + " が、対戦メンバー募集中になりました。";
        annouceToAll(message);
        message = "ゲームモードは、" + mode + " です。";
        annouceToAll(message);

        refreshArenaSign();
    }

    /**
     * セッションを対戦中にする
     */
    public void startGame() {

        this.phase = GamePhase.IN_GAME;

        String message = "アリーナ " + arena + " でゲームが開始しました！";
        logger.write(message);
        ShooterArena.sendBroadcast(ShooterArena.PRENOTICE + message);

        refreshArenaSign();

        // チームわけ
        setRandomTeam();

        // プレイヤーをテレポート
        teleportAllToArena();

        // プレイヤーをフリーズ
        setFreeze(true);

        // アイテムを支給
        setAllPlayerKits();

        // 経験値を配布
        setAllPlayerExperience();

        // タイマーを生成してスタート
        timer = new GameTimer(this, 5, mode.minute * 60);
        ShooterArena.startGameTimer(timer);
    }

    /**
     * セッションをキャンセルして終了する
     */
    public void cancelGame() {

        GamePhase prePhase = phase;
        this.phase = GamePhase.CANCELED;

        String message = "アリーナ " + arena + " のゲームがキャンセルされました。";
        logger.write(message);
        ShooterArena.sendBroadcast(ShooterArena.PRENOTICE + message);

        // タイマーをキャンセルする
        if ( timer != null ) {
            ShooterArena.cancelGameTimer(timer);
            timer = null;
        }

        // アイテムをクリア
        clearAllPlayerInventory();

        // プレイヤーをアンフリーズ
        setFreeze(false);

        // プレイヤーをテレポート
        if ( prePhase == GamePhase.IN_GAME )
            teleportAllToLounge();

        // チームわけを解除
        clearTeams();

        // 全員解散する
        players.clear();
        listeners.clear();

        // セッションを削除する
        ArenaManager.removeSession(this);

        // アリーナサインを更新する
        refreshArenaSign();
    }

    /**
     * セッションを終了する
     */
    public void endGame() {

        this.phase = GamePhase.END;

        String message = "アリーナ " + arena + " のゲームが終了しました。";
        logger.write(message);
        ShooterArena.sendBroadcast(ShooterArena.PRENOTICE + message);

        refreshArenaSign();

        // アイテムをクリア
        clearAllPlayerInventory();

        // プレイヤーをアンフリーズ(念のため)
        setFreeze(false);

        // プレイヤーをテレポート
        teleportAllToLounge();

        // チームわけを解除
        clearTeams();

        // 全員解散する
        players.clear();
        listeners.clear();

        // セッションを削除する
        ArenaManager.removeSession(this);

        // アリーナサインを更新する
        refreshArenaSign();
    }

    /**
     * セッションに参加しているプレイヤー名一覧を取得する
     * @return プレイヤー名一覧
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * 参加メンバーが満員かどうかを返す
     * @return 満員かどうか
     */
    public boolean isFull() {
        return players.size() >= mode.type.getMaxPlayerNum();
    }

    /**
     * セッションに参加メンバーを追加する
     * @param playerName 追加するプレイヤー名
     */
    public void addPlayer(String playerName) {
        annouceToAll(playerName + " さんが、ゲームセッションに参加しました。");
        players.add(playerName);

        // 満員になったら、開始コマンドを実行するよう、メッセージを送信する
        if ( isFull() ) {
            annouceToAll("ゲーム開始できる人数が集まりました。");
            annouceToAll("誰かが、/shooter start を実行してゲームを開始してください。");
        }

        refreshArenaSign();
    }

    /**
     * セッションから参加メンバーを離脱する
     * @param playerName 離脱するプレイヤー名
     */
    public void removePlayer(String playerName) {
        annouceToAll(playerName + " さんが、ゲームセッションから離脱しました。");
        players.remove(playerName);
        clearPlayerInventory(playerName);
        setFreeze(false);
        teleportToLounge(playerName);

        if ( teamRedNames.contains(playerName) )
            teamRedNames.remove(playerName);
        if ( teamBlueNames.contains(playerName) )
            teamBlueNames.remove(playerName);

        // 誰もいなくなったら、一旦セッションを終了する
        if ( players.size() <= 0 ) {
            cancelGame();
        }

        refreshArenaSign();
    }

    /**
     * 指定したプレイヤーがライフを使い切って終了した場合に呼ばれるメソッド
     * @param playerName ライフを使い切ったプレイヤー名
     */
    public void deadPlayer(String playerName) {
        annouceToAll(playerName + " さんが、ライフを使い切って死亡しました。");
        players.remove(playerName);
        clearPlayerInventory(playerName);
        setFreeze(false);

        if ( teamRedNames.contains(playerName) ) {
            teamRedNames.remove(playerName);
            // 誰もいなくなったらゲームを終了する。そうでなければ観客に追加する
            if ( teamRedNames.size() <= 0 ) {
                endGame();
            } else {
                addListener(playerName);
            }
        } else if ( teamBlueNames.contains(playerName) ) {
            teamBlueNames.remove(playerName);
            // 誰もいなくなったらゲームを終了する。そうでなければ観客に追加する
            if ( teamBlueNames.size() <= 0 ) {
                endGame();
            } else {
                addListener(playerName);
            }
        }

        refreshArenaSign();
    }

    /**
     * 全ての観客を取得する
     * @return 全ての観客
     */
    public ArrayList<String> getListeners() {
        return listeners;
    }

    /**
     * 観客を追加する
     * @param listenerName 観客名
     */
    public void addListener(String listenerName) {

        listeners.add(listenerName);
        Player player = ShooterArena.getPlayerExact(listenerName);

        // TODO 既にゲーム中なら、テレポして、flyして、インビジブルする
//        if ( phase == GamePhase.IN_GAME ) {
//            teleportToArena(player);
//            player.setFlying(true);
//        }
    }

    /**
     * 観客を削除する
     * @param listenerName 削除する観客名
     */
    public void removeListener(String listenerName) {
        listeners.remove(listenerName);
    }

    /**
     * 全てのプレイヤーにアナウンスする。観客にはアナウンスされないことに注意。
     * @param message アナウンスするメッセージ
     */
    public void annouceToPlayer(String message) {
        logger.write(message); // ログ出力
        for ( String name : players ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                String msg = String.format(FORMAT_ANNOUNCE, arena, message);
                player.sendMessage(msg);
            }
        }
    }

    /**
     * 全てのプレイヤーと観客にアナウンスする。
     * @param message アナウンスするメッセージ
     */
    public void annouceToAll(String message) {
        annouceToPlayer(message);
        for ( String name : listeners ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                String msg = String.format(FORMAT_ANNOUNCE, arena, message);
                player.sendMessage(msg);
            }
        }
    }

    /**
     * 全てのプレイヤーと観客を、アリーナにテレポートする
     */
    public void teleportAllToArena() {

        // 赤チームリスポーンにテレポート
        Location location = ArenaManager.getArena(arena).getRedRespawn();
        for ( String name : teamRedNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.teleport(location, TeleportCause.PLUGIN);
            }
        }
        for ( String name : listeners ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.teleport(location, TeleportCause.PLUGIN);
            }
        }

        // 青チームリスポーンにテレポート
        location = ArenaManager.getArena(arena).getBlueRespawn();
        for ( String name : teamBlueNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.teleport(location, TeleportCause.PLUGIN);
            }
        }
    }

    /**
     * 指定したプレイヤーをアリーナにテレポートする
     * @param player テレポートするプレイヤー
     */
    public void teleportToArena(Player player) {

        // 赤チームリスポーンにテレポート
        Location location = ArenaManager.getArena(arena).getRedRespawn();
        player.teleport(location, TeleportCause.PLUGIN);
    }

    /**
     * 全てのプレイヤーをラウンジにテレポートする
     */
    public void teleportAllToLounge() {

        for ( String name : players ) {
            teleportToLounge(name);
        }
        for ( String name : listeners ) {
            teleportToLounge(name);
        }
    }

    /**
     * 指定したプレイヤーをラウンジにテレポートする
     * @param name プレイヤー名
     */
    private void teleportToLounge(String name) {

        Location location = ArenaManager.getLoungeRespawn();
        Player player = ShooterArena.getPlayerExact(name);
        if ( player != null ) {
            player.teleport(location, TeleportCause.PLUGIN);
        }
    }

    /**
     * アリーナサインを更新する
     */
    private void refreshArenaSign() {
        Arena arena = ArenaManager.getArena(this.arena);
        if ( arena != null ) {
            arena.refreshSign();
        }
    }

    /**
     * プレイヤーをチームわけする
     */
    private void setRandomTeam() {
        teamRedNames = new ArrayList<String>();
        teamBlueNames = new ArrayList<String>();

        if ( players.size() == 1 ) {
            teamRedNames.add(players.get(0));
            return;
        }

        int num = players.size() / 2;
        ArrayList<String> temp = new ArrayList<String>(players);
        Collections.shuffle(temp);
        for ( int i=0; i<num; i++ ) {
            teamRedNames.add(temp.get(i));
        }
        for ( int i=num; i<temp.size(); i++ ) {
            teamBlueNames.add(temp.get(i));
        }

        // チームメンバーの色設定
        for ( String name : teamRedNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                ShooterArena.thandler.setPlayerColor(player, "red");
            }
        }
        for ( String name : teamBlueNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                ShooterArena.thandler.setPlayerColor(player, "blue");
            }
        }

        // 決定したチームの通知処理
        sendInfoToRedTeam("あなたは赤チームになりました。");
        sendInfoToBlueTeam("あなたは青チームになりました。");
    }

    /**
     * チームわけを解除する
     */
    private void clearTeams() {

        for ( String name : players ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                ShooterArena.thandler.leavePlayerTeam(player);
            }
        }
    }

    /**
     * 全てのプレイヤーのインベントリをキットで初期化する
     */
    private void setAllPlayerKits() {

        for ( String name : players ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                setPlayerKits(player);
            }
        }
    }

    /**
     * プレイヤーのインベントリをキットで初期化する
     * @param player プレイヤー
     */
    public void setPlayerKits(Player player) {

        ArrayList<ItemStack> items = SAConfig.getKitsItems();
        ArrayList<ItemStack> armors = SAConfig.getKitsArmors();

        PlayerInventory inv = player.getInventory();

        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);

        for ( ItemStack item : items ) {
            inv.addItem(item);
        }
        inv.setHelmet(armors.get(0));
        inv.setChestplate(armors.get(1));
        inv.setLeggings(armors.get(2));
        inv.setBoots(armors.get(3));
    }

    /**
     * 全てのプレイヤーのインベントリをクリアする
     */
    private void clearAllPlayerInventory() {

        for ( String name : players ) {
            clearPlayerInventory(name);
        }
    }

    /**
     * プレイヤーのインベントリをクリアする
     * @param name プレイヤー
     */
    private void clearPlayerInventory(String name) {
        Player player = ShooterArena.getPlayerExact(name);
        if ( player != null ) {
            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
        }
    }

    /**
     * チャットをセッションに送信する。
     * @param player 送信したプレイヤー
     * @param message メッセージ
     */
    public void sendChat(Player player, String message) {

        ArrayList<Player> playersToSend = new ArrayList<Player>();

        // 観客には全て送る
        for ( String name : listeners ) {
            Player p = ShooterArena.getPlayerExact(name);
            if ( p != null ) {
                playersToSend.add(p);
            }
        }

        // 発言者が観客か、発言者がプレイヤーかつアリーナチャットモードなら、
        // プレイヤー全員にもメッセージを送る
        if ( listeners.contains(player.getName()) ||
                (players.contains(player.getName()) &&
                        SAConfig.chatmode == ChatMode.ARENA) ) {
            for ( String name : players ) {
                Player p = ShooterArena.getPlayerExact(name);
                if ( p != null ) {
                    playersToSend.add(p);
                }
            }

        // 発言者がプレイヤーかつチームチャットモードなら、
        // チーム全員にもメッセージを送る
        } else if (players.contains(player.getName()) &&
                        SAConfig.chatmode == ChatMode.TEAM) {
            if ( teamRedNames.contains(player.getName()) ) {
                for ( String name : teamRedNames ) {
                    Player p = ShooterArena.getPlayerExact(name);
                    if ( p != null ) {
                        playersToSend.add(p);
                    }
                }
            } else {
                for ( String name : teamBlueNames ) {
                    Player p = ShooterArena.getPlayerExact(name);
                    if ( p != null ) {
                        playersToSend.add(p);
                    }
                }
            }
        }

        String suffix;
        if ( listeners.contains(player.getName()) ) {
            suffix = ChatColor.GRAY + "L";
        } else if ( teamRedNames.contains(player.getName()) ) {
            suffix = ChatColor.RED + "R";
        } else if ( teamBlueNames.contains(player.getName()) ) {
            suffix = ChatColor.BLUE + "B";
        } else {
            suffix = "";
        }

        String m = String.format(TEAM_CHAT_FORMAT,
                ChatColor.WHITE + arena + suffix,
                player.getDisplayName(),
                message);

        // 送信
        for ( Player p : playersToSend ) {
            p.sendMessage(m);
        }
    }

    /**
     * 赤チームに情報を送る
     * @param message メッセージ
     */
    public void sendInfoToRedTeam(String message) {

        String pre = ChatColor.WHITE + arena + ChatColor.RED + "R";
        String m = String.format(TEAM_INFORMATION_FORMAT, pre, message);

        for ( String name : teamRedNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.sendMessage(m);
            }
        }
    }

    /**
     * 青チームに情報を送る
     * @param message メッセージ
     */
    public void sendInfoToBlueTeam(String message) {

        String pre = ChatColor.WHITE + arena + ChatColor.BLUE + "B";
        String m = String.format(TEAM_INFORMATION_FORMAT, pre, message);

        for ( String name : teamBlueNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.sendMessage(m);
            }
        }
    }

    /**
     * プレイヤーのフリーズを設定する
     * @param freeze フリーズするか、解除するか
     */
    public void setFreeze(boolean freeze) {

        if ( freeze ) {
            for ( String name : players ) {
                if ( !ShooterArena.freezePlayers.contains(name) ) {
                    ShooterArena.freezePlayers.add(name);
                }
            }
        } else {
            for ( String name : players ) {
                if ( ShooterArena.freezePlayers.contains(name) ) {
                    ShooterArena.freezePlayers.remove(name);
                }
            }
        }
    }

    /**
     * すべてのプレイヤーをカタパルトで打ち出す
     */
    public void launchAllPlayer() {

        Vector redv = ArenaManager.getArena(arena).getRedVector();
        redv = redv.multiply(SAConfig.catapultPower);
        for ( String name : teamRedNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.setFallDistance(-1000);
                player.setVelocity(redv);
            }
        }

        Vector bluev = ArenaManager.getArena(arena).getBlueVector();
        bluev = bluev.multiply(SAConfig.catapultPower);
        for ( String name : teamBlueNames ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.setFallDistance(-1000);
                player.setVelocity(redv);
            }
        }
    }

    /**
     * 指定のプレイヤーをカタパルトで打ち出す
     * @param player
     */
    public void launchPlayer(Player player) {

        if ( player == null ) {
            return;
        }

        Vector vec;
        if ( teamRedNames.contains(player.getName()) ) {
            vec = ArenaManager.getArena(arena).getRedVector();
        } else {
            vec = ArenaManager.getArena(arena).getBlueVector();
        }
        vec = vec.multiply(SAConfig.catapultPower);

        player.setFallDistance(-1000);
        player.setVelocity(vec);
    }

    /**
     * 指定したプレイヤーに対応するリスポーン地点を取得する
     * @param player プレイヤー
     * @return リスポーン地点
     */
    public Location getRespawnPointByPlayer(Player player) {

        if ( player == null ) {
            return null;
        }

        if ( teamRedNames != null && teamRedNames.contains(player.getName()) ) {
            return ArenaManager.getArena(arena).getRedRespawn();
        } else if ( teamBlueNames != null && teamBlueNames.contains(player.getName()) ) {
            return ArenaManager.getArena(arena).getBlueRespawn();
        }

        return null;
    }

    /**
     * 全てのプレイヤーの経験値を設定する
     */
    public void setAllPlayerExperience() {

        for ( String name : players ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                PlayerExpHandler.setExperience(player, SAConfig.kitsExp);
            }
        }
    }

    /**
     * ゲームスコアを取得する
     * @return ゲームスコア
     */
    public GameScore getScore() {
        return score;
    }

    /**
     * ゲームスコアを設定する
     * @param score ゲームスコア
     */
    public void setScore(GameScore score) {
        this.score = score;
    }

    /**
     * プレイヤーが所属するチームを返す
     * @param name プレイヤー名
     * @return 赤チームなら red、青チームなら blue を返す
     */
    public String getPlayerTeam(String name) {
        if ( teamRedNames.contains(name) )
            return "red";
        else if ( teamBlueNames.contains(name) )
            return "blue";
        else
            return "";
    }
}