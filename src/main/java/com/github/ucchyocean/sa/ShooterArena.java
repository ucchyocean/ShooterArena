/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.command.ShooterArenaCommand;
import com.github.ucchyocean.sa.command.ShooterCommand;
import com.github.ucchyocean.sa.game.GameLogger;
import com.github.ucchyocean.sa.game.GameTimer;
import com.github.ucchyocean.sa.item.ICustomItem;
import com.github.ucchyocean.sa.item.Shooter;
import com.github.ucchyocean.sa.listener.CustomItemUseListener;
import com.github.ucchyocean.sa.listener.EntityDamageListener;
import com.github.ucchyocean.sa.listener.LoungeSignListener;
import com.github.ucchyocean.sa.listener.PlayerListener;

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

    public static ArrayList<String> freezePlayers;
    public static Hashtable<String, ICustomItem> customItems;

    private ArrayList<GameTimer> timers;

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
        GameLogger.initialize(new File(getDataFolder(), "gamelog"));

        // リスナーの登録
        getServer().getPluginManager().registerEvents(new LoungeSignListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // コマンドの登録
        getCommand("ShooterArena").setExecutor(new ShooterArenaCommand());
        getCommand("Shooter").setExecutor(new ShooterCommand());

        // スケジューラーの登録
        timers = new ArrayList<GameTimer>();
        getServer().getScheduler().scheduleSyncRepeatingTask(
                instance, new TimerBase(this), 20, 20);

        // その他初期化
        khandler = new KitHandler(getLogger());
        thandler = new TeamHandler(this.getServer().getScoreboardManager().getMainScoreboard());
        freezePlayers = new ArrayList<String>();
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
     * 全てのワールドを返す。
     * @return 全てのワールド
     */
    public static List<World> getWorlds() {
        return instance.getServer().getWorlds();
    }

    /**
     * このプラグインのJarファイル自身を示すFileクラスを返す。
     * @return
     */
    protected static File getPluginJarFile() {
        return instance.getFile();
    }

    /**
     * ゲームタイマーを開始する
     * @param timer
     */
    public static void startGameTimer(GameTimer timer) {
        instance.timers.add(timer);
    }

    /**
     * ゲームタイマーを終了する
     * @param timer
     */
    public static void cancelGameTimer(GameTimer timer) {
        if ( instance.timers.contains(timer) )
            instance.timers.remove(timer);
    }

    /**
     * このメソッドは、1秒（20ticks）に1回呼び出される。
     */
    protected void onEachSeconds() {

        // 登録されているタイマーの処理を行う
        final ArrayList<GameTimer> timers = new ArrayList<GameTimer>(this.timers);
        for ( GameTimer timer : timers ) {
            if ( timer != null ) {
                timer.onTick();
            }
        }
    }
}
