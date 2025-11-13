package be.sasha.pluginsasha.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;


import java.lang.reflect.Field;
import java.net.URL;
import java.util.UUID;


public class MenuFactory {

    public static Inventory createMenuPrincipal(Player player) {
        String valeurDiscord="eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzM5ZWU3MTU0OTc5YjNmODc3MzVhMWM4YWMwODc4MTRiNzkyOGQwNTc2YTI2OTViYTAxZWQ2MTYzMTk0MjA0NSJ9fX0=";
        return new InventoryBuilder(new MenuHolder("menuPrincipal"), 45, "§7§lMenu Principal")
                .addItem(-1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("").build())
                .addItem(21, new ItemBuilder(Material.GRASS_BLOCK).setName("§a§lMonde survie").setLore("§8Clique pour te téléporter").build())
                .addItem(36, new ItemBuilder(Material.PLAYER_HEAD).setCustomHeadTexture(valeurDiscord).setName("§l§9Notre discord").setLore("§8Rejoins nous!").build())
                .addItem(40, new ItemBuilder(Material.ENDER_PEARL).setName("§7§lSpawn").setLore("§8Clique pour te téléporter").build())
                .addItem(44, getPlayerHead(player))

                .build();
    }

    public static Inventory createMenu2() {
        return new InventoryBuilder(new MenuHolder("menu2"), 9, "Menu 2")
                .addItem(2, new ItemBuilder(Material.DIAMOND).setName("Option Diamant").build())
                .addItem(4, new ItemBuilder(Material.GOLD_INGOT).setName("Option Or").build())
                .addItem(6, new ItemBuilder(Material.EMERALD).setName("Option Émeraude").build())
                .build();
    }

    public static Inventory createOptionOr() {
        return new InventoryBuilder(new MenuHolder("optionOr"), 9, "Option Or")
                .addItem(4, new ItemBuilder(Material.GOLD_INGOT).setName("Option Or").build())
                .build();
    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            Component name = Component.text(player.getName())
                    .color(NamedTextColor.DARK_PURPLE)
                    .decorate(TextDecoration.BOLD);
            meta.displayName(name);
            head.setItemMeta(meta);
        }
        return head;
    }

    public static ItemStack getCustomHead(String nom, String base64) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(nom);
        ProfileProperty property = new ProfileProperty("textures", base64);
        profile.setProperty(property);

        meta.setPlayerProfile(profile);

        // ✅ Ajout du nom stylisé
        Component displayName = Component.text(nom)
                .color(NamedTextColor.AQUA)
                .decorate(TextDecoration.BOLD);
        meta.displayName(displayName);

        head.setItemMeta(meta);
        return head;
    }






}