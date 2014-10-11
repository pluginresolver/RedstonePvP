package dev.LollipopNinga.RedstonePvP;

import dev.LollipopNinga.RedstonePvP.util.Config;

public class RedstoneConfig
  extends Config
{
  public RedstoneConfig(RedstonePvP plugin, String name)
  {
    super(plugin, name);
  }
  
  public void enableFeature(RedstoneFeature feature)
  {
    this.config.set(feature.getConfigKey(), Boolean.valueOf(true));
    saveConfig();
  }
  
  public void disableFeature(RedstoneFeature feature)
  {
    this.config.set(feature.getConfigKey(), Boolean.valueOf(false));
    saveConfig();
  }
  
  public boolean isFeatureEnabled(RedstoneFeature feature)
  {
    return this.config.getBoolean(feature.getConfigKey(), false);
  }
  
  public void restoreDefaults()
  {
    this.config.options().header("Which features should be enabled? (true or false)");
    for (RedstoneFeature feature : RedstoneFeature.values()) {
      enableFeature(feature);
    }
  }
}