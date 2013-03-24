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
     * アイテムが使用されたときに呼び出されるメソッド
     * @param player 使用者
     */
    public void onUse(Player player);

    /**
     * アイテムを取得するメソッド
     * @return アイテム
     */
    public ItemStack getItem();

    /**
     * DisplayNameを取得するメソッド
     * @return DisplayName
     */
    public String getDisplayName();
}
