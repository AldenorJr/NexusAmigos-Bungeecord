package br.com.nexus.plugin.API;

import br.com.nexus.plugin.cache.AmigosCache;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NexusAmigosAPI {

    public Boolean hasFriends(ProxiedPlayer proxiedPlayer, ProxiedPlayer proxiedPlayer2) {
        return AmigosCache.hashMapList.get(proxiedPlayer).getFriendList().contains(proxiedPlayer2.getName());
    }

}
