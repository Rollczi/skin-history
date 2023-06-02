package dev.rollczi.liteskinhistory.bukkit;

import dev.rollczi.liteskinhistory.bukkit.legacy.LegacyColorProcessor;
import dev.rollczi.liteskinhistory.config.ConfigFile;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Exclude;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.Instant;
import java.util.List;

@ConfigFile(fileName = "config.yml")
class LiteSkinHistoryConfig implements LiteSkinHistoryGui.Config {

    @Exclude
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new LegacyColorProcessor())
            .build();

    String title = "&aSkin History";
    String skullName = "{skin}";
    List<String> skullLore = List.of("&7Changed at: &f{changedAt}");
    String skullSelfName = "&a{skin}";
    List<String> skullSelfLore = List.of("&7Clear your skin!");

    ItemConfig fillItem = new ItemConfig();
    ItemConfig previousPageItem = new ItemConfig(Material.ARROW, "&aPrevious page", List.of());
    ItemConfig nextPageItem = new ItemConfig(Material.ARROW, "&aNext page", List.of());

    @Override
    public Component title() {
        return MINI_MESSAGE.deserialize(title);
    }

    @Override
    public Component skull(String skin, Instant changedAt) {
        return MINI_MESSAGE.deserialize(replace(skullName, skin, changedAt));
    }

    @Override
    public List<Component> skullLore(String skin, Instant changedAt) {
        return skullLore.stream()
            .map(lore -> MINI_MESSAGE.deserialize(replace(lore, skin, changedAt)))
            .toList();
    }

    @Override
    public GuiItem fillItem() {
        return fillItem.create();
    }

    @Override
    public GuiItem previousPageItem(GuiAction<InventoryClickEvent> action) {
        return previousPageItem.create(action);
    }

    @Override
    public GuiItem nextPageItem(GuiAction<InventoryClickEvent> action) {
        return nextPageItem.create(action);
    }

    @Override
    public Component skullSelfName(String skin) {
        return MINI_MESSAGE.deserialize(skullSelfName.replace("{skin}", skin));
    }

    @Override
    public List<Component> skullSelfLore(String skin) {
        return skullSelfLore.stream()
            .map(lore -> MINI_MESSAGE.deserialize(lore.replace("{skin}", skin)))
            .toList();
    }

    private String replace(String text, String skin, Instant changedAt) {
        return text
            .replace("{skin}", skin)
            .replace("{changedAt}", changedAt.toString());
    }

    @Contextual
    static class ItemConfig {
        Material material = Material.GRAY_STAINED_GLASS_PANE;
        String name = "";
        List<String> lore = List.of();

        public ItemConfig() {
        }

        public ItemConfig(Material material, String name, List<String> lore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
        }

        @Exclude
        public GuiItem create() {
            return this.create(event -> {});
        }

        @Exclude
        public GuiItem create(GuiAction<InventoryClickEvent> action) {
            return ItemBuilder.from(material)
                .name(MINI_MESSAGE.deserialize(name))
                .lore(lore.stream().map(MINI_MESSAGE::deserialize).toList())
                .asGuiItem(action);
        }
    }

}
