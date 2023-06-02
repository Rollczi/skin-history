package dev.rollczi.liteskinhistory.bukkit;

import dev.rollczi.liteskinhistory.bukkit.scheduler.Scheduler;
import dev.rollczi.liteskinhistory.bukkit.skin.SkinMessenger;
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

    LiteSkinHistoryGui(HistoryService historyService, Scheduler scheduler, SkullAPI skullAPI, SkinMessenger skinMessenger, Config config) {
        this.historyService = historyService;
        this.scheduler = scheduler;
        this.skullAPI = skullAPI;
        this.skinMessenger = skinMessenger;
        this.config = config;
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

        IntStream.range(0, 9).forEach(column -> IntStream.range(0, 6).forEach(row -> {
            if (row < MIN_ROW || row > MAX_ROW || column < MIN_COLUMN || column > MAX_COLUMN) {
                gui.setItem(row, column, config.fillItem());
            }
        }));

        int row = MIN_ROW;
        int column = MIN_COLUMN;

        for (SkinHistoryRecord record : response.getRecords()) {
            if (column > MAX_COLUMN) {
                column = MIN_COLUMN;
                row++;
            }

            if (row > MAX_ROW) {
                break;
            }

            SkullData skullData = skullAPI.awaitSkullData(record.getSkin(), 10, TimeUnit.SECONDS);

            gui.setItem(
                row,
                column,
                ItemBuilder.skull()
                    .texture(skullData.getValue())
                    .texture(skullData.getSignature())
                    .name(config.skull(record.getSkin(), record.getChangedAt()))
                    .lore(config.skullLore(record.getSkin(), record.getChangedAt()))
                    .asGuiItem(event -> this.setSkin(player, record.getSkin()))
            );

            column++;
        }

        if (range.offset() > 0) {
            gui.setItem(6, 2, config.previousPageItem(event -> this.open(player, range.previousRange())));
        }

        if (range.offset() + PAGE_SIZE < response.getTotal()) {
            gui.setItem(6, 6, config.nextPageItem(event -> this.open(player, range.nextRange())));
        }

        SkullData selfSkullData = skullAPI.awaitSkullData(player.getName(), 10, TimeUnit.SECONDS);

        gui.setItem(6, 4, ItemBuilder.skull()
            .texture(selfSkullData.getValue())
            .name(config.skullSelfName(player.getName()))
            .lore(config.skullSelfLore(player.getName()))
            .asGuiItem(event -> this.setSkin(player, player.getName()))
        );

        return gui;
    }

    private void setSkin(Player player, String skin) {
        skinMessenger.setSkin(player, skin);
    }

    interface Config {

        Component title();

        Component skull(String skin, Instant changedAt);

        List<Component> skullLore(String skin, Instant changedAt);

        GuiItem fillItem();

        GuiItem previousPageItem(GuiAction<InventoryClickEvent> action);

        GuiItem nextPageItem(GuiAction<InventoryClickEvent> action);

        Component skullSelfName(String skin);

        List<Component> skullSelfLore(String skin);

    }

}
