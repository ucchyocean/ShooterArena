/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.item;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.ucchyocean.sa.PlayerExpHandler;
import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;

/**
 * @author ucchy
 * Shooterのカスタマイズアイテムクラス
 */
public class Shooter implements ICustomItem {

    private static final String NAME = "shooter";
    private static final String DISPLAY_NAME =
            ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + NAME;

    private static final int DEFAULT_LEVEL = 4;
    private static final int DEFAULT_COST = 10;
    private static final int MAX_LEVEL = 15;
    private static final int RANGE = 50;

    private ItemStack item;

    /**
     * コンストラクタ
     */
    public Shooter() {

        item = new ItemStack(Material.TRIPWIRE_HOOK, 1);
        ItemMeta shooterMeta = item.getItemMeta();
        shooterMeta.setDisplayName(DISPLAY_NAME);
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
    public void onUseByLeftClick(final Player player) {

        Material target = player.getTargetBlock(null, RANGE).getType();
        Location eLoc = player.getEyeLocation();

        // クリックが有効範囲かを調べる
        if ( target == Material.AIR ) {
            player.sendMessage(ChatColor.RED + "Out of range!!");
            player.playEffect(eLoc, Effect.SMOKE, 4);
            player.playEffect(eLoc, Effect.SMOKE, 4);
            player.playSound(eLoc, Sound.IRONGOLEM_THROW, (float)1.0, (float)1.5);
            return;
        }

        // ガラスにはフックできない
        if ( target == Material.GLASS || target == Material.THIN_GLASS ) {
            player.sendMessage(ChatColor.RED + "Cannot hook to glass!!");
            player.playEffect(eLoc, Effect.SMOKE, 4);
            player.playEffect(eLoc, Effect.SMOKE, 4);
            player.playSound(eLoc, Sound.IRONGOLEM_THROW, (float)1.0, (float)1.5);
            return;
        }

        // 燃料不足かどうかを調べる
        if ( !PlayerExpHandler.hasExperience(player, DEFAULT_COST) ) {
            player.sendMessage(ChatColor.RED + "No fuel!!");
            player.playEffect(eLoc, Effect.SMOKE, 4);
            player.playEffect(eLoc, Effect.SMOKE, 4);
            player.playSound(eLoc, Sound.IRONGOLEM_THROW, (float)1.0, (float)1.5);
            return;
        }

        // 燃料を取得
        PlayerExpHandler.takeExperience(player, DEFAULT_COST);

        // 燃料切れになったら、規定秒数後に再チャージする
        if ( SAConfig.reviveExp && !PlayerExpHandler.hasExperience(player, DEFAULT_COST) ) {
            player.sendMessage(ChatColor.GOLD + "燃料切れです。あと " +
                    SAConfig.reviveExpSeconds + "秒で再チャージされます。");
            BukkitRunnable runnable = new BukkitRunnable() {
                public void run() {
                    PlayerExpHandler.takeExperience(player, -SAConfig.reviveExpAmount);
                }
            };
            runnable.runTaskLater(ShooterArena.instance, SAConfig.reviveExpSeconds * 20);
        }

        // レベルを取得
        ItemStack shooter = player.getItemInHand();
        double level = (double)shooter.getEnchantmentLevel(Enchantment.OXYGEN);

        // プレイヤーを吹っ飛ばす
        player.setVelocity(player.getLocation().getDirection().multiply(level));

        // エフェクト発生
        player.playEffect(eLoc, Effect.POTION_BREAK, 21);
        player.playEffect(eLoc, Effect.POTION_BREAK, 21);
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
        return give(DEFAULT_LEVEL);
    }

    /**
     * @see com.github.ucchyocean.sa.item.ICustomItem#getDisplayName()
     */
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    /**
     * @see com.github.ucchyocean.sa.item.ICustomItem#getName()
     */
    public String getName() {
        return NAME;
    }
}
