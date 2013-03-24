/*
 * Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


/**
 * @author ucchy
 * WorldGuardに接続して、WorldGuardのAPIを実行するクラス
 */
public class WorldGuardHandler {

    private WorldGuardPlugin wg;

    /**
     * WorldGuardプラグインをロードする
     * @return ロードできたかどうか
     */
    public boolean loadWorldGuard(Server server) {
        Plugin temp = server.getPluginManager().getPlugin("WorldGuard");
        if ( temp != null && temp instanceof WorldGuardPlugin ) {
            wg = (WorldGuardPlugin)temp;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 指定した領域が存在するかどうかを確認する
     * @param world ワールド
     * @param name 領域名
     * @return 存在するかどうか
     */
    public boolean existRegion(World world, String name) {
        RegionManager manager = wg.getRegionManager(world);
        return manager.hasRegion(name);
    }

    /**
     * 指定の地点が、指定の名前の領域に含まれているかどうかを確認する
     * @param location 地点
     * @param name 領域名
     * @return 含まれているかどうか
     */
    public boolean insideRegion(Location location, String name) {
        RegionManager manager = wg.getRegionManager(location.getWorld());
        ApplicableRegionSet regions = manager.getApplicableRegions(location);
        for ( ProtectedRegion region : regions ) {
            if ( region.getId().equals(name) ) {
                return true;
            }
        }
        return false;
    }
}
