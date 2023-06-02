package dev.rollczi.liteskinhistory.bukkit.skin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SkinMessenger {

    private static final String SET_SKIN = "setSkin";
    private static final String MESSAGE_CHANNEL = "sr:messagechannel";

    private final Plugin plugin;
    private final Messenger messenger;

    public SkinMessenger(Plugin plugin, Messenger messenger) {
        this.plugin = plugin;
        this.messenger = messenger;
    }

    public void setSkin(Player player, String skin) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);

            out.writeUTF(SET_SKIN);
            out.writeUTF(player.getName());
            out.writeUTF(skin);

            player.sendPluginMessage(plugin, MESSAGE_CHANNEL, bytes.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void register() {
        this.messenger.registerOutgoingPluginChannel(plugin, MESSAGE_CHANNEL);
    }

    public void unregister() {
        this.messenger.unregisterOutgoingPluginChannel(plugin, MESSAGE_CHANNEL);
    }

}
