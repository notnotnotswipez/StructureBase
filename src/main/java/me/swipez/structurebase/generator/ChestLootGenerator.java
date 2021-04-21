package me.swipez.structurebase.generator;

import me.swipez.structurebase.StructureBase;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class ChestLootGenerator {

    public static void setChestInventory(Inventory inventory, StructureBase plugin, String name, Double airChance){
        Random random = new Random();
        for (int i = 0; i < inventory.getSize(); i++){
            double isAir = random.nextDouble();
            if (isAir < airChance){
                RandomLootGenerator randomLootGenerator = new RandomLootGenerator();
                List<String> enchants = plugin.getConfig().getStringList("enchants_list");
                List<String> peffects = plugin.getConfig().getStringList("potion_effects");

                ItemStack ritem;
                switch (name){
                    case "pyramid":
                        ritem = randomLootGenerator.getPyramidItem();
                        break;
                    case "bastion":
                        ritem = randomLootGenerator.getBastionItem();
                        break;
                    case "fortress":
                        ritem = randomLootGenerator.getFortressItem();
                        break;
                    case "pillager_outpost":
                        ritem = randomLootGenerator.getOutPostItem();
                        break;
                    case "stronghold":
                        ritem = randomLootGenerator.getStrongHoldItem();
                        break;
                    case "end_city":
                        ritem = randomLootGenerator.getEndCityItem();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + name);
                }
                if (ritem.getType() == Material.ENCHANTED_BOOK) {
                    ItemMeta meta = ritem.getItemMeta();
                    EnchantmentStorageMeta emeta = (EnchantmentStorageMeta) meta;
                    int mine = 0;
                    int maxe = enchants.size() - 1;
                    double rench = Math.random() * (maxe - mine + 1) + mine;
                    emeta.addStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchants.get((int) rench).toLowerCase())), 10, true);
                    ritem.setItemMeta(emeta);
                }
                if (ritem.getType() == Material.POTION) {
                    ItemMeta meta = ritem.getItemMeta();
                    PotionMeta pmeta = (PotionMeta) meta;
                    int mine = 0;
                    int maxe = peffects.size() - 1;
                    double rench = Math.random() * (maxe - mine + 1) + mine;
                    pmeta.addCustomEffect(new PotionEffect((PotionEffectType.getByName(peffects.get((int) rench).toUpperCase())), 1200, 2), true);
                    pmeta.setColor(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    pmeta.setDisplayName(ChatColor.WHITE+"OP Potion");
                    ritem.setItemMeta(pmeta);
                }
                inventory.setItem(i, ritem);
            }
        }
    }
}
