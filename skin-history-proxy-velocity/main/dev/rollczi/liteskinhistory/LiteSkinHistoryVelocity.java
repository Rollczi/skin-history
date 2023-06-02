package dev.rollczi.liteskinhistory;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import dev.rollczi.liteskinhistory.history.HistoryService;
import net.skinsrestorer.api.velocity.events.SkinApplyVelocityEvent;

import java.nio.file.Path;

@Plugin(id = "liteskinhistory", name = "LiteSkinHistory", version = "1.0.0", authors = { "Rollczi" }, url = "https://rollczi.dev/")
public class LiteSkinHistoryVelocity {

    private final Path dataDirectory;
    private LiteSkinHistory liteSkinHistory;

    @Inject
    public LiteSkinHistoryVelocity(@DataDirectory Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.liteSkinHistory = new LiteSkinHistory(dataDirectory.toFile());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.liteSkinHistory.shutdown();
    }

    @Subscribe
    public void onSkinChange(SkinApplyVelocityEvent event) {
        HistoryService historyService = this.liteSkinHistory.getHistoryService();
        historyService.createHistory(event.getWho().getUsername(), event.getProperty().getName());
    }

}
