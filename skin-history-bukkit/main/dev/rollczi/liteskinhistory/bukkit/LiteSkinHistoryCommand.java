package dev.rollczi.liteskinhistory.bukkit;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.liteskinhistory.LiteSkinHistory;
import org.bukkit.entity.Player;

@Route(name = "skiny")
class LiteSkinHistoryCommand {

    private final LiteSkinHistoryGui historyGui;
    private final LiteSkinHistory liteSkinHistory;

    LiteSkinHistoryCommand(LiteSkinHistoryGui historyGui, LiteSkinHistory liteSkinHistory) {
        this.historyGui = historyGui;
        this.liteSkinHistory = liteSkinHistory;
    }

    @Execute
    public void execute(Player player) {
        historyGui.open(player);
    }

    @Execute(route = "reload")
    @Permission("lsh.reload")
    public void reload(LiteSender liteSender) {
        liteSkinHistory.getConfigService().reloadAll();
        liteSender.sendMessage("&aConfiguration reloaded!");
    }

}
