package dev.LollipopNinga.RedstonePvP.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PaymentManager
{
  @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public static boolean pay(Player ply, Material type, int amount)
  {
    PlayerInventory inv = ply.getInventory();
    
    int available = 0;
    Map<Integer, ItemStack> items = new HashMap();
    for (int i = 0; i < inv.getSize(); i++)
    {
      ItemStack stack = inv.getItem(i);
      if ((stack != null) && (stack.getType() == type))
      {
        items.put(Integer.valueOf(i), stack);
        available += stack.getAmount();
      }
    }
    if (available < amount) {
      return false;
    }
    int unpaidAmount = amount;
    for (Map.Entry<Integer, ItemStack> stackEntry : items.entrySet())
    {
      int invSlot = ((Integer)stackEntry.getKey()).intValue();
      ItemStack stack = (ItemStack)stackEntry.getValue();
      if (stack.getAmount() < unpaidAmount)
      {
        unpaidAmount -= stack.getAmount();
        inv.setItem(invSlot, null);
      }
      else
      {
        if (stack.getAmount() == unpaidAmount)
        {
          inv.setItem(invSlot, null);
          break;
        }
        stack.setAmount(stack.getAmount() - unpaidAmount);
        break;
      }
    }
    ply.updateInventory();
    return true;
  }
}