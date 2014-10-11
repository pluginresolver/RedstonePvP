package dev.LollipopNinga.RedstonePvP;

import dev.LollipopNinga.RedstonePvP.anvilrepair.AnvilRepair;
import dev.LollipopNinga.RedstonePvP.anvilrepair.AnvilRepairConfig;
import dev.LollipopNinga.RedstonePvP.beacondrops.BeaconDrops;
import dev.LollipopNinga.RedstonePvP.beacondrops.BeaconDropsConfig;
import dev.LollipopNinga.RedstonePvP.bloodspray.BloodSpray;
import dev.LollipopNinga.RedstonePvP.goldtrade.GoldTrade;
import dev.LollipopNinga.RedstonePvP.goldtrade.GoldTradeConfig;
import dev.LollipopNinga.RedstonePvP.randombox.RandomBox;
import dev.LollipopNinga.RedstonePvP.randombox.RandomBoxConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RedstonePvP
  extends JavaPlugin
{
  public static RedstoneConfig config;
  
  @Override
  public void onEnable()
  {
    config = new RedstoneConfig(this, "RedstonePVPConfig.yml");
    
    getCommand("feature").setExecutor(new RedstoneMainCommands());
    getCommand("features").setExecutor(new RedstoneMainCommands());
    
    RandomBoxConfig randomConfig = new RandomBoxConfig(this, "RandomBoxConfig.yml");
    if (randomConfig.getItemIntList().size() == 100)
    {
      RandomBox random = new RandomBox(this, randomConfig);
      getServer().getPluginManager().registerEvents(random, this);
    }
    else
    {
      getLogger().warning("RandomBox cannot set up. Percentages in config don't add up to 100");
    }
    BeaconDrops beacon = new BeaconDrops(this, new BeaconDropsConfig(this, "BeaconDropsConfig.yml"));
    getServer().getPluginManager().registerEvents(beacon, this);
    getCommand("setdropbeacon").setExecutor(beacon);
    
    AnvilRepair anvil = new AnvilRepair(this, new AnvilRepairConfig(this, "AnvilRepairConfig.yml"));
    getServer().getPluginManager().registerEvents(anvil, this);
    
    GoldTrade trade = new GoldTrade(new GoldTradeConfig(this, "TradeConfig.yml"));
    getServer().getPluginManager().registerEvents(trade, this);
    getCommand("settradesign").setExecutor(trade);
    
    getServer().getPluginManager().registerEvents(new BloodSpray(), this);
  }
}