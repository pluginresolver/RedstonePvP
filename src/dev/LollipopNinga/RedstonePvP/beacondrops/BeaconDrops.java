package dev.LollipopNinga.RedstonePvP.beacondrops;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import dev.LollipopNinga.RedstonePvP.RedstoneFeature;
import dev.LollipopNinga.RedstonePvP.RedstonePvP;

public class BeaconDrops
  implements Listener, CommandExecutor
{
  private RedstonePvP plugin;
  private BeaconDropsConfig config;
  private boolean canRun;
  private Set<String> beaconSetters;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public BeaconDrops(RedstonePvP plugin, BeaconDropsConfig config)
  {
    this.plugin = plugin;
    this.config = config;
    this.canRun = true;
    this.beaconSetters = new HashSet();
  }
  
  @SuppressWarnings("deprecation")
@EventHandler
  public void onClickBeacon(PlayerInteractEvent event)
  {
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.BEACON_DROPS)) {
      return;
    }
    Player ply = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getClickedBlock() == null) {
      return;
    }
    if (!(event.getClickedBlock().getState() instanceof Beacon))
    {
      if (this.beaconSetters.contains(ply.getName()))
      {
        ply.sendMessage(this.config.getMessage("not-beacon"));
        this.beaconSetters.remove(ply.getName());
      }
      return;
    }
    Beacon beacon = (Beacon)event.getClickedBlock().getState();
    if (this.beaconSetters.contains(ply.getName()))
    {
      event.setCancelled(true);
      if (!this.config.isDropBeacon(beacon))
      {
        this.config.setBeacon(beacon);
        ply.sendMessage(this.config.getMessage("beacon-set"));
      }
      else
      {
        ply.sendMessage(this.config.getMessage("already-set"));
      }
      this.beaconSetters.remove(ply.getName());
      
      return;
    }
    if (this.config.isDropBeacon(beacon))
    {
      event.setCancelled(true);
      if (!this.canRun)
      {
        ply.sendMessage(this.config.getMessage("wait"));
        return;
      }
      if (Bukkit.getOnlinePlayers().length < this.config.getPlayerAmount()) //<-- you get an error ingame?
      {
        ply.sendMessage(this.config.getMessage("not-enough-players"));
        return;
      }
      runBeaconDrops(beacon);
    }
  }
  
  @EventHandler(ignoreCancelled=true)
  public void onBeaconBreak(BlockBreakEvent event)
  {
    if (!(event.getBlock().getState() instanceof Beacon)) {
      return;
    }
    Beacon beacon = (Beacon)event.getBlock().getState();
    if (this.config.isDropBeacon(beacon)) {
      this.config.removeBeacon();
    }
  }
  
  @EventHandler
  public void onPlayerLogout(PlayerQuitEvent event)
  {
    String name = event.getPlayer().getName();
    if (this.beaconSetters.contains(name)) {
      this.beaconSetters.remove(name);
    }
  }
  
  private void runBeaconDrops(Beacon beacon)
  {
    this.canRun = false;
    runPhaseOne(beacon, 1);
  }
  
  private void runPhaseOne(final Beacon beacon, final int repeat)
  {
    if (repeat > this.config.getRepeat())
    {
      this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, new Runnable()
      {
        public void run()
        {
          BeaconDrops.this.plugin.getServer().broadcastMessage(BeaconDrops.this.config.getMessage("charged"));
          BeaconDrops.this.canRun = true;
        }
      }, this.config.getWaitTime() * 20L * 60L);
      
      return;
    }
    final Block belowBlock = beacon.getWorld().getBlockAt(beacon.getLocation().add(0.0D, -1.0D, 0.0D));
    final Random rnd = new Random();
    
    final int blockSwitchTask = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
    {
      @SuppressWarnings("deprecation")
	public void run()
      {
        switch (belowBlock.getType())
        {
        case COOKED_CHICKEN: 
          belowBlock.setType(Material.GOLD_BLOCK);
          break;
        default: 
          belowBlock.setType(Material.DIAMOND_BLOCK);
        }
        beacon.getWorld().dropItemNaturally(beacon.getLocation().add(0.0D, 1.0D, 0.0D), new ItemStack(BeaconDrops.this.config.getItemId(), rnd.nextInt(64) + 1));
      }
    }, 10L, 10L);
    
    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
    {
      public void run()
      {
        BeaconDrops.this.plugin.getServer().getScheduler().cancelTask(blockSwitchTask);
        belowBlock.setType(Material.DIAMOND_BLOCK);
        
        BeaconDrops.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(BeaconDrops.this.plugin, new Runnable()
        {
          public void run()
          {
          	runPhaseTwo(beacon, repeat);
          }
        }, 60L);
      }
    }, this.config.getPhaseOne() * 20L);
  }
  
  private void runPhaseTwo(final Beacon beacon, final int repeat)
  {
    final Location loc = beacon.getLocation().add(0.0D, 1.0D, 0.0D);
    final Block aboveBlock = loc.getWorld().getBlockAt(loc);
    final Random rnd = new Random();
    
    loc.getWorld().playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
    aboveBlock.setType(Material.WATER);
    spawnPressurePlates(beacon);
    
    final int itemDropTask = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
    {
      @SuppressWarnings("deprecation")
	public void run()
      {
        beacon.getWorld().dropItemNaturally(loc, new ItemStack(BeaconDrops.this.config.getItemId(), rnd.nextInt(64) + 1));
      }
    }, 10L, 10L);
    
    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
    {
      public void run()
      {
        BeaconDrops.this.plugin.getServer().getScheduler().cancelTask(itemDropTask);
        aboveBlock.setType(Material.AIR);
        
        BeaconDrops.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(BeaconDrops.this.plugin, new Runnable()
        {
          public void run()
          {
            BeaconDrops.this.removePressurePlates(beacon);
            BeaconDrops.this.runPhaseOne(beacon, repeat + 1);
          }
        }, 60L);
      }
    }, this.config.getPhaseTwo() * 20L);
  }
  
  private void spawnPressurePlates(Beacon beacon)
  {
    for (int x = -2; x <= 2; x++)
    {
      Block block = beacon.getWorld().getBlockAt(beacon.getLocation().add(x, -1.0D, 3.0D));
      if (block.getType() == Material.AIR) {
        block.setType(Material.WOOD_PLATE);
      }
      block = beacon.getWorld().getBlockAt(beacon.getLocation().add(x, -1.0D, -3.0D));
      if (block.getType() == Material.AIR) {
        block.setType(Material.WOOD_PLATE);
      }
    }
    for (int z = -2; z <= 2; z++)
    {
      Block block = beacon.getWorld().getBlockAt(beacon.getLocation().add(3.0D, -1.0D, z));
      if (block.getType() == Material.AIR) {
        block.setType(Material.WOOD_PLATE);
      }
      block = beacon.getWorld().getBlockAt(beacon.getLocation().add(-3.0D, -1.0D, z));
      if (block.getType() == Material.AIR) {
        block.setType(Material.WOOD_PLATE);
      }
    }
  }
  
  private void removePressurePlates(Beacon beacon)
  {
    for (int x = -2; x <= 2; x++)
    {
      Block block = beacon.getWorld().getBlockAt(beacon.getLocation().add(x, -1.0D, 3.0D));
      if (block.getType() == Material.WOOD_PLATE) {
        block.setType(Material.AIR);
      }
      block = beacon.getWorld().getBlockAt(beacon.getLocation().add(x, -1.0D, -3.0D));
      if (block.getType() == Material.WOOD_PLATE) {
        block.setType(Material.AIR);
      }
    }
    for (int z = -2; z <= 2; z++)
    {
      Block block = beacon.getWorld().getBlockAt(beacon.getLocation().add(3.0D, -1.0D, z));
      if (block.getType() == Material.WOOD_PLATE) {
        block.setType(Material.AIR);
      }
      block = beacon.getWorld().getBlockAt(beacon.getLocation().add(-3.0D, -1.0D, z));
      if (block.getType() == Material.WOOD_PLATE) {
        block.setType(Material.AIR);
      }
    }
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.BEACON_DROPS))
    {
      sender.sendMessage(this.config.getMessage("disabled"));
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("setdropbeacon")) {
      return setBeaconCmd(sender);
    }
    return false;
  }
  
  private boolean setBeaconCmd(CommandSender sender)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command");
      return true;
    }
    Player ply = (Player)sender;
    if (!this.beaconSetters.contains(ply.getName()))
    {
      this.beaconSetters.add(ply.getName());
      ply.sendMessage(this.config.getMessage("set-prompt"));
      return true;
    }
    return false;
  }
}