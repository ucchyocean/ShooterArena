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

    /**
     * このメソッドは、1秒（20ticks）に1回呼び出される。
     * @see java.lang.Runnable#run()
     */
    public void run() {
        ShooterArena.onEachSeconds();
    }

}
