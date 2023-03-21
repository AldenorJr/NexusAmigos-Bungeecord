package br.com.nexus.plugin.listener;

import br.com.nexus.plugin.api.VeantyCoreAPI;
import br.com.nexus.plugin.cache.AmigosCache;
import br.com.nexus.plugin.model.PlayerObject;
import br.com.nexus.plugin.storage.HikaridConnect;
import br.com.nexus.plugin.storage.database.DatabaseMethod;
import br.com.nexus.plugin.util.TextComponentUtil;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class EventPlayer implements Listener {

    private final HikaridConnect hikaridConnect;
    private final DatabaseMethod databaseMethod;
    private final TextComponentUtil textComponentUtil;

    @EventHandler
    public void toJoin(PostLoginEvent e) {
        ProxiedPlayer proxiedPlayer = e.getPlayer();
        if(!databaseMethod.hasProxiedPlayer(proxiedPlayer)) databaseMethod.setProxiedPlayer(proxiedPlayer);
        PlayerObject proxiedPlayerObject = databaseMethod.getPlayerObjectByPorxiedPlayer(proxiedPlayer);
        AmigosCache.hashMapList.put(proxiedPlayer, proxiedPlayerObject);
        toJoinEvent(proxiedPlayerObject);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        toQuitEvent(AmigosCache.hashMapList.get(e.getPlayer()));
        AmigosCache.hashMapList.remove(e.getPlayer());
    }

    public void toJoinEvent(PlayerObject playerObject) {
        for(String amigosNick : playerObject.getFriendList()) {
            try {
                ProxiedPlayer proxiedPlayer = BungeeCord.getInstance().getPlayer(amigosNick);
                if(proxiedPlayer != null) {
                    proxiedPlayer.sendMessage(textComponentUtil.createTextComponent(
                            "§e[§a§l+§e] Seu amigo "+ new VeantyCoreAPI().getTagUtil(proxiedPlayer).getTag()+ " " +playerObject.getProxiedPlayer().getName()+"§e, entrou no servidor."));
                }
            } catch (Exception ignored) {}
        }
    }

    public void toQuitEvent(PlayerObject playerObject) {
        for(String amigosNick : playerObject.getFriendList()) {
            try {
                ProxiedPlayer proxiedPlayer = BungeeCord.getInstance().getPlayer(amigosNick);
                if(proxiedPlayer != null) {
                    proxiedPlayer.sendMessage(textComponentUtil.createTextComponent(
                            "§e[§4§l-§e] Seu amigo "+ new VeantyCoreAPI().getTagUtil(proxiedPlayer).getTag()+ " " +playerObject.getProxiedPlayer().getName()+"§e, saiu no servidor."));
                }
            } catch (Exception ignored) {}
        }
    }


}