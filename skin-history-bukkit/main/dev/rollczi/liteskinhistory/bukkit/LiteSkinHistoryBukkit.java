package dev.rollczi.liteskinhistory.bukkit;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.liteskinhistory.LiteSkinHistory;
import dev.rollczi.liteskinhistory.bukkit.legacy.LegacyColorProcessor;
import dev.rollczi.liteskinhistory.bukkit.scheduler.Scheduler;
import dev.rollczi.liteskinhistory.bukkit.scheduler.SchedulerBukkitImpl;
import dev.rollczi.liteskinhistory.bukkit.skin.SkinMessenger;
import dev.rollczi.liteskinhistory.history.HistoryService;
import dev.rollczi.liteskullapi.LiteSkullFactory;
import dev.rollczi.liteskullapi.SkullAPI;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class LiteSkinHistoryBukkit extends JavaPlugin {

    private LiteSkinHistory liteSkinHistory;
    private SkullAPI skullAPI;
    private SkinMessenger skinMessenger;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        AudienceProvider audiencesProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
            .postProcessor(new LegacyColorProcessor())
            .build();

        this.liteSkinHistory = new LiteSkinHistory(this.getDataFolder());
        this.skullAPI = LiteSkullFactory.builder()
                .bukkitScheduler(this)
                .cacheExpireAfterWrite(Duration.ofMinutes(30))
                .cacheExpireAfterAccess(Duration.ofHours(1))
                .dataBaseSaveExpire(Duration.ofHours(4))
                .build();

        this.skinMessenger = new SkinMessenger(this, this.getServer().getMessenger());
        this.skinMessenger.register();

        Scheduler scheduler = new SchedulerBukkitImpl(this);
        HistoryService historyService = this.liteSkinHistory.getHistoryService();
        LiteSkinHistoryConfig config = liteSkinHistory.getConfigService().load(LiteSkinHistoryConfig.class);
        LiteSkinHistoryGui historyGui = new LiteSkinHistoryGui(historyService, scheduler, this.skullAPI, this.skinMessenger, config);

        this.liteCommands = LiteBukkitAdventurePlatformFactory.builder(this.getServer(), "lsh", false, audiencesProvider, miniMessage)
                .commandInstance(new LiteSkinHistoryCommand(historyGui, liteSkinHistory))
                .register();
    }

    @Override
    public void onDisable() {
        this.skinMessenger.unregister();
        this.liteSkinHistory.shutdown();
        this.skullAPI.shutdown();
        this.liteCommands.getPlatform().unregisterAll();
    }

}
