package me.swipez.structurebase.structure;

import me.swipez.structurebase.StructureBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.TileState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StructureUtil {

    public static StructurePlacement placeLayer(FileConfiguration structureFile, final Location placementOrigin, int layer) {
        ArrayList<String> coordKeys = new ArrayList<>();
        for (String key : structureFile.getKeys(true)) {
            if (!key.contains(".")) continue;
            String yPart = key.substring(0, key.indexOf('.'));
            int yOffset = Integer.parseInt(yPart.replace("y=", ""));
            if (yOffset == layer) {
                coordKeys.add(key);
            }
        }
        return placeStructure(structureFile, coordKeys, placementOrigin);
    }

    public static StructurePlacement placeStructure(FileConfiguration structureFile, final Location placementOrigin) {
        ArrayList<String> coordKeys = new ArrayList<>();
        for (String key : structureFile.getKeys(true)) {
            if (key.contains(".")) {
                coordKeys.add(key);
            }
        }
        return placeStructure(structureFile, coordKeys, placementOrigin);
    }

    private static StructurePlacement placeStructure(FileConfiguration structureFile, List<String> serializedCoordinates, final Location placementOrigin) {
        ArrayList<TileState> tileEntities = new ArrayList<>();
        for (String key : serializedCoordinates) {
            String yPart = key.substring(0, key.indexOf('.'));
            int yOffset = Integer.parseInt(yPart.replace("y=", ""));
            String xPart = key.substring(key.indexOf('.') + 1);
            int xOffset = Integer.parseInt(xPart.replace("x=", ""));

            for (String zPart : structureFile.getStringList(key)) {
                int zOffset = Integer.parseInt(zPart.substring(0, zPart.indexOf(';')));
                String blockDataStr = zPart.substring(zPart.indexOf(';') + 1);
                Location placement = placementOrigin.clone();
                placement = placement.add(- xOffset, yOffset, -zOffset);
                placement.getBlock().setBlockData(Bukkit.getServer().createBlockData(blockDataStr), false);
                if (placement.getBlock().getState() instanceof TileState) {
                    tileEntities.add((TileState) placement.getBlock().getState());
                }
                if (placement.getBlock().getType() == Material.YELLOW_CONCRETE) {
                    placement.getBlock().setType(Material.AIR);
                }
            }
        }
        return new StructurePlacement((ArrayList<TileState>) tileEntities.clone());
    }

    public static void loadAndSlowlyPlaceCaio(String name, Location placementOrigin, int maxY,
                                              EntityType spawnersEntity, LootTable chestsLootTable, StructureBase plugin) {
        File file = new File(plugin.getDataFolder(), name + ".yml");
        FileConfiguration fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            return;
        }
        for (int y = 0; y <= maxY; y++) {
            final int layer = y;
            new BukkitRunnable() {
                @Override
                public void run() {
                  List<TileState> tileEntities = placeLayer(fileConfiguration, placementOrigin, layer).getTileEntities();
                  for (BlockState state : tileEntities) {
                      if (chestsLootTable != null && state.getBlock().getType() == Material.CHEST) {
                          setLootTable((Chest) state, chestsLootTable);
                      }
                      if (spawnersEntity != null && state.getBlock().getType() == Material.SPAWNER) {
                          setSpawnerType((CreatureSpawner) state, spawnersEntity);
                      }
                  }
                }
            }.runTaskLater(plugin, 5L * y);
        }
    }

    public static void setLootTable(Chest chest, LootTable lootTable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                chest.setLootTable(lootTable);
                chest.update(true);
            }
        }.runTaskLater(StructureBase.plugin, 1);
    }

    public static void setSpawnerType(CreatureSpawner spawner, EntityType entityType) {
        spawner.setMaxSpawnDelay(300);
        spawner.setMaxNearbyEntities(6);
        spawner.setSpawnedType(entityType);
        spawner.update();
    }

    // Not needed
    // Here for archival purposes
    /*
    public static void loadAndSlowlyPlace(String name, Block playerBlock, AdvancementsSpawnStructures plugin,
                                          Vector placementOffset, EntityType entityType, int loopSize) {
        File file = new File(plugin.getDataFolder(), name + ".yml");
        FileConfiguration fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            return;
        }
        for (int y = 0; y < loopSize; y++) {
            int finalY = y;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String key : fileConfiguration.getKeys(true)) {
                        if (!key.contains(".")) continue; // We only need nested keys

                        String yPart = key.substring(0, key.indexOf('.'));
                        int yOffset = Integer.parseInt(yPart.replace("y=", ""));
                        if (yOffset != finalY) continue;
                        String xPart = key.substring(key.indexOf('.') + 1);
                        int xOffset = Integer.parseInt(xPart.replace("x=", ""));

                        for (String zPart : fileConfiguration.getStringList(key)) {
                            int zOffset = Integer.parseInt(zPart.substring(0, zPart.indexOf(';')));
                            String blockDataStr = zPart.substring(zPart.indexOf(';') + 1);
                            Location location = playerBlock.getLocation().subtract(
                                    placementOffset.getBlockX(), placementOffset.getBlockY(), placementOffset.getBlockZ());
                            location.setX(location.getBlockX() - xOffset);
                            location.setY(location.getBlockY() + yOffset);
                            location.setZ(location.getBlockZ() - zOffset);

                            Block block = playerBlock.getWorld().getBlockAt(location);
                            block.setBlockData(Bukkit.getServer().createBlockData(blockDataStr), false);
                            if (block.getType().equals(Material.YELLOW_CONCRETE)) {
                                block.setType(Material.AIR);
                            }
                            if (block.getType().equals(Material.SPAWNER)) {
                                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                                creatureSpawner.setMaxSpawnDelay(300);
                                creatureSpawner.setMaxNearbyEntities(6);
                                creatureSpawner.setSpawnedType(entityType);
                                creatureSpawner.update();
                            }
                        }
                    }
                    playerBlock.setType(Material.AIR);
                }
            }.runTaskLater(plugin, 5L * y);
        }
    }
    */
}