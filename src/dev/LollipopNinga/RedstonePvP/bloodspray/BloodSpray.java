package dev.LollipopNinga.RedstonePvP.bloodspray;

import dev.LollipopNinga.RedstonePvP.RedstoneFeature;
import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BloodSpray
  implements Listener
{
  @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
  public void onDamage(EntityDamageByEntityEvent event)
  {
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.BLOOD_SPRAY)) {
      return;
    }
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    Entity ply = event.getEntity();
    Location loc = ply.getLocation();
    
    ply.getWorld().playEffect(loc, Effect.STEP_SOUND, 152);
  }
}
