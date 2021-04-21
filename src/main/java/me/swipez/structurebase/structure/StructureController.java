package me.swipez.structurebase.structure;

import me.swipez.structurebase.StructureBase;
import me.swipez.structurebase.generator.ChestLootGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class StructureController {

    public static void loadStructure(String name, Block player, StructureBase plugin, int ylower, int xlower, int zlower, EntityType entityType, int loopSize, double airChance) {
        for (int y = 0; y < loopSize; y++) {
            int finalY = y;
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("StructureBase").getDataFolder().getPath()+"/structures", name + ".yml");
                    FileConfiguration fileConfiguration = new YamlConfiguration();
                    try {
                        fileConfiguration.load(file);
                    } catch (IOException | InvalidConfigurationException exception) {
                        exception.printStackTrace();
                    }
                    for (String key : fileConfiguration.getKeys(true)) {
                        if (!key.contains(".")) continue; // We only need nested keys

                        String yPart = key.substring(0, key.indexOf('.'));
                        int yOffset = Integer.parseInt(yPart.replace("y=", ""));
                        if (yOffset == finalY) {
                            String xPart = key.substring(key.indexOf('.') + 1);
                            int xOffset = Integer.parseInt(xPart.replace("x=", ""));

                            for (String zPart : fileConfiguration.getStringList(key)) {
                                int zOffset = Integer.parseInt(zPart.substring(0, zPart.indexOf(';')));
                                String blockDataStr = zPart.substring(zPart.indexOf(';') + 1);
                                Location location = player.getLocation().subtract(xlower, ylower, zlower);
                                location.setX(location.getBlockX() - xOffset);
                                location.setY(location.getBlockY() + yOffset);
                                location.setZ(location.getBlockZ() - zOffset);

                                Block block = player.getWorld().getBlockAt(location);
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
                                if (block.getType().equals(Material.CHEST)) {
                                    Chest chest = (Chest) block.getState();
                                    ChestLootGenerator.setChestInventory(chest.getInventory(), plugin, name, airChance);
                                }
                            }
                        }
                    }
                    player.setType(Material.AIR);
                }
            }.runTaskLater(plugin, 5L * y);
        }
    }
}
