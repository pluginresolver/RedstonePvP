package dev.LollipopNinga.RedstonePvP.beacondrops;

import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.Config;
import dev.LollipopNinga.RedstonePvP.util.LocationFormatter;
import org.bukkit.Location;
import org.bukkit.block.Beacon;

public class BeaconDropsConfig
  extends Config
{
  public BeaconDropsConfig(RedstonePvP plugin, String name)
  {
    super(plugin, name);
  }
  
  public void restoreDefaults()
  {
    this.config.options().header("Set the options for the beacon drop party.\nwait-time is how long in minutes you have to wait between each drop party before reactivating the beacon.\nitem-id is the itemID of the item that will be dropped\nphase-one is how long phase one lasts in seconds\nphase-two is how long phase two lasts in seconds\nrepeat is how many times it repeats\nplayer-amount is how many players there has to be on the server for a drop party to be run");
    
    this.config.set("wait-time", Integer.valueOf(30));
    this.config.set("item-id", Integer.valueOf(266));
    this.config.set("phase-one", Integer.valueOf(5));
    this.config.set("phase-two", Integer.valueOf(5));
    this.config.set("repeat", Integer.valueOf(3));
    this.config.set("player-amount", Integer.valueOf(25));
    this.config.set("Beacon", "null");
    
    setMessage("not-beacon", "&cThis is not a beacon");
    setMessage("beacon-set", "&aBeacon successfully set");
    setMessage("already-set", "&cThis is already the drop beacon");
    setMessage("wait", "&eA beacon drop party can only run once every 30 minutes.\nYou will be notified in the chat when the beacon is ready");
    setMessage("not-enough-players", "&cThere has to be at least &625 &cplayers online to start a drop party");
    setMessage("charged", "&eThe beacon is charged!");
    setMessage("disabled", "&cBeaconDrops is not enabled");
    setMessage("set-prompt", "&aRight click a beacon to set it as the drop beacon");
    
    saveConfig();
  }
  
  public int getWaitTime()
  {
    return this.config.getInt("wait-time", 30);
  }
  
  public int getItemId()
  {
    return this.config.getInt("item-id", 266);
  }
  
  public int getPhaseOne()
  {
    return this.config.getInt("phase-one", 5);
  }
  
  public int getPhaseTwo()
  {
    return this.config.getInt("phase-two", 5);
  }
  
  public int getRepeat()
  {
    return this.config.getInt("repeat", 3);
  }
  
  public void setBeacon(Beacon beacon)
  {
    Location loc = beacon.getLocation();
    String format = LocationFormatter.formatLocation(loc);
    
    this.config.set("Beacon", format);
    saveConfig();
  }
  
  public void removeBeacon()
  {
    this.config.set("Beacon", "null");
    saveConfig();
  }
  
  public boolean isDropBeacon(Beacon beacon)
  {
    Location loc = beacon.getLocation();
    String format = LocationFormatter.formatLocation(loc);
    if (format.equalsIgnoreCase(this.config.getString("Beacon"))) {
      return true;
    }
    return false;
  }
  
  public int getPlayerAmount()
  {
    return this.config.getInt("player-amount", 25);
  }
}