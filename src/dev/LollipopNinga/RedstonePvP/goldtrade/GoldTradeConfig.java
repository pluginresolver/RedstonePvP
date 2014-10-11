package dev.LollipopNinga.RedstonePvP.goldtrade;

import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.Config;
import dev.LollipopNinga.RedstonePvP.util.LocationFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class GoldTradeConfig
  extends Config
{
  public GoldTradeConfig(RedstonePvP plugin, String name)
  {
    super(plugin, name);
  }
  
  @SuppressWarnings("rawtypes")
public void restoreDefaults()
  {
    this.config.options().header("Set EXP options and which item should be used for trading");
    
    this.config.set("min-xp", Integer.valueOf(300));
    this.config.set("max-xp", Integer.valueOf(1000));
    this.config.set("item-id", Integer.valueOf(266));
    this.config.set("Signs", new ArrayList());
    
    setMessage("not-sign", "&cThis is not a sign");
    setMessage("sign-set", "&aSign successfully set as a trade sign");
    setMessage("already-sign", "&cThis sign is already a trade sign");
    setMessage("insufficient-funds", "&cYou do not have any &6Gold Ingots &cto trade");
    setMessage("disabled", "&cGoldTrade is not enabled");
    setMessage("set-prompt", "&aRight click a sign to set it as a trade sign");
    
    saveConfig();
  }
  
  public int getMinXp()
  {
    return this.config.getInt("min-xp", 100);
  }
  
  public int getMaxXp()
  {
    return this.config.getInt("max-xp", 300);
  }
  
  public int getRandomXp()
  {
    int min = getMinXp();
    int max = getMaxXp();
    Random rnd = new Random();
    
    return rnd.nextInt(max - min) + min;
  }
  
  public int getItemId()
  {
    return this.config.getInt("item-id", 266);
  }
  
  public void storeSign(Sign sign)
  {
    Location loc = sign.getLocation();
    List<String> signs = this.config.getStringList("Signs");
    String format = LocationFormatter.formatLocation(loc);
    for (String signData : signs) {
      if (format.equalsIgnoreCase(signData)) {
        return;
      }
    }
    signs.add(format);
    this.config.set("Signs", signs);
    saveConfig();
  }
  
  public void removeSign(Sign sign)
  {
    Location loc = sign.getLocation();
    String format = LocationFormatter.formatLocation(loc);
    List<String> signs = this.config.getStringList("Signs");
    for (int i = 0; i < signs.size(); i++)
    {
      String signData = (String)signs.get(i);
      if (format.equalsIgnoreCase(signData))
      {
        signs.remove(signData);
        i--;
      }
    }
    this.config.set("Signs", signs);
    saveConfig();
  }
  
  public boolean isTradeSign(Sign sign)
  {
    Location loc = sign.getLocation();
    String format = LocationFormatter.formatLocation(loc);
    List<String> signs = this.config.getStringList("Signs");
    for (String signData : signs) {
      if (format.equalsIgnoreCase(signData)) {
        return true;
      }
    }
    return false;
  }
}