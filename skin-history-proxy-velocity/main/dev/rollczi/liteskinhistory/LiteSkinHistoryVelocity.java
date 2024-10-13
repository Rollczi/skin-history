package dev.rollczi.liteskinhistory;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.liteskinhistory.history.HistoryService;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.event.SkinApplyEvent;

import java.nio.file.Path;
import java.time.Instant;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.property.SkinIdentifier;
import net.skinsrestorer.api.property.SkinProperty;

@Plugin(id = "liteskinhistory", name = "LiteSkinHistory", version = "1.0.0", authors = { "Rollczi" }, url = "https://rollczi.dev/", dependencies = {
    @Dependency(id = "skinsrestorer")
})
public class LiteSkinHistoryVelocity {

    private final Logger logger = Logger.getLogger("LiteSkinHistory");
    private final Path dataDirectory;
    private final ProxyServer proxyServer;

    private SkinsRestorer skinsRestorerAPI;
    private LiteSkinHistory liteSkinHistory;

    @Inject
    public LiteSkinHistoryVelocity(@DataDirectory Path dataDirectory, ProxyServer proxyServer) {
        this.dataDirectory = dataDirectory;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.liteSkinHistory = new LiteSkinHistory(dataDirectory.toFile());
        this.skinsRestorerAPI = SkinsRestorerProvider.get();
        this.skinsRestorerAPI.getEventBus().subscribe(this, SkinApplyEvent.class, skinChange -> onSkinChange(skinChange));

        logger.info("LiteSkinHistory has been initialized!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.liteSkinHistory.shutdown();
    }

    @Subscribe
    public void onSkinChange(SkinApplyEvent event) {

        proxyServer.getScheduler().buildTask(this, () -> {
            try {
                HistoryService historyService = this.liteSkinHistory.getHistoryService();
                SkinProperty property = event.getProperty();

                Player player = event.getPlayer(Player.class);
                UUID uniqueId = player.getUniqueId();
                String username = player.getUsername();

                Optional<SkinIdentifier> skinId = skinsRestorerAPI.getPlayerStorage().getSkinIdForPlayer(uniqueId, username);

                if (skinId.isEmpty()) {
                    logger.warning("Skin identifier is empty for player: " + username);
                    return;
                }

                String skinName = SkinsRestorerReflect.getSkinName(skinId.get());

                historyService.createHistory(username, skinName, property.getValue(), Instant.now());
            }
            catch (DataRequestException requestException) {
                logger.warning("An error occurred while trying to save skin history: " + requestException.getMessage());
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).schedule();
    }

}
