/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

/**
 * @author ucchy
 * チャットモードを示す列挙体
 */
public enum ChatMode {

    /** グローバルチャット */
    GLOBAL("global"),

    /** アリーナチャット */
    ARENA("arena"),

    /** チームチャット */
    TEAM("team");

    private String id;

    /**
     * コンストラクタ
     * @param id ID
     */
    ChatMode(String id) {
        this.id = id;
    }

    /**
     * このenumを示す文字列を返す
     * @see java.lang.Enum#toString()
     */
    public String toString() {
        return id;
    }

    /**
     * 指定した文字列（ID）から、ChatModeを作成して返す
     * @param id ID
     * @return ChatMode、ただし無効なIDが指定された場合は null が返される
     */
    public static ChatMode fromString(String id) {
        if ( id == null ) {
            return null;
        }
        for ( ChatMode cm : values() ) {
            if ( id.equalsIgnoreCase(cm.toString()) ) {
                return cm;
            }
        }
        return null;
    }
}
