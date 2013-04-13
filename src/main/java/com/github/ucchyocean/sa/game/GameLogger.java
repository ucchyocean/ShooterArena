/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ucchy
 * ゲームごとのログを出力するクラス
 */
public class GameLogger {

    private static String FILE_NAME_FORMAT = "%s-%s.log";
    private static File folder;
    private static SimpleDateFormat formatForFileName;
    private static SimpleDateFormat formatForLine;

    private File file;

    /**
     * コンストラクタ
     * @param gameName ゲーム名
     */
    public GameLogger(String gameName) {

        file = new File(folder,
                String.format(FILE_NAME_FORMAT,
                        gameName, formatForFileName.format(new Date())));
    }

    /**
     * ログを1行出力する
     * @param message ログ
     */
    public void write(String message) {

        String date = formatForLine.format(new Date());

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            writer.write(date + ", " + message);
            writer.newLine();
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
        }
    }

    /**
     * 初期化。プラグインのonEnableで、1回呼び出しておくこと。
     * @param saveFolder ログ保存フォルダー
     */
    public static void initialize(File saveFolder) {

        folder = saveFolder;
        formatForFileName = new SimpleDateFormat("yyyyMMdd'-'HHmmss");
        formatForLine = new SimpleDateFormat("yyyy-MM-dd','HH:mm:ss");

        if ( !folder.exists() ) {
            folder.mkdirs();
        }
    }
}