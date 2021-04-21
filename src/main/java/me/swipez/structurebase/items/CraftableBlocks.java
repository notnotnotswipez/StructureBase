package me.swipez.structurebase.items;

import me.swipez.structurebase.StructureBase;
import me.swipez.structurebase.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftableBlocks {

    public static ItemStack WOOD = ItemBuilder.of(Material.OAK_PLANKS)
            .name(ChatColor.GOLD+"Village")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack OBSIDIAN = ItemBuilder.of(Material.OBSIDIAN)
            .name(ChatColor.GOLD+"Ruined Portal")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack END_FRAME = ItemBuilder.of(Material.END_PORTAL_FRAME)
            .name(ChatColor.GOLD+"Stronghold")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack SAND = ItemBuilder.of(Material.SAND)
            .name(ChatColor.GOLD+"Pyramid")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack LOG = ItemBuilder.of(Material.OAK_LOG)
            .name(ChatColor.GOLD+"Sunken Ship")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static ItemStack SOUL_SAND = ItemBuilder.of(Material.SOUL_SAND)
            .name(ChatColor.GOLD+"Bastion")
            .enchantment(Enchantment.CHANNELING, 1)
            .flags(ItemFlag.HIDE_ENCHANTS)
            .build();

    public static void initRecipes(StructureBase plugin){
        registerWoodRecipe(plugin);
        registerObsidianRecipe(plugin);
        registerEndFrame(plugin);
        registerSandRecipe(plugin);
        registerLogRecipe(plugin);
        registerSoulSandRecipe(plugin);
    }

    public static void registerWoodRecipe(StructureBase plugin){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "wood_block_recipe"), CraftableBlocks.WOOD)
                .shape("SSS","SCS","LLL")
                .setIngredient('S', Material.COBBLESTONE_STAIRS)
                .setIngredient('C', Material.CHEST)
                .setIngredient('L', Material.OAK_LOG);
        Bukkit.addRecipe(shapedRecipe);
    }

    public static void registerObsidianRecipe(StructureBase plugin){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "obsidian_block_recipe"), CraftableBlocks.OBSIDIAN)
                .shape(" S "," C "," L ")
                .setIngredient('S', Material.LAVA_BUCKET)
                .setIngredient('C', Material.CHEST)
                .setIngredient('L', Material.WATER_BUCKET);
        Bukkit.addRecipe(shapedRecipe);
    }

    public static void registerEndFrame(StructureBase plugin){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "endframe_block_recipe"), CraftableBlocks.END_FRAME)
                .shape("SSS","SCS","SSS")
                .setIngredient('S', Material.ENDER_EYE)
                .setIngredient('C', Material.OBSIDIAN);
        Bukkit.addRecipe(shapedRecipe);
    }

    public static void registerSandRecipe(StructureBase plugin){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "sand_block_recipe"), CraftableBlocks.SAND)
                .shape("SSS","SCS","SSS")
                .setIngredient('S', Material.SAND)
                .setIngredient('C', Material.CHEST);
        Bukkit.addRecipe(shapedRecipe);
    }

    public static void registerLogRecipe(StructureBase plugin){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "log_block_recipe"), CraftableBlocks.LOG)
                .shape(" S ","SSS"," S ")
                .setIngredient('S', Material.OAK_BOAT);
        Bukkit.addRecipe(shapedRecipe);
    }

    public static void registerSoulSandRecipe(StructureBase plugin){
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "soul_sand_block_recipe"), CraftableBlocks.SOUL_SAND)
                .shape("SSS","GGG","SSS")
                .setIngredient('S', Material.BASALT)
                .setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(shapedRecipe);
    }
}
