/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.ucchyocean.sa.PlayerExpHandler;

/**
 * @author ucchy
 * Shooterのカスタマイズアイテムクラス
 */
public class Shooter implements ICustomItem {

    private static final String SHOOTER_NAME =
            ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Shooter";
    private static final ArrayList<String> LORE;
    static {
        LORE = new ArrayList<String>();
        LORE.add("飛びたい目標をクリックすることで、慣性を利かせながら");
        LORE.add("飛ぶことが出来る。飛べる回数は有限で、EXPバーで");
        LORE.add("残り燃料を確認することが出来る。");
    }

    private static final int DEFAULT_LEVEL = 6;
    private static final int DEFAULT_COST = 10;
    private static final int MAX_LEVEL = 15;

    private ItemStack item;

    /**
     * コンストラクタ
     */
    public Shooter() {

        item = new ItemStack(Material.BOW, 1);
        ItemMeta shooterMeta = item.getItemMeta();
        shooterMeta.setDisplayName(SHOOTER_NAME);
        shooterMeta.setLore(LORE);
        item.setItemMeta(shooterMeta);
    }

    /**
     * 新しいShooterを作成して取得する
     * @param level Shooterのレベル
     * @return Shooter
     */
    public ItemStack give(int level) {

        ItemStack shooter = item.clone();
        if ( level <= 0 || MAX_LEVEL < level ) {
            level = DEFAULT_LEVEL;
        }
        shooter.addUnsafeEnchantment(Enchantment.OXYGEN, level);

        return shooter;
    }

    /**
     * @see com.github.ucchyocean.sa.item.ICustomItem#onUseByLeftClick(org.bukkit.entity.Player)
     */
    public void onUseByLeftClick(Player player) {

        Material target = player.getTargetBlock(null, 100).getType();

        // クリックが有効範囲かを調べる
        if ( target == Material.AIR ) {
            player.sendMessage(ChatColor.RED + "Out of range!!");
            return;
        }

        // ガラスにはフックできない
        if ( target == Material.GLASS || target == Material.THIN_GLASS ) {
            player.sendMessage(ChatColor.RED + "Cannot hook to glass!!");
            return;
        }

        // 燃料不足かどうかを調べる
        if ( !PlayerExpHandler.hasExperience(player, DEFAULT_COST) ) {
            player.sendMessage(ChatColor.RED + "No fuel!!");
            // TODO: あと何秒で燃料切れから回復するのかを設定する
            return;
        }

        // 燃料を取得
        PlayerExpHandler.takeExperience(player, DEFAULT_COST);

        // レベルを取得
        ItemStack shooter = player.getItemInHand();
        double level = (double)shooter.getEnchantmentLevel(Enchantment.OXYGEN);

        // プレイヤーを吹っ飛ばす
        player.setVelocity(player.getLocation().getDirection().multiply(level));

        // 着地ダメージ無効
        player.setFallDistance(-1000F);

        // エフェクト発生
        player.playEffect(player.getEyeLocation().add(0.5D, 0.0D, 0.5D),
                Effect.POTION_BREAK, 21);
        player.playEffect(player.getEyeLocation().add(0.5D, 0.0D, 0.5D),
                Effect.POTION_BREAK, 21);
    }

    /**
     * @see com.github.ucchyocean.sa.item.ICustomItem#onUseByRightClick(org.bukkit.entity.Player)
     */
    public void onUseByRightClick(Player player) {
        // 右クリックでは何も処理をしない
    }

    /**
     * @see com.github.ucchyocean.sa.item.ICustomItem#getItem()
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @see com.github.ucchyocean.sa.item.ICustomItem#getDisplayName()
     */
    public String getDisplayName() {
        return SHOOTER_NAME;
    }
}
