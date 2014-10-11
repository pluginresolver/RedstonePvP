package dev.LollipopNinga.RedstonePvP;

public enum RedstoneFeature
{
  BLOOD_SPRAY("blood-spray", "bloodspray", "Enables blood spray from players."),  GOLD_TRADE("gold-trade", "goldtrade", "Enables players to trade gold ingots (default) for EXP through signs."),  ANVIL_REPAIR("anvil-repair", "anvilrepair", "Enables players to repair any repairable item on an anvil for a set price."),  BEACON_DROPS("beacon-drops", "beacondrops", "Enables a beacon to be set as a drop-beacon. Every now and then, players can click it and it will start a drop party."),  RANDOM_BOX("random-box", "randombox", "Enables players to click specific pistons to get random goods.");
  
  private String configKey;
  private String name;
  private String description;
  
  private RedstoneFeature(String configKey, String name, String description)
  {
    this.configKey = configKey;
    this.name = name;
    this.description = description;
  }
  
  public String getConfigKey()
  {
    return this.configKey;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDescription()
  {
    return this.description;
  }
}
