package dev.LollipopNinga.RedstonePvP.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationFormatter
{
  private LocationFormatter()
  {
    throw new UnsupportedOperationException("Cannot instantiate this class");
  }
  
  public static String formatLocation(Location loc)
  {
    return loc.getWorld() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
  }
  
  public static Location parseLocation(String loc)
  {
    String[] coords = loc.split(", ");
    int x = 0;
    int y = 0;
    int z = 0;
    
    if (coords.length != 4) {
      throw new IllegalArgumentException("The string is not correctly formatted as a Location");
    }
    World world = Bukkit.getWorld(coords[0]);
    if (world == null) {
      throw new IllegalArgumentException("The world does not exist");
    }
    try
    {
      x = Integer.parseInt(coords[1]);
      y = Integer.parseInt(coords[2]);
      z = Integer.parseInt(coords[3]);
    }
    catch (NumberFormatException e)
    {
      throw new IllegalArgumentException("Coordinates cannot be parsed");
    }

    return new Location(world, x, y, z);
  }
}