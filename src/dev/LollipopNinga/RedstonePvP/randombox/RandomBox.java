package dev.LollipopNinga.RedstonePvP.randombox;

import dev.LollipopNinga.RedstonePvP.RedstoneFeature;
import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.PaymentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RandomBox
  implements Listener
{
  private Map<Player, Block> confirming;
  List<Block> inUse;
  private RedstonePvP plugin;
  private RandomBoxConfig config;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public RandomBox(RedstonePvP plugin, RandomBoxConfig config)
  {
    this.plugin = plugin;
    this.inUse = new ArrayList();
    this.confirming = new HashMap();
    this.config = config;
  }
  
  RandomBoxConfig getConfig()
  {
    return this.config;
  }
  
  @SuppressWarnings("deprecation")
@EventHandler
  public void onRandomBoxClick(PlayerInteractEvent event)
  {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getClickedBlock() == null) {
      return;
    }
    if (event.getClickedBlock().getType() != Material.PISTON_BASE) {
      return;
    }
    Block block = event.getClickedBlock();
    final Player ply = event.getPlayer();
    
    event.setCancelled(true);
    if (!ply.getWorld().getName().equalsIgnoreCase(this.config.getWorld())) {
      return;
    }
    if (ply.getGameMode() != GameMode.SURVIVAL)
    {
      ply.sendMessage(this.config.getMessage("illegal-gamemode"));
      return;
    }
    if (this.inUse.contains(block))
    {
      ply.sendMessage(this.config.getMessage("already-in-use"));
      return;
    }
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.RANDOM_BOX))
    {
      ply.sendMessage(this.config.getMessage("disabled"));
      return;
    }
    if (this.config.isUsingConfirm())
    {
      if (this.confirming.containsKey(ply)) {
        return;
      }
      this.confirming.put(ply, block);
      ply.sendMessage(this.config.getMessage("confirm"));
      new BukkitRunnable()
      {
        public void run()
        {
          if (RandomBox.this.confirming.containsKey(ply))
          {
            RandomBox.this.confirming.remove(ply);
            ply.sendMessage(RandomBox.this.config.getMessage("timeout"));
          }
        }
      }.runTaskLater(this.plugin, 20L * this.config.getConfirmationTimeout());
    }
    else
    {
      if (!PaymentManager.pay(ply, Material.getMaterial(this.config.getCurrency()), this.config.getPrice()))
      {
        ply.sendMessage(this.config.getMessage("insufficient-funds"));
        return;
      }
      startRandomItemGeneration(ply, block);
    }
  }
  
  @SuppressWarnings("deprecation")
@EventHandler(ignoreCancelled=true)
  public void onConfirm(AsyncPlayerChatEvent event)
  {
    if (!event.getMessage().equalsIgnoreCase("y")) {
      return;
    }
    if (!this.confirming.containsKey(event.getPlayer())) {
      return;
    }
    for (Map.Entry<Player, Block> confEntry : this.confirming.entrySet())
    {
      Player ply = (Player)confEntry.getKey();
      Block block = (Block)confEntry.getValue();
      if (block == this.confirming.get(event.getPlayer()))
      {
        if (ply == event.getPlayer())
        {
          if (!PaymentManager.pay(ply, Material.getMaterial(this.config.getCurrency()), this.config.getPrice())) {
            ply.sendMessage(this.config.getMessage("insufficient-funds"));
          } else {
            startRandomItemGeneration(event.getPlayer(), block);
          }
        }
        else {
          ply.sendMessage(this.config.getMessage("ninjaed"));
        }
        this.confirming.remove(ply);
      }
    }
    event.setCancelled(true);
  }
  
  private void startRandomItemGeneration(Player ply, Block block)
  {
    ply.sendMessage(this.config.getMessage("accepted"));
    this.inUse.add(block);
    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new RandomBoxTask(this.plugin, ply, block, this), 60L);
  }
}