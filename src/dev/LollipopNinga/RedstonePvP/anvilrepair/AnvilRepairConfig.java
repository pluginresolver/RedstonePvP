package dev.LollipopNinga.RedstonePvP.anvilrepair;

import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class AnvilRepairConfig
  extends Config
{
  public AnvilRepairConfig(RedstonePvP plugin, String name)
  {
    super(plugin, name);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public void restoreDefaults()
  {
    this.config.options().header("A list of all repairable items. All swords and armour is added by default (even chain). Add and remove at your leisure. Item ID\nCurrency is the Item ID if the item used as payment for repairing\nPrice is the amount of the currency it costs to repair");
    
    List<Integer> swords = Arrays.asList(new Integer[] { Integer.valueOf(268), Integer.valueOf(283), Integer.valueOf(272), Integer.valueOf(267), Integer.valueOf(276) });
    List<Integer> helmets = Arrays.asList(new Integer[] { Integer.valueOf(298), Integer.valueOf(302), Integer.valueOf(306), Integer.valueOf(310), Integer.valueOf(314) });
    List<Integer> chestplates = Arrays.asList(new Integer[] { Integer.valueOf(299), Integer.valueOf(303), Integer.valueOf(307), Integer.valueOf(311), Integer.valueOf(315) });
    List<Integer> leggings = Arrays.asList(new Integer[] { Integer.valueOf(300), Integer.valueOf(304), Integer.valueOf(308), Integer.valueOf(312), Integer.valueOf(316) });
    List<Integer> boots = Arrays.asList(new Integer[] { Integer.valueOf(301), Integer.valueOf(305), Integer.valueOf(309), Integer.valueOf(313), Integer.valueOf(317) });
    
    List<Integer> items = new ArrayList();
    items.addAll(swords);
    items.addAll(helmets);
    items.addAll(chestplates);
    items.addAll(leggings);
    items.addAll(boots);
    
    this.config.set("repairable", items);
    
    this.config.set("currency", Integer.valueOf(266));
    this.config.set("price", Integer.valueOf(10));
    
    setMessage("no-item", "&cThere is no item in your hand to repair");
    setMessage("not-repairable", "&cThis item is not repairable");
    setMessage("not-damaged", "&cThis item is not damaged");
    setMessage("insufficient-funds", "&cYou do not have enough &6Gold Ingots &cto repair");
    
    saveConfig();
  }
  
  public boolean isRepairable(ItemStack item)
  {
    @SuppressWarnings("deprecation")
	int itemId = item.getTypeId();
    if (this.config.getIntegerList("repairable").contains(Integer.valueOf(itemId))) {
      return true;
    }
    return false;
  }
  
  public int getCurrency()
  {
    return this.config.getInt("currency", 266);
  }
  
  public int getPrice()
  {
    return this.config.getInt("price", 10);
  }
}