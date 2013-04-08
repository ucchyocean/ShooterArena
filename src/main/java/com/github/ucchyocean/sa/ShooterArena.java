/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.command.ShooterArenaCommand;
import com.github.ucchyocean.sa.command.ShooterCommand;
import com.github.ucchyocean.sa.game.GameTimer;
import com.github.ucchyocean.sa.game.SAGameLogger;
import com.github.ucchyocean.sa.item.ICustomItem;
import com.github.ucchyocean.sa.item.Shooter;
import com.github.ucchyocean.sa.listener.CustomItemUseListener;
import com.github.ucchyocean.sa.listener.LoungeSignListener;

/**
 * @author ucchy
 * Shooter Arena Plugin
 */
public class ShooterArena extends JavaPlugin {

    public static final String PREFIX = "[SA]";
    public static final String PREERR = ChatColor.RED + PREFIX;
    public static final String PREINFO = ChatColor.AQUA + PREFIX;
    public static final String PRENOTICE = ChatColor.LIGHT_PURPLE + PREFIX;

    public static ShooterArena instance;
    public static WorldGuardHandler wghandler;
    public static KitHandler khandler;
    public static TeamHandler thandler;

    public static ArrayList<Player> freezePlayers;
    public static Hashtable<String, ICustomItem> customItems;

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        instance = this;

        // WorldGuard の読み込み
        wghandler = new WorldGuardHandler();
        if ( !wghandler.loadWorldGuard(getServer()) ) {
            getLogger().warning("WorldGuard がロードされていません。");
            getLogger().warning("ShooterArena を無効化します。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 設定の読み込み
        SAConfig.reloadConfig();

        // アリーナデータの読み込み
        ArenaManager.load();

        // カスタムアイテムの初期化
        initializeCustomItems();

        // ゲームロガーの初期化
        SAGameLogger.initialize(new File(getDataFolder(), "gamelog"));

        // リスナーの登録
        getServer().getPluginManager().registerEvents(new LoungeSignListener(), this);

        // コマンドの登録
        getCommand("ShooterArena").setExecutor(new ShooterArenaCommand());
        getCommand("Shooter").setExecutor(new ShooterCommand());

        // その他初期化
        khandler = new KitHandler(getLogger());
        thandler = new TeamHandler(this.getServer().getScoreboardManager().getMainScoreboard());
        freezePlayers = new ArrayList<Player>();
    }

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
    public void onDisable() {

        // 全てのGameSessionをキャンセルする
        ArenaManager.cancelAllSessions();
    }

    /**
     * カスタムアイテムの初期化メソッド
     */
    public void initializeCustomItems() {

        customItems = new Hashtable<String, ICustomItem>();

        ICustomItem shooter = new Shooter();
        customItems.put(shooter.getName(), shooter);

        CustomItemUseListener listener = new CustomItemUseListener(customItems.values());
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * 指定したワールドにいる全てのプレイヤーを取得する。
     * ただし、指定したワールドが存在しない場合は、空のリストが返される。
     * @param worldName ワールド名
     * @return 指定したワールドにいる全てのプレイヤー
     */
    public static ArrayList<Player> getAllPlayersOnWorld(String worldName) {

        Player[] temp = instance.getServer().getOnlinePlayers();
        ArrayList<Player> result = new ArrayList<Player>();
        World world = getWorld(worldName);
        if ( world == null ) {
            return result;
        }
        for ( Player p : temp ) {
            if ( p.getWorld().equals(world) ) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * メッセージをブロードキャストに送信する。
     * @param message 送信するメッセージ
     */
    public static void sendBroadcast(String message) {
        instance.getServer().broadcastMessage(message);
    }

    /**
     * プレイヤー名からPlayerインスタンスを返す。
     * @param name プレイヤー名
     * @return
     */
    public static Player getPlayerExact(String name) {
        return instance.getServer().getPlayerExact(name);
    }

    /**
     * ワールド名からWorldインスタンスを返す。
     * @param name ワールド名
     * @return
     */
    public static World getWorld(String name) {
        return instance.getServer().getWorld(name);
    }

    /**
     * このプラグインのJarファイル自身を示すFileクラスを返す。
     * @return
     */
    protected static File getPluginJarFile() {
        return instance.getFile();
    }

    public static void startGameTimer(GameTimer timer) {
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, timer, 20, 20);
    }
}
