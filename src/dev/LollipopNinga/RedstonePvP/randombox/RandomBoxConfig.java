package dev.LollipopNinga.RedstonePvP.randombox;

import dev.LollipopNinga.RedstonePvP.RedstonePvP;
import dev.LollipopNinga.RedstonePvP.util.Config;

import java.util.ArrayList;
import java.util.List;

public class RandomBoxConfig
  extends Config
{
  List<Integer> rewards;
  
  public RandomBoxConfig(RedstonePvP plugin, String name)
  {
    super(plugin, name);
    generateRewards();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public void generateRewards()
  {
    this.rewards = new ArrayList();
    for (int i = 0; i < getSwordChance(); i++) {
      this.rewards.add(Integer.valueOf(0));
    }
    for (int i = 0; i < getBowChance(); i++) {
      this.rewards.add(Integer.valueOf(1));
    }
    for (int i = 0; i < getArmourChance(); i++) {
      this.rewards.add(Integer.valueOf(2));
    }
    for (int i = 0; i < getPotionChance(); i++) {
      this.rewards.add(Integer.valueOf(3));
    }
    for (int i = 0; i < getGodAppleChance(); i++) {
      this.rewards.add(Integer.valueOf(4));
    }
  }
  
  public void restoreDefaults()
  {
    this.config.options().header("How you want RandomBox to work\nsword-chance, bow-chance, armour-chance, potion-chance and godapple-chance represent the chances of receiving respecitve item types. ATTENTION: If the percentages do not add up to 100, the feature will be disabled\nconfirmation-timeout is how long (in seconds) the RandomBox waits for the player's confirmation\ncurrency is the itemID of the item you want to use as currency\nprice is how much of the currency it costs to use the RandomBox\nworld is the world in which RandomBox should be effective\nuse-confirm defines whether players should confirm their purchase. If set to false, the RandomBox won't ask for confirmation.");
    
    this.config.set("sword-chance", Integer.valueOf(40));
    this.config.set("bow-chance", Integer.valueOf(30));
    this.config.set("armour-chance", Integer.valueOf(15));
    this.config.set("potion-chance", Integer.valueOf(10));
    this.config.set("godapple-chance", Integer.valueOf(5));
    this.config.set("confirmation-timeout", Integer.valueOf(10));
    this.config.set("currency", Integer.valueOf(266));
    this.config.set("price", Integer.valueOf(20));
    this.config.set("world", "world");
    this.config.set("use-confirm", Boolean.valueOf(true));
    
    setMessage("illegal-gamemode", "&cYou can only use RandomBox while in Survival");
    setMessage("already-in-use", "&cThis RandomBox is already in use");
    setMessage("disabled", "&cRandomBox is not enabled");
    setMessage("confirm", "&9Are you sure you want to pay &620 Gold Ingots &9for a random item? Type 'Y' to confirm");
    setMessage("timeout", "&cYour RandomBox request has timed out");
    setMessage("insufficient-funds", "&cYou do not have enough &6Gold Ingots &cto use RandomBox");
    setMessage("ninjaed", "&cYour RandomBox request is no longer valid as the RandomBox is in use by someone else");
    setMessage("accepted", "&aHave a random item!");
    
    saveConfig();
  }
  
  public int getSwordChance()
  {
    return this.config.getInt("sword-chance", 40);
  }
  
  public int getBowChance()
  {
    return this.config.getInt("bow-chance", 30);
  }
  
  public int getArmourChance()
  {
    return this.config.getInt("armour-chance", 20);
  }
  
  public int getPotionChance()
  {
    return this.config.getInt("potion-chance", 10);
  }
  
  public int getGodAppleChance()
  {
    return this.config.getInt("godapple-chance", 5);
  }
  
  public int getConfirmationTimeout()
  {
    return this.config.getInt("confirmation-timeout", 10);
  }
  
  public int getCurrency()
  {
    return this.config.getInt("currency", 266);
  }
  
  public int getPrice()
  {
    return this.config.getInt("price", 20);
  }
  
  public String getWorld()
  {
    return this.config.getString("world", "world");
  }
  
  public boolean isUsingConfirm()
  {
    return this.config.getBoolean("use-confirm", true);
  }
  
  public List<Integer> getItemIntList()
  {
    return this.rewards;
  }
}