/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ucchy
 * ゲームごとのログを出力するクラス
 */
public class SAGameLogger {

    private static String FILE_NAME_FORMAT = "%s-%s.log";
    private static File folder;
    private static SimpleDateFormat format;

    private File file;

    /**
     * コンストラクタ
     * @param gameName ゲーム名
     */
    public SAGameLogger(String gameName) {

        file = new File(
                String.format(FILE_NAME_FORMAT,
                        gameName, format.format(new Date())));
    }

    /**
     * ログを1行出力する
     * @param message ログ
     */
    public void write(String message) {

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(message);
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
        format = new SimpleDateFormat("yyyyMMdd'-'HHmmss");

        if ( !folder.exists() ) {
            folder.mkdirs();
        }
    }
}
