package dev.rollczi.liteskinhistory;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.liteskinhistory.history.HistoryService;
import java.util.Optional;
import java.util.UUID;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.event.SkinApplyEvent;

import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.property.SkinIdentifier;
import net.skinsrestorer.api.property.SkinProperty;

@Plugin(id = "liteskinhistory", name = "LiteSkinHistory", version = "1.0.0", authors = { "Rollczi" }, url = "https://rollczi.dev/", dependencies = {
    @Dependency(id = "skinsrestorer")
})
public class LiteSkinHistoryVelocity {

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private final Path dataDirectory;

    private SkinsRestorer skinsRestorerAPI;
    private LiteSkinHistory liteSkinHistory;

    @Inject
    public LiteSkinHistoryVelocity(@DataDirectory Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.liteSkinHistory = new LiteSkinHistory(dataDirectory.toFile());
        this.skinsRestorerAPI = SkinsRestorerProvider.get();
        this.skinsRestorerAPI.getEventBus().subscribe(this, SkinApplyEvent.class, skinChange -> onSkinChange(skinChange));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.liteSkinHistory.shutdown();
        this.executorService.shutdown();
    }

    @Subscribe
    public void onSkinChange(SkinApplyEvent event) {
        executorService.submit(() -> {
            try {
                HistoryService historyService = this.liteSkinHistory.getHistoryService();
                SkinProperty property = event.getProperty();

                Player player = event.getPlayer(Player.class);
                UUID uniqueId = player.getUniqueId();
                String username = player.getUsername();

                Optional<SkinIdentifier> skinId = skinsRestorerAPI.getPlayerStorage().getSkinIdForPlayer(uniqueId, username);

                if (skinId.isEmpty()) {
                    return;
                }

                historyService.createHistory(username, skinId.get().getIdentifier(), property.getValue(), Instant.now());
            }
            catch (DataRequestException requestException) {
                throw new RuntimeException(requestException);
            }
        });
    }

}
