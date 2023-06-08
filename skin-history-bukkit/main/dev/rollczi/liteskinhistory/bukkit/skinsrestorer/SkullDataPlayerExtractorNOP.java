package dev.rollczi.liteskinhistory.bukkit.skinsrestorer;

import dev.rollczi.liteskullapi.PlayerIdentification;
import dev.rollczi.liteskullapi.SkullData;
import dev.rollczi.liteskullapi.extractor.SkullDataPlayerExtractor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkullDataPlayerExtractorNOP implements SkullDataPlayerExtractor {
    @Override
    public CompletableFuture<Optional<SkullData>> extractData(PlayerIdentification playerIdentification) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public void setExecutor(Executor executor) {}

}
