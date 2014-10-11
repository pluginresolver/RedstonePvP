package dev.LollipopNinga.RedstonePvP.anvilrepair;

import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairTask
  implements Runnable
{
  private Player ply;
  private Item spawnedItem;
  private int animationTask;
  
  public RepairTask(Player ply, Item item, RedstonePvP plugin)
  {
    this.ply = ply;
    this.spawnedItem = item;
    
    this.animationTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    {
      public void run()
      {
        RepairTask.this.spawnedItem.getWorld().playEffect(RepairTask.this.spawnedItem.getLocation(), Effect.STEP_SOUND, 145);
        RepairTask.this.spawnedItem.getWorld().playSound(RepairTask.this.spawnedItem.getLocation(), Sound.ANVIL_LAND, 0.5F, (float)Math.random() * 0.9F + 1.2F);
      }
    }, 20L, 10L);
  }
  
  public void run()
  {
    Map<Integer, ItemStack> givenItems = this.ply.getInventory().addItem(new ItemStack[] { this.spawnedItem.getItemStack() });
    for (ItemStack stack : givenItems.values())
    {
      Item item = this.ply.getWorld().dropItem(this.ply.getLocation(), stack);
      item.setItemStack(stack);
    }
    this.spawnedItem.remove();
    Bukkit.getScheduler().cancelTask(this.animationTask);
  }
}