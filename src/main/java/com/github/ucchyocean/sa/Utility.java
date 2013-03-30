/*
 * Copyright ucchy 2012
 */
package com.github.ucchyocean.sa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @author ucchy
 * ユーティリティクラス
 */
public class Utility {

    private static final String[] VALID_COLORS = {
        "red", "blue", "yellow", "green", "aqua", "gray", "dark_red",
        "dark_green", "dark_aqua", "black", "dark_blue", "dark_gray",
        "dark_purple", "gold", "light_purple", "white"
    };

    /**
     * jarファイルの中に格納されているファイルを、jarファイルの外にコピーするメソッド
     * @param jarFile jarファイル
     * @param targetFile コピー先
     * @param sourceFilePath コピー元
     * @param isBinary バイナリファイルかどうか
     */
    protected static void copyFileFromJar(
            File jarFile, File targetFile, String sourceFilePath, boolean isBinary) {

        InputStream is = null;
        FileOutputStream fos = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        File parent = targetFile.getParentFile();
        if ( !parent.exists() ) {
            parent.mkdirs();
        }

        try {
            JarFile jar = new JarFile(jarFile);
            ZipEntry zipEntry = jar.getEntry(sourceFilePath);
            is = jar.getInputStream(zipEntry);

            fos = new FileOutputStream(targetFile);

            if ( isBinary ) {
                byte[] buf = new byte[8192];
                int len;
                while ( (len = is.read(buf)) != -1 ) {
                    fos.write(buf, 0, len);
                }

            } else {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                writer = new BufferedWriter(new OutputStreamWriter(fos));

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( writer != null ) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
            if ( reader != null ) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
            if ( fos != null ) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
            if ( is != null ) {
                try {
                    is.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }
    }

    /**
     * 文字列内のカラーコードを置き換えする
     * @param source 置き換え元の文字列
     * @return 置き換え後の文字列
     */
    public static String replaceColorCode(String source) {

        return source.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
    }

    /**
     * 文字列が整数値に変換可能かどうかを判定する
     * @param source 変換対象の文字列
     * @return 整数に変換可能かどうか
     */
    public static boolean tryIntParse(String source) {

        return source.matches("^-?[0-9]+$");
    }

    /**
     * ColorMeの色設定を、ChatColorクラスに変換する
     * @param color ColorMeの色設定
     * @return ChatColorクラス
     */
    public static ChatColor replaceColors(String color) {

        if ( isValidColor(color) ) {
            return ChatColor.valueOf(color.toUpperCase());
        }
        return ChatColor.WHITE;
    }

    /**
     * ColorMeで指定可能な色かどうかを判断する
     * @param color ColorMeの色設定
     * @return 指定可能かどうか
     */
    public static boolean isValidColor(String color) {

        for ( String s : VALID_COLORS ) {
            if ( s.equals(color) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Location を文字列表現に変換して返す
     * @param location 変換対象
     * @return 文字列表現
     */
    public static String convLocationToDesc(Location location) {

        return String.format("%s_%d_%d_%d",
                location.getWorld().getName(), location.getBlockX(),
                location.getBlockY(), location.getBlockZ());
    }

    /**
     * 文字列表現をLocationに変換して返す
     * @param desc 文字列表現
     * @return 生成されたLocation
     */
    public static Location convDescToLocation(String desc) {

        String[] args = desc.split("_");
        if ( args.length >= 4 ) {
            return null;
        }

        World world = ShooterArena.getWorld(args[0]);
        if ( world == null ) {
            return null;
        }

        if ( !Utility.tryIntParse(args[1]) ||
                !Utility.tryIntParse(args[2]) ||
                !Utility.tryIntParse(args[3]) ) {
            return null;
        }
        double x = Integer.parseInt(args[1]) + 0.5;
        double y = Integer.parseInt(args[2]);
        double z = Integer.parseInt(args[3]) + 0.5;

        return new Location(world, x, y, z);
    }

    /**
     * 指定されたlocationを、使いやすいように、ブロックの中心に変更します。
     * @param location 変換するLocation
     * @return 変換されたLocation
     */
    public static Location toCenterOfBlock(Location location) {

        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;

        return new Location(location.getWorld(), x, y, z);
    }

    /**
     * 指定のブロックが、看板かどうかを調べる
     * @param block ブロック
     * @return 看板かどうか
     */
    public static boolean isSign(Block block) {

        if ( block == null ) {
            return false;
        }

        return ( block.getType() == Material.SIGN ||
                block.getType() == Material.SIGN_POST ||
                block.getType() == Material.WALL_SIGN );
    }
}
