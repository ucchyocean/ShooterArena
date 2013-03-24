/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.sa.item.ICustomItem;
import com.github.ucchyocean.sa.item.Shooter;
import com.github.ucchyocean.sa.listener.CustomItemUseListener;

/**
 * @author ucchy
 * Shooter Arena Plugin
 */
public class ShooterArena extends JavaPlugin {

    public static ShooterArena instance;
    public static WorldGuardHandler wghandler;

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        instance = this;

        initializeCustomItems();

        wghandler = new WorldGuardHandler();
        if ( !wghandler.loadWorldGuard(getServer()) ) {
            getLogger().warning("WorldGuard がロードされていません。");
            getLogger().warning("ShooterArena を無効化します。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    /**
     * カスタムアイテムの初期化メソッド
     */
    public void initializeCustomItems() {

        ArrayList<ICustomItem> items = new ArrayList<ICustomItem>();
        items.add(new Shooter());

        CustomItemUseListener listener = new CustomItemUseListener(items);
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
}
