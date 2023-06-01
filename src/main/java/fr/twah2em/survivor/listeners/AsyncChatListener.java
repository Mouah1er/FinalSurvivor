package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Messages;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class AsyncChatListener implements SurvivorListener<AsyncChatEvent> {
    private final Main main;

    public AsyncChatListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final TextComponent message = (TextComponent) event.message();
        final Map<UUID, CompletableFuture<Boolean>> uuidCompletableFutureMap = main.gameInfos().startCommandConfirmation();

        if (uuidCompletableFutureMap.containsKey(player.getUniqueId())) {
            event.setCancelled(true);

            final String content = message.content();
            final boolean isValid = content.equalsIgnoreCase("oui") || content.equalsIgnoreCase("non");

            if (isValid) {
                if (content.equalsIgnoreCase("oui")) {
                    uuidCompletableFutureMap.get(player.getUniqueId()).complete(true);
                } else {
                    uuidCompletableFutureMap.get(player.getUniqueId()).complete(false);
                }

                uuidCompletableFutureMap.remove(player.getUniqueId());
            } else {
                player.sendMessage(Messages.PREFIX
                        .append(text("Vous devez répondre par ", YELLOW))
                        .append(text("Oui ", GREEN))
                        .append(text("ou par ", YELLOW))
                        .append(text("Non ", RED))
                        .append(text("pour lancer la partie.", YELLOW)));
            }
        }
    }
}
