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
import org.bukkit.util.Vector;

import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.Utility;
import com.github.ucchyocean.sa.game.GameType;
import com.github.ucchyocean.sa.game.MatchMode;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 * アリーナと、そのゲームセッションを管理するクラス
 */
public class ArenaManager {

    protected static Hashtable<String, Arena> arenas;
    protected static Hashtable<String, SAGameSession> sessions;
    protected static Location loungeRespawn;
    static {
        arenas = new Hashtable<String, Arena>();
        sessions = new Hashtable<String, SAGameSession>();
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
            conf.set("lounge.respawn", Utility.convLocationToDesc(loungeRespawn));
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
                conf.set("arenas." + name + ".red.respawn",
                        Utility.convLocationToDesc(redRespawn));
            }

            Vector redVector = arena.getRedVector();
            if ( redVector != null ) {
                conf.set("arenas." + name + ".red.catapult.x", redVector.getX());
                conf.set("arenas." + name + ".red.catapult.y", redVector.getY());
                conf.set("arenas." + name + ".red.catapult.z", redVector.getZ());
            }

            Location blueRespawn = arena.getBlueRespawn();
            if ( blueRespawn != null ) {
                conf.set("arenas." + name + ".blue.respawn",
                        Utility.convLocationToDesc(blueRespawn));
            }

            Vector blueVector = arena.getBlueVector();
            if ( blueVector != null ) {
                conf.set("arenas." + name + ".blue.catapult.x", blueVector.getX());
                conf.set("arenas." + name + ".blue.catapult.y", blueVector.getY());
                conf.set("arenas." + name + ".blue.catapult.z", blueVector.getZ());
            }
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
            loungeRespawn = Utility.convDescToLocation(conf.getString("lounge.respawn"));
        }

        // Arena の復帰
        if ( !conf.contains("arenas") ) {
            return; // アリーナ設定が無ければ、後は何もすることがないのでここで終わる。
        }

        // 一旦初期化
        arenas = new Hashtable<String, Arena>();
        sessions = new Hashtable<String, SAGameSession>();

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
                Location location =
                        Utility.convDescToLocation(section.getString("red.respawn"));
                arena.setRedRespawn(location);
                Vector catapult =
                        convSectionToVector(section.getConfigurationSection("red.catapult"));
                arena.setRedVector(catapult);
            }

            if ( section.contains("blue") ) {
                Location location =
                        Utility.convDescToLocation(section.getString("blue.respawn"));
                arena.setBlueRespawn(location);
                Vector catapult =
                        convSectionToVector(section.getConfigurationSection("blue.catapult"));
                arena.setBlueVector(catapult);
            }
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
            SAGameSession session = sessions.get(key);
            if ( session == null ) {
                result.add(key + " - 空き");
            } else {
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
    public static SAGameSession createNewGameSession(Arena arena, Player leader) {
        SAGameSession session = new SAGameSession(arena);
        session.addPlayer(leader.getName());
        sessions.put(arena.getName(), session);
        return session;
    }

    /**
     * アリーナ名からゲームセッションを取得する
     * @param name アリーナ名
     * @return ゲームセッション
     */
    public static SAGameSession getSession(String name) {
        return sessions.get(name);
    }

    /**
     * ゲームセッションを削除する
     * @param session 削除するゲームセッション
     */
    public static void removeSession(SAGameSession session) {
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
     * 指定したプレイヤーが参加しているゲームセッションを取得する
     * @param player プレイヤー
     * @return ゲームセッション
     */
    public static SAGameSession getSessionByPlayer(Player player) {
        Enumeration<String> keys = sessions.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            SAGameSession session = sessions.get(key);
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
     * x, y, z の Doubleのノードをもった ConfigurationSection を、Vectorに変換して返す
     * @param section セクション
     * @return ベクトル
     */
    private static Vector convSectionToVector(ConfigurationSection section) {
        double x = section.getDouble("x", 0);
        double y = section.getDouble("y", 0);
        double z = section.getDouble("z", 0);
        return new Vector(x, y, z);
    }
}
