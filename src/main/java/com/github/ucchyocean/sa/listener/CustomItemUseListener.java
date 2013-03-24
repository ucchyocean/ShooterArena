/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.ucchyocean.sa.item.ICustomItem;

/**
 * @author ucchy
 * プレイヤーのアイテム使用をハンドリングするリスナークラス
 */
public class CustomItemUseListener implements Listener {

    private ArrayList<ICustomItem> items;

    /**
     * コンストラクタ
     * @param items リスナー対象にするカスタムアイテム
     */
    public CustomItemUseListener(ArrayList<ICustomItem> items) {
        this.items = items;
    }

    /**
     * プレイヤーがクリックしたイベント
     * @param event イベント
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        boolean isLeftClick = false;

        // 左クリックor右クリックイベントでなければ対象外
        if ( event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.LEFT_CLICK_BLOCK ) {
            isLeftClick = true;
        } else if ( event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
            isLeftClick = false;
        } else {
            return;
        }

        // 何も持っていない場合は対象外
        if ( player.getItemInHand() == null ||
                player.getItemInHand().getType() == Material.AIR ) {
            return;
        }

        // DisplayNameが設定されていないアイテムを持っている場合は対象外
        if ( player.getItemInHand().getItemMeta().getDisplayName() == null ) {
            return;
        }

        String displayName = player.getItemInHand().getItemMeta().getDisplayName();

        // DisplayNameが一致するアイテムを探して、onUseを実行する
        for ( ICustomItem i : items ) {
            if ( displayName.equals(i.getDisplayName()) ) {
                if ( isLeftClick ) {
                    i.onUseByLeftClick(player);
                } else {
                    i.onUseByRightClick(player);
                }
                event.setCancelled(true);
                return;
            }
        }
    }
}
