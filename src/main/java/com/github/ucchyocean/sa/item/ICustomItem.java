/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author ucchy
 * カスタムアイテムのインターフェイス
 */
public interface ICustomItem {

    /**
     * 左クリックでアイテムが使用されたときに呼び出されるメソッド
     * @param player 使用者
     */
    public void onUseByLeftClick(Player player);

    /**
     * 右クリックでアイテムが使用されたときに呼び出されるメソッド
     * @param player 使用者
     */
    public void onUseByRightClick(Player player);

    /**
     * アイテムを取得するメソッド
     * @return アイテム
     */
    public ItemStack getItem();

    /**
     * アイテム表示名を取得するメソッド
     * @return アイテム表示名
     */
    public String getDisplayName();

    /**
     * カスタムアイテム名を取得するメソッド
     * @return アイテム名
     */
    public String getName();
}
