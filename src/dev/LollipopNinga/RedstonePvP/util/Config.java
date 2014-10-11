package dev.LollipopNinga.RedstonePvP.util;

import dev.LollipopNinga.RedstonePvP.RedstonePvP;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Config
{
  protected FileConfiguration config;
  protected File path;
  protected Logger log;
  private ConfigurationSection msg;
  
  protected Config(RedstonePvP plugin, String name)
  {
    this.log = plugin.getLogger();
    
    this.path = new File(plugin.getDataFolder(), name);
    try
    {
      this.config = YamlConfiguration.loadConfiguration(this.path);
      this.msg = this.config.getConfigurationSection("Messages");
    }
    catch (IllegalArgumentException e)
    {
      createNewConfig();
    }
    if (this.config.getKeys(true).size() == 0) {
      createNewConfig();
    }
  }
  
  public void createNewConfig()
  {
    this.config = new YamlConfiguration();
    
    this.msg = this.config.createSection("Messages");
    
    restoreDefaults();
  }
  
  public final void saveConfig()
  {
    try
    {
      this.config.save(this.path);
    }
    catch (IOException e)
    {
      this.log.warning("Config couldn't be saved: " + this.path.getPath());
    }
  }
  
  protected final void setMessage(String key, String message)
  {
    this.msg.set(key, message);
  }
  
  public final String getMessage(String key)
  {
    boolean isArgLegal = false;
    if (this.msg == null)
    {
      this.log.severe("Messages not found for " + this.path.getName());
      return "";
    }
    for (String s : this.msg.getKeys(false)) {
      if (key.equals(s))
      {
        isArgLegal = true;
        break;
      }
    }
    if (!isArgLegal)
    {
      this.log.warning("Message key not found: " + key);
      return "";
    }
    return ChatColor.translateAlternateColorCodes('&', this.msg.getString(key, ""));
  }
  
  public abstract void restoreDefaults();
}
