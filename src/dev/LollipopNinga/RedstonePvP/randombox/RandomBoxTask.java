package dev.LollipopNinga.RedstonePvP.randombox;

import dev.LollipopNinga.RedstonePvP.RedstoneFeature;
import dev.LollipopNinga.RedstonePvP.RedstonePvP;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

public class RandomBoxTask
  implements Runnable
{
  private RedstonePvP plugin;
  private Player ply;
  private RandomBoxConfig config;
  private RandomBox box;
  private int rollItemTask;
  private Item currentItem;
  private Block block;
  
  public RandomBoxTask(RedstonePvP plugin, Player ply, final Block box, RandomBox feature)
  {
    this.plugin = plugin;
    this.ply = ply;
    this.box = feature;
    this.config = feature.getConfig();
    this.block = box;
    
    this.rollItemTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    {
      public void run()
      {
        if (RandomBoxTask.this.currentItem != null)
        {
          RandomBoxTask.this.currentItem.remove();
          RandomBoxTask.this.currentItem = null;
        }
        Random rnd = new Random();
        ItemStack item;
        
        switch (rnd.nextInt(8))
        {
        case 0: 
        default: 
          item = new ItemStack(Material.DIAMOND_SWORD, 1);
          break;
        case 1: 
          item = new ItemStack(Material.BOW, 1);
          break;
        case 2: 
          item = new ItemStack(Material.DIAMOND_HELMET, 1);
          break;
        case 3: 
          item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
          break;
        case 4: 
          item = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
          break;
        case 5: 
          item = new ItemStack(Material.DIAMOND_BOOTS, 1);
          break;
        case 6: 
          Potion potion = new Potion(PotionType.values()[rnd.nextInt(PotionType.values().length)]);
          if (rnd.nextInt(2) == 1) {
            potion.setSplash(true);
          }
          item = potion.toItemStack(1);
          break;
        case 7: 
          item = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
        }
        RandomBoxTask.this.currentItem = box.getWorld().dropItem(box.getLocation().add(0.5D, 1.2D, 0.5D), item);
        RandomBoxTask.this.currentItem.setVelocity(new Vector(0, 0, 0));
        RandomBoxTask.this.currentItem.setPickupDelay(80);
        
        box.getWorld().playSound(box.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
      }
    }, 4L, 4L);
  }
  
  @SuppressWarnings("incomplete-switch")
public void run()
  {
    this.plugin.getServer().getScheduler().cancelTask(this.rollItemTask);
    this.currentItem.remove();
    this.currentItem = null;
    
    Random rnd = new Random();
    List<Integer> rewards = this.config.getItemIntList();
    if (rewards.size() != 100)
    {
      this.plugin.getLogger().severe("Percentages for RandomBox do not add up");
      RedstonePvP.config.disableFeature(RedstoneFeature.RANDOM_BOX); return;
    }
    final ItemStack item;
    switch (((Integer)rewards.get(rnd.nextInt(rewards.size()))).intValue())
    {
    case 0: 
    default: 
      item = new ItemStack(Material.DIAMOND_SWORD, 1);
      break;
    case 1: 
      item = new ItemStack(Material.BOW, 1);
      break;
    case 2:
      switch (rnd.nextInt(4))
      {
      case 0: 
      default: 
        item = new ItemStack(Material.DIAMOND_HELMET);
        break;
      case 1: 
        item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        break;
      case 2: 
        item = new ItemStack(Material.DIAMOND_LEGGINGS);
        break;
      case 3: 
        item = new ItemStack(Material.DIAMOND_BOOTS);
      }
      break;
    case 3: 
      Potion potion = new Potion(PotionType.values()[rnd.nextInt(PotionType.values().length)]);
      if (rnd.nextInt(2) == 1) {
        potion.setSplash(true);
      }
      if ((rnd.nextInt(2) == 1) && (!potion.getType().isInstant())) {
        potion.extend();
      }
      item = potion.toItemStack(1);
      break;
    case 4: 
      item = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
    }
    switch (item.getType())
    {
    case LAVA_BUCKET: 
    case MAP: 
    case POTATO: 
    case POTATO_ITEM: 
    case POTION: 
    case POWERED_MINECART: 
      List<Enchantment> enchs = Arrays.asList(Enchantment.values());
      int enchCount = rnd.nextInt(3);
      int i = 0;
      for (;;)
      {
        addEnchantment(item, enchs);i++;
        if (i > enchCount) {
          break;
        }
      }
    }
    this.currentItem = this.block.getWorld().dropItem(this.block.getLocation().add(0.5D, 1.2D, 0.5D), item);
    this.currentItem.setPickupDelay(80);
    this.currentItem.setVelocity(new Vector(0, 0, 0));
    
    this.block.getWorld().playSound(this.block.getLocation(), Sound.ORB_PICKUP, 1.0F, 2.0F);
    
    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
    {
      public void run()
      {
        RandomBoxTask.this.currentItem.remove();
        RandomBoxTask.this.currentItem = null;
        for (ItemStack stack : RandomBoxTask.this.ply.getInventory().addItem(new ItemStack[] { item }).values()) {
          RandomBoxTask.this.ply.getWorld().dropItem(RandomBoxTask.this.ply.getLocation(), stack);
        }
        RandomBoxTask.this.box.inUse.remove(RandomBoxTask.this.block);
      }
    }, 40L);
  }
  
  private void addEnchantment(ItemStack item, List<Enchantment> enchs)
  {
    Random rnd = new Random();
    int i = 0;
    do
    {
      i++;
      Enchantment ench = (Enchantment)enchs.get(rnd.nextInt(Enchantment.values().length));
      if (ench.canEnchantItem(item))
      {
        item.addEnchantment(ench, rnd.nextInt(ench.getMaxLevel()) + 1);
        return;
      }
    } while (i < 5000);
  }
}