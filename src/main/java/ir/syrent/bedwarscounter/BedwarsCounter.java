package ir.syrent.bedwarscounter;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.minecrell.serverlistplus.core.ServerListPlusCore;
import net.minecrell.serverlistplus.core.player.PlayerIdentity;
import net.minecrell.serverlistplus.core.replacement.LiteralPlaceholder;
import net.minecrell.serverlistplus.core.replacement.ReplacementManager;
import net.minecrell.serverlistplus.core.status.StatusResponse;
import org.slf4j.Logger;

@Plugin(
        id = "bedwarscounter",
        name = "BedwarsCounter",
        version = BuildConstants.VERSION,
        authors = {"Syrent"}
)
public class BedwarsCounter {

    private Logger logger;
    private ProxyServer server;

    @Inject
    public BedwarsCounter(Logger logger, ProxyServer server) {
        this.logger = logger;
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ReplacementManager.getDynamic().add(new LiteralPlaceholder("%bedwars_total%") {
            @Override
            public String replace(StatusResponse response, String s) {
                PlayerIdentity identity = response.getRequest().getIdentity();
                if (identity != null) {
                    int count = 0;
                    for (RegisteredServer server : server.getAllServers()) {
                        if (server.getServerInfo().getName().toLowerCase().startsWith("bedwars") || server.getServerInfo().getName().toLowerCase().startsWith("bw")) {
                            count += server.getPlayersConnected().size();
                        }
                    }
                    return String.valueOf(count);
                } else // Use the method below if player is unknown
                    return super.replace(response, s);
            }

            @Override
            public String replace(ServerListPlusCore core, String s) {
                int count = 0;
                for (RegisteredServer server : server.getAllServers()) {
                    if (server.getServerInfo().getName().toLowerCase().startsWith("bedwars") || server.getServerInfo().getName().toLowerCase().startsWith("bw")) {
                        count += server.getPlayersConnected().size();
                    }
                }
                return String.valueOf(count);
            }
        });
    }
}
