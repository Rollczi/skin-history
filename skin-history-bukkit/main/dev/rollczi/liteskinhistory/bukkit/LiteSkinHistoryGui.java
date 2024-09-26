package dev.rollczi.liteskinhistory.bukkit;

import dev.rollczi.liteskinhistory.bukkit.scheduler.Scheduler;
import dev.rollczi.liteskinhistory.bukkit.skinsrestorer.SkinMessenger;
import dev.rollczi.liteskinhistory.history.HistoryRange;
import dev.rollczi.liteskinhistory.history.HistoryService;
import dev.rollczi.liteskinhistory.history.api.SkinHistoryRecord;
import dev.rollczi.liteskinhistory.history.api.SkinHistoryResponse;
import dev.rollczi.liteskullapi.SkullAPI;
import dev.rollczi.liteskullapi.SkullData;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class LiteSkinHistoryGui {

    private static final int MIN_ROW = 2;
    private static final int MAX_ROW = 8;
    private static final int MIN_COLUMN = 2;
    private static final int MAX_COLUMN = 5;

    private static final int FIRST_PAGE = 0;
    private static final int PAGE_SIZE = (MAX_ROW - 1) * (MAX_COLUMN - 1);

    private final HistoryService historyService;
    private final Scheduler scheduler;
    private final SkullAPI skullAPI;
    private final SkinMessenger skinMessenger;
    private final Config config;
    private final Server server;

    LiteSkinHistoryGui(HistoryService historyService, Scheduler scheduler, SkullAPI skullAPI, SkinMessenger skinMessenger, Config config, Server server) {
        this.historyService = historyService;
        this.scheduler = scheduler;
        this.skullAPI = skullAPI;
        this.skinMessenger = skinMessenger;
        this.config = config;
        this.server = server;
    }

    void open(Player player) {
        this.open(player, new HistoryRange(PAGE_SIZE, FIRST_PAGE));
    }

    private void open(Player player, HistoryRange range) {
        scheduler.async(() -> {
            SkinHistoryResponse history = this.historyService.findHistory(player.getName(), range);
            Gui gui = this.createGui(player, history, range);

            scheduler.sync(() -> gui.open(player));
        });
    }

    private Gui createGui(Player player, SkinHistoryResponse response, HistoryRange range) {
        Gui gui = Gui.gui()
            .type(GuiType.CHEST)
            .rows(6)
            .title(config.title())
            .disableAllInteractions()
            .create();

        // Fill gui with empty items
        IntStream.range(1, 7).forEach(column -> IntStream.range(1, 10).forEach(row -> {
            if ((row < MIN_ROW || row > MAX_ROW) || (column < MIN_COLUMN || column > MAX_COLUMN)) {
                gui.setItem(column, row, config.fillItem());
            }
        }));

        // Fill gui with skulls
        int row = MIN_ROW;
        int column = MIN_COLUMN;
        int counter = range.offset() + 1;

        for (SkinHistoryRecord record : response.getRecords()) {
            if (row > MAX_ROW) {
                row = MIN_ROW;
                column++;
            }

            if (column > MAX_COLUMN) {
                break;
            }

            gui.setItem(
                column,
                row,
                ItemBuilder.skull()
                    .texture(record.getSkinValue())
                    .name(config.skull(counter, record.getSkin(), record.getChangedAt()))
                    .lore(config.skullLore(counter, record.getSkin(), record.getChangedAt()))
                    .asGuiItem(event -> this.setSkin(player, record.getSkin()))
            );

            row++;
            counter++;
        }

        // Previous button
        if (range.offset() > 0) {
            gui.setItem(6, 3, config.previousPageItem(event -> this.open(player, range.previousRange())));
        }

        // Next button
        if (range.offset() + PAGE_SIZE < response.getTotal()) {
            gui.setItem(6, 7, config.nextPageItem(event -> this.open(player, range.nextRange())));
        }

        // Self skull
        SkullData selfSkullData = skullAPI.awaitSkullData(player.getName(), 10, TimeUnit.SECONDS);

        gui.setItem(6, 5, ItemBuilder.skull()
            .texture(selfSkullData.getValue())
            .name(config.skullSelfName(player.getName()))
            .lore(config.skullSelfLore(player.getName()))
            .asGuiItem(event -> this.setSkin(player, player.getName()))
        );

        // Close button
        gui.setItem(6, 9, config.closeItem(event -> {
            player.closeInventory();
        }));

        return gui;
    }

    private void setSkin(Player player, String skin) {
        skinMessenger.setSkin(player, skin);
    }

    interface Config {

        Component title();

        Component skull(int count, String skin, Instant changedAt);

        List<Component> skullLore(int count, String skin, Instant changedAt);

        GuiItem fillItem();

        GuiItem previousPageItem(GuiAction<InventoryClickEvent> action);

        GuiItem nextPageItem(GuiAction<InventoryClickEvent> action);

        GuiItem closeItem(GuiAction<InventoryClickEvent> action);

        Component skullSelfName(String skin);

        List<Component> skullSelfLore(String skin);

    }

}
