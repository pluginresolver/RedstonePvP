package dev.LollipopNinga.RedstonePvP.goldtrade;

import dev.LollipopNinga.RedstonePvP.RedstoneFeature;
import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.PaymentManager;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GoldTrade
  implements Listener, CommandExecutor
{
  private Set<String> signSetters;
  private GoldTradeConfig config;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public GoldTrade(GoldTradeConfig config)
  {
    this.signSetters = new HashSet();
    this.config = config;
  }
  
  @SuppressWarnings("deprecation")
@EventHandler
  public void onTradeSignClick(PlayerInteractEvent event)
  {
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.GOLD_TRADE)) {
      return;
    }
    Player ply = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getClickedBlock() == null) {
      return;
    }
    if (!(event.getClickedBlock().getState() instanceof Sign))
    {
      if (this.signSetters.contains(ply.getName()))
      {
        ply.sendMessage(this.config.getMessage("not-sign"));
        this.signSetters.remove(ply.getName());
      }
      return;
    }
    Sign sign = (Sign)event.getClickedBlock().getState();
    if (this.signSetters.contains(ply.getName()))
    {
      event.setCancelled(true);
      if (!this.config.isTradeSign(sign))
      {
        this.config.storeSign(sign);
        ply.sendMessage(this.config.getMessage("sign-set"));
      }
      else
      {
        ply.sendMessage(this.config.getMessage("already-set"));
      }
      this.signSetters.remove(ply.getName());
      return;
    }
    if (this.config.isTradeSign(sign))
    {
      event.setCancelled(true);
      int itemId = this.config.getItemId();
      if (PaymentManager.pay(ply, Material.getMaterial(itemId), 1))
      {
        ply.giveExp(this.config.getRandomXp());
        return;
      }
      ply.sendMessage(this.config.getMessage("insufficient-funds"));
    }
  }
  
  @EventHandler
  public void onTradeSignBreak(BlockBreakEvent event)
  {
    if (!(event.getBlock().getState() instanceof Sign)) {
      return;
    }
    Sign sign = (Sign)event.getBlock().getState();
    if (this.config.isTradeSign(sign)) {
      this.config.removeSign(sign);
    }
  }
  
  @EventHandler
  public void onPlayerLogout(PlayerQuitEvent event)
  {
    String name = event.getPlayer().getName();
    if (this.signSetters.contains(name)) {
      this.signSetters.remove(name);
    }
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (!RedstonePvP.config.isFeatureEnabled(RedstoneFeature.GOLD_TRADE))
    {
      sender.sendMessage(this.config.getMessage("disabled"));
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("settradesign")) {
      return setSignCmd(sender);
    }
    return false;
  }
  
  private boolean setSignCmd(CommandSender sender)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command");
      return true;
    }
    Player ply = (Player)sender;
    if (!this.signSetters.contains(ply.getName()))
    {
      this.signSetters.add(ply.getName());
      ply.sendMessage(this.config.getMessage("set-prompt"));
      return true;
    }
    return false;
  }
}
