package be.sasha.pluginsasha.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        if (meta != null) {
            meta.displayName(Component.text(name, NamedTextColor.GOLD));
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchants) {
        if (meta != null) {
            enchants.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
