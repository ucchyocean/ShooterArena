/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.game.GameSession;
import com.github.ucchyocean.sa.game.GameType;
import com.github.ucchyocean.sa.game.MatchMode;

/**
 * @author ucchy
 * アリーナと、そのゲームセッションを管理するクラス
 */
public class ArenaManager {

    protected static Hashtable<String, Arena> arenas;
    protected static Hashtable<String, GameSession> sessions;
    protected static Location loungeRespawn;
    static {
        arenas = new Hashtable<String, Arena>();
        sessions = new Hashtable<String, GameSession>();
    }

    private static File file;

    /**
     * ファイルに保存します。
     */
    public static void save() {

        if ( file == null ) {
            file = new File(ShooterArena.instance.getDataFolder(), "arena.yml");
        }

        YamlConfiguration conf = new YamlConfiguration();

        // loungeRespawn の保存
        if ( loungeRespawn != null ) {
            conf.set("lounge.respawn.world", loungeRespawn.getWorld().getName());
            conf.set("lounge.respawn.x", loungeRespawn.getBlockX());
            conf.set("lounge.respawn.y", loungeRespawn.getBlockY());
            conf.set("lounge.respawn.z", loungeRespawn.getBlockZ());
            conf.set("lounge.respawn.yaw", loungeRespawn.getYaw());
            conf.set("lounge.respawn.pitch", loungeRespawn.getPitch());
        }

        // Arena の保存
        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String name = keys.nextElement();
            Arena arena = arenas.get(name);

            conf.createSection("arenas." + name);

            MatchMode mode = arena.getMode();
            if ( mode != null ) {
                conf.set("arenas." + name + ".mode.type", mode.type);
                conf.set("arenas." + name + ".mode.life", mode.life);
                conf.set("arenas." + name + ".mode.minute", mode.minute);
            }

            ArenaSign sign = arena.getSign();
            if ( sign != null ) {
                conf.set("arenas." + name + ".sign", sign.toLocationDesc());
            }

            Location redRespawn = arena.getRedRespawn();
            if ( redRespawn != null ) {
                conf.set("arenas." + name + ".redrespawn.world",
                        redRespawn.getWorld().getName());
                conf.set("arenas." + name + ".redrespawn.x",
                        redRespawn.getBlockX());
                conf.set("arenas." + name + ".redrespawn.y",
                        redRespawn.getBlockY());
                conf.set("arenas." + name + ".redrespawn.z",
                        redRespawn.getBlockZ());
                conf.set("arenas." + name + ".redrespawn.yaw",
                        redRespawn.getYaw());
                conf.set("arenas." + name + ".redrespawn.pitch",
                        redRespawn.getPitch());
            }

            Location blueRespawn = arena.getBlueRespawn();
            if ( blueRespawn != null ) {
                conf.set("arenas." + name + ".bluerespawn.world",
                        blueRespawn.getWorld().getName());
                conf.set("arenas." + name + ".bluerespawn.x",
                        blueRespawn.getBlockX());
                conf.set("arenas." + name + ".bluerespawn.y",
                        blueRespawn.getBlockY());
                conf.set("arenas." + name + ".bluerespawn.z",
                        blueRespawn.getBlockZ());
                conf.set("arenas." + name + ".bluerespawn.yaw",
                        blueRespawn.getYaw());
                conf.set("arenas." + name + ".bluerespawn.pitch",
                        blueRespawn.getPitch());
            }
        }

        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ファイルからロードします。
     */
    public static void load() {

        if ( file == null ) {
            file = new File(ShooterArena.instance.getDataFolder(), "arena.yml");
        }

        if ( !file.exists() ) {
            YamlConfiguration conf = new YamlConfiguration();
            try {
                conf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        // loungeRespawn の復帰
        if ( conf.contains("lounge.respawn") ) {
            loungeRespawn = convSectionToLocation(
                    conf.getConfigurationSection("lounge.respawn"));
        }

        // Arena の復帰
        if ( !conf.contains("arenas") ) {
            return; // アリーナ設定が無ければ、後は何もすることがないのでここで終わる。
        }

        // 一旦初期化
        arenas = new Hashtable<String, Arena>();
        sessions = new Hashtable<String, GameSession>();

        // arenas の復帰
        ConfigurationSection arenaSection = conf.getConfigurationSection("arenas");
        Iterator<String> i = arenaSection.getValues(false).keySet().iterator();
        while (i.hasNext()) {
            String name = i.next();
            Arena arena = new Arena(name);

            ConfigurationSection section = arenaSection.getConfigurationSection(name);

            if ( section.contains("mode") ) {
                GameType type = GameType.fromString(section.getString("mode.type"));
                int life = section.getInt("mode.life", -1);
                int minute = section.getInt("mode,minute", -1);
                MatchMode mode = new MatchMode(type, life, minute);
                arena.setMode(mode);
            }

            if ( section.contains("sign") ) {
                ArenaSign.fromLocationDesc(arena, section.getString("sign"));
            }

            if ( section.contains("red") ) {
                Location location = convSectionToLocation(
                        section.getConfigurationSection("red.respawn"));
                arena.setRedRespawn(location);
            }

            if ( section.contains("blue") ) {
                Location location = convSectionToLocation(
                        section.getConfigurationSection("blue.respawn"));
                arena.setBlueRespawn(location);
            }

            arenas.put(name, arena);
        }
    }

    /**
     * 既に存在するアリーナ名かどうかを確認する
     * @param name アリーナ名
     * @return 存在するかどうか
     */
    public static boolean existArenaName(String name) {
        return arenas.containsKey(name);
    }

    /**
     * 既に登録済みの領域かどうかを確認する
     * @param name 領域名
     * @return 登録済みかどうか
     */
    public static boolean existArenaRegion(String name) {
        World world = ShooterArena.getWorld(SAConfig.defaultWorldName);
        if ( world == null ) {
            return false;
        }
        return ShooterArena.wghandler.existRegion(world, name);
    }

    /**
     * 新しいアリーナを登録する
     * @param name アリーナ名
     * @return アリーナ
     */
    public static Arena registerNewArena(String name) {
        Arena arena = new Arena(name);
        arenas.put(name, arena);
        save();
        return arena;
    }

    /**
     * アリーナを取得する
     * @param name アリーナ名
     * @return アリーナ
     */
    public static Arena getArena(String name) {
        return arenas.get(name);
    }

    /**
     * アリーナ名のリストを取得する
     * @return アリーナ名のリスト
     */
    public static ArrayList<String> getArenaNames() {
        ArrayList<String> result = new ArrayList<String>();
        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            result.add(key);
        }
        return result;
    }

    /**
     * 指定した位置からアリーナを取得する
     * @param location 位置
     * @return アリーナ
     */
    public static Arena getArenaByLocation(Location location) {
        String regionName =
                ShooterArena.wghandler.getRegionNameFromLocation(
                        location, ArenaManager.getArenaNames() );
        if ( regionName == null ) {
            return null;
        } else {
            return arenas.get(regionName);
        }
    }

    /**
     * アリーナを削除する
     * @param name アリーナ名
     */
    public static void removeArena(String name) {
        if ( arenas.containsKey(name) ) {
            arenas.remove(name);
            save();
        }
    }

    /**
     * アリーナのリストを取得する
     * @return アリーナのリスト
     */
    public static ArrayList<String> getArenaList() {

        ArrayList<String> result = new ArrayList<String>();

        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            if ( sessions.containsKey(key) ) {
                result.add(key + " - 空き");
            } else {
                GameSession session = sessions.get(key);
                result.add(key + " - " + session.phase.toJapanese());
            }
        }

        return result;
    }

    /**
     * 新しいゲームセッションを作成して返す
     * @param arena ゲームを行うアリーナ
     * @return 新しいゲームセッション
     */
    public static GameSession createNewGameSession(Arena arena, Player leader) {
        GameSession session = new GameSession(arena);
        session.addPlayer(leader.getName());
        sessions.put(arena.getName(), session);
        return session;
    }

    /**
     * アリーナ名からゲームセッションを取得する
     * @param name アリーナ名
     * @return ゲームセッション
     */
    public static GameSession getSession(String name) {
        return sessions.get(name);
    }

    /**
     * ゲームセッションを削除する
     * @param session 削除するゲームセッション
     */
    public static void removeSession(GameSession session) {
        if ( sessions.contains(session) ) {
            sessions.remove(session.arena);
        }
    }

    /**
     * ゲームセッションを削除する
     * @param name 削除対象のアリーナ名
     */
    public static void removeSession(String name) {
        if ( sessions.containsKey(name) ) {
            sessions.remove(name);
        }
    }

    /**
     * 全てのゲームセッションをキャンセルする
     */
    public static void cancelAllSessions() {
        for ( String key : sessions.keySet() ) {
            GameSession session = sessions.get(key);
            session.cancelGame();
        }
    }

    /**
     * 指定したプレイヤーが参加しているゲームセッションを取得する
     * @param player プレイヤー
     * @return ゲームセッション
     */
    public static GameSession getSessionByPlayer(Player player) {
        Enumeration<String> keys = sessions.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            GameSession session = sessions.get(key);
            if ( session.getPlayers().contains(player.getName()) ) {
                return session;
            }
        }
        return null;
    }

    /**
     * 全てのArenaSignを返す
     * @return 全てのArenaSign
     */
    public static ArrayList<ArenaSign> getAllArenaSign() {
        ArrayList<ArenaSign> signs = new ArrayList<ArenaSign>();
        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            Arena arena = arenas.get(key);
            if ( arena.getSign() != null ) {
                signs.add(arena.getSign());
            }
        }
        return signs;
    }

    /**
     * ラウンジリスポーンポイントを設定します。
     * @param location
     */
    public static void setLoungeRespawn(Location location) {
        loungeRespawn = location;
        save();
    }

    /**
     * ラウンジリスポーンポイントを取得します。
     * @return ラウンジリスポーンポイント
     */
    public static Location getLoungeRespawn() {
        return loungeRespawn;
    }

    /**
     * アリーナサインをアリーナに設定します。
     * @param arena アリーナ
     * @param sign アリーナサイン
     */
    public static void registerArenaSign(Arena arena, Sign sign) {
        ArenaSign as = new ArenaSign(sign);
        as.setParent(arena);
        save();
    }

    /**
     * アリーナに赤チームリスポーン地点を設定します。
     * @param arena アリーナ
     * @param location リスポーン地点
     */
    public static void setRedRespawn(Arena arena, Location location) {
        arena.setRedRespawn(location);
        save();
    }

    /**
     * アリーナに青チームリスポーン地点を設定します。
     * @param arena アリーナ
     * @param location リスポーン地点
     */
    public static void setBlueRespawn(Arena arena, Location location) {
        arena.setBlueRespawn(location);
        save();
    }

    /**
     * x, y, z, yaw, pitch のノードをもった ConfigurationSection を、Locationに変換して返す
     * @param section セクション
     * @return ベクトル
     */
    private static Location convSectionToLocation(ConfigurationSection section) {
        String world_temp = section.getString("world", "world");
        World world = ShooterArena.getWorld(world_temp);
        double x = section.getInt("x", 0) + 0.5;
        double y = section.getInt("y", 64);
        double z = section.getInt("z", 0) + 0.5;
        float yaw = (float)section.getDouble("yaw", 0);
        float pitch = (float)section.getDouble("pitch", 0);

        return new Location(world, x, y, z, yaw, pitch);
    }
}
