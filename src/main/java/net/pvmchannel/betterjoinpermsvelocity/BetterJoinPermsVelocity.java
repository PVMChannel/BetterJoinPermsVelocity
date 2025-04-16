package net.pvmchannel.betterjoinpermsvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "betterjoinpermsvelocity",
        name = "BetterJoinPermsVelocity",
        version = BuildConstants.VERSION,
        authors = {"PVMChannel"}
)
public class BetterJoinPermsVelocity {
    @Inject
    @DataDirectory
    private Path dataDirectory;

    @Inject
    private Logger logger;

    private CommentedConfigurationNode config;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {

        if (Files.notExists(dataDirectory)) {
            Files.createDirectory(dataDirectory);
        }
        final Path configFile = dataDirectory.resolve("config.yml");
        if (Files.notExists(configFile)) {
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(stream, configFile);
            }
        }
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(configFile).build();
        config = loader.load();
    }

    @Subscribe(priority = Short.MAX_VALUE, order = PostOrder.CUSTOM)
    public void onPlayerPreConnect(ServerPreConnectEvent event) throws SerializationException {
        // don't allow anyone joining if the config is invalid
        // because it could lead to big security issues
        if(config == null){
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
            event.getPlayer().disconnect(MiniMessage.miniMessage().deserialize("<red>Configuration error</red>"));
            logger.error("The BetterJoinPermsVelocity config is null! It has either not loaded yet, or is invalid!");
            return;
        }

        String serverName = event.getOriginalServer().getServerInfo().getName();

        boolean hasServerEntry = config.node("servers").hasChild(serverName);

        // if not controlled server
        if(!hasServerEntry && !config.node("restrictAllServers").getBoolean()) return;

        String permissionToCheck;

        if(hasServerEntry){
            permissionToCheck = config.node("servers").node(serverName).getString();
        }else{
            // if is excluded
            if(config.node("excludeServers").getList(String.class).contains(serverName)) return;

            permissionToCheck = "*";
        }

        // if the player cant connect
        if(!event.getPlayer().getPermissionChecker().test("betterjoinpermsvelocity." + permissionToCheck)){
            // deny!
            event.setResult(ServerPreConnectEvent.ServerResult.denied());

            if(config.node("messages").hasChild(permissionToCheck)){
                // send message if there is one set up
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(config.node("messages").node(permissionToCheck).getString()));
            }
        }
    }
}
