/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author ucchy
 * タイマーのベースとなるクラス
 */
public class TimerBase extends BukkitRunnable {
    
    private ShooterArena plugin;
    
    public TimerBase(ShooterArena plugin) {
        this.plugin = plugin;
    }

    /**
     * このメソッドは、1秒（20ticks）に1回呼び出される。
     * @see java.lang.Runnable#run()
     */
    public void run() {
        plugin.onEachSeconds();
    }
}
