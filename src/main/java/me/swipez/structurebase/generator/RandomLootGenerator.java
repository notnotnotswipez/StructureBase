package me.swipez.structurebase.generator;

import me.swipez.structurebase.StructureBase;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLootGenerator {

    Random random = new Random();

    private static List<String> pyramidItems = new ArrayList<>();
    private static List<String> bastionItems = new ArrayList<>();
    private static List<String> fortressItems = new ArrayList<>();
    private static List<String> outpostItems = new ArrayList<>();
    private static List<String> strongholdItems = new ArrayList<>();
    private static List<String> endCityItems = new ArrayList<>();

    public static void buildLists(){
        buildPyramidItems();
        buildBastionList();
        buildFortressList();
        buildOutPostList();
        buildStrongholdList();
        buildEndCityList();
    }

    private static void buildPyramidItems(){
        for (String string : StructureBase.plugin.getConfig().getConfigurationSection("pyramid").getKeys(false)){
            pyramidItems.add(string);
        }
    }

    private static void buildBastionList(){
        for (String string : StructureBase.plugin.getConfig().getConfigurationSection("bastion").getKeys(false)){
            bastionItems.add(string);
        }
    }

    private static void buildFortressList(){
        for (String string : StructureBase.plugin.getConfig().getConfigurationSection("fortress").getKeys(false)){
            fortressItems.add(string);
        }
    }

    private static void buildOutPostList(){
        for (String string : StructureBase.plugin.getConfig().getConfigurationSection("pillager_outpost").getKeys(false)){
            outpostItems.add(string);
        }
    }

    private static void buildStrongholdList(){
        for (String string : StructureBase.plugin.getConfig().getConfigurationSection("stronghold").getKeys(false)){
            strongholdItems.add(string);
        }
    }

    private static void buildEndCityList(){
        for (String string : StructureBase.plugin.getConfig().getConfigurationSection("end_city").getKeys(false)){
            endCityItems.add(string);
        }
    }

    public ItemStack getPyramidItem(){
        int index = random.nextInt(pyramidItems.size());
        ItemStack itemStack = new ItemStack(Material.valueOf(pyramidItems.get(index).toUpperCase()));
        itemStack.setAmount(StructureBase.plugin.getConfig().getInt("pyramid."+pyramidItems.get(index)+".count"));
        checkForApply(itemStack);
        return itemStack;
    }

    public ItemStack getBastionItem(){
        int index = random.nextInt(bastionItems.size());
        ItemStack itemStack = new ItemStack(Material.valueOf(bastionItems.get(index).toUpperCase()));
        itemStack.setAmount(StructureBase.plugin.getConfig().getInt("bastion."+bastionItems.get(index)+".count"));
        checkForApply(itemStack);
        return itemStack;
    }

    public ItemStack getFortressItem(){
        int index = random.nextInt(fortressItems.size());
        ItemStack itemStack = new ItemStack(Material.valueOf(fortressItems.get(index).toUpperCase()));
        itemStack.setAmount(StructureBase.plugin.getConfig().getInt("fortress."+fortressItems.get(index)+".count"));
        checkForApply(itemStack);
        return itemStack;
    }

    public ItemStack getOutPostItem(){
        int index = random.nextInt(outpostItems.size());
        ItemStack itemStack = new ItemStack(Material.valueOf(outpostItems.get(index).toUpperCase()));
        itemStack.setAmount(StructureBase.plugin.getConfig().getInt("pillager_outpost."+outpostItems.get(index)+".count"));
        checkForApply(itemStack);
        return itemStack;
    }

    public ItemStack getStrongHoldItem(){
        int index = random.nextInt(strongholdItems.size());
        ItemStack itemStack = new ItemStack(Material.valueOf(strongholdItems.get(index).toUpperCase()));
        itemStack.setAmount(StructureBase.plugin.getConfig().getInt("stronghold."+strongholdItems.get(index)+".count"));
        checkForApply(itemStack);
        return itemStack;
    }

    public ItemStack getEndCityItem(){
        int index = random.nextInt(endCityItems.size());
        ItemStack itemStack = new ItemStack(Material.valueOf(endCityItems.get(index).toUpperCase()));
        itemStack.setAmount(StructureBase.plugin.getConfig().getInt("end_city."+endCityItems.get(index)+".count"));
        checkForApply(itemStack);
        return itemStack;
    }

    private void checkForApply(ItemStack ritem){
        List<String> enchants = StructureBase.plugin.getConfig().getStringList("enchants_list");
        List<String> peffects = StructureBase.plugin.getConfig().getStringList("potion_effects");

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
    }
}
