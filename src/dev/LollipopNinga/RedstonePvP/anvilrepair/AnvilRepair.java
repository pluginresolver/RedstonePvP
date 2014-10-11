package dev.LollipopNinga.RedstonePvP.anvilrepair;

import dev.LollipopNinga.RedstonePvP.RedstoneFeature;
import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.PaymentManager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class AnvilRepair
  implements Listener
{
  private AnvilRepairConfig config;
  private RedstonePvP plugin;
  
  public AnvilRepair(RedstonePvP plugin, AnvilRepairConfig config)
  {
    this.config = config;
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onAnvilClick(PlayerInteractEvent event)
  {
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.ANVIL_REPAIR)) {
      return;
    }
    Player ply = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getClickedBlock().getType() != Material.ANVIL) {
      return;
    }
    event.setCancelled(true);
    ItemStack item = ply.getItemInHand();
    if ((item == null) || (item.getType() == Material.AIR))
    {
      ply.sendMessage(this.config.getMessage("no-item"));
      return;
    }
    if (!this.config.isRepairable(item))
    {
      ply.sendMessage(this.config.getMessage("not-repairable"));
      return;
    }
    if (item.getDurability() == 0)
    {
      ply.sendMessage(this.config.getMessage("not-damaged"));
      return;
    }
    runRepair(ply, event.getClickedBlock());
  }
  
  @SuppressWarnings("deprecation")
private void runRepair(Player ply, Block anvil)
  {
    if (!PaymentManager.pay(ply, Material.getMaterial(this.config.getCurrency()), this.config.getPrice()))
    {
      ply.sendMessage(this.config.getMessage("insufficient-funds"));
      return;
    }
    ItemStack item = ply.getItemInHand();
    item.setDurability((short)0);
    ply.setItemInHand(null);
    
    Item spawnedItem = ply.getWorld().dropItem(anvil.getLocation().add(0.5D, 1.5D, 0.5D), item);
    spawnedItem.setVelocity(new Vector(0, 0, 0));
    spawnedItem.setItemStack(item);
    spawnedItem.setPickupDelay(80);
    
    BukkitScheduler sched = this.plugin.getServer().getScheduler();
    sched.scheduleSyncDelayedTask(this.plugin, new RepairTask(ply, spawnedItem, this.plugin), 60L);
  }
}