package dev.rollczi.liteskinhistory.bukkit.legacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class LegacyColorProcessor implements UnaryOperator<Component> {

    private static final Pattern LEGACY_PATTERN = Pattern.compile(".*");
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private static final TextReplacementConfig REPLACEMENT_CONFIG = TextReplacementConfig.builder()
            .match(LEGACY_PATTERN)
            .replacement(new LegacyReplace())
            .build();

    private static final Component RESET_ITALIC = Component.empty()
        .decoration(TextDecoration.ITALIC, false);


    @Override
    public Component apply(Component component) {
        return component.replaceText(REPLACEMENT_CONFIG);
    }

    private static class LegacyReplace implements BiFunction<MatchResult, TextComponent.Builder, ComponentLike> {

        @Override
        public ComponentLike apply(MatchResult match, TextComponent.Builder builder) {
            return RESET_ITALIC.append(LEGACY_SERIALIZER.deserialize(match.group()));
        }

    }

}
