package dev.rollczi.liteskinhistory;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import dev.rollczi.liteskinhistory.history.HistoryService;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.api.velocity.events.SkinApplyVelocityEvent;

import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Plugin(id = "liteskinhistory", name = "LiteSkinHistory", version = "1.0.0", authors = { "Rollczi" }, url = "https://rollczi.dev/", dependencies = {
    @Dependency(id = "skinsrestorer")
})
public class LiteSkinHistoryVelocity {

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private final Path dataDirectory;

    private SkinsRestorerAPI skinsRestorerAPI;
    private LiteSkinHistory liteSkinHistory;

    @Inject
    public LiteSkinHistoryVelocity(@DataDirectory Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.liteSkinHistory = new LiteSkinHistory(dataDirectory.toFile());
        this.skinsRestorerAPI = SkinsRestorerAPI.getApi();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.liteSkinHistory.shutdown();
        this.executorService.shutdown();
    }

    @Subscribe
    public void onSkinChange(SkinApplyVelocityEvent event) {
        executorService.submit(() -> {
            HistoryService historyService = this.liteSkinHistory.getHistoryService();
            IProperty property = event.getProperty();
            String username = event.getWho().getUsername();
            String skinName = skinsRestorerAPI.getSkinName(username);

            if (skinName == null) {
                return;
            }

            historyService.createHistory(username, skinName, property.getValue(), Instant.now());
        });
    }

}
