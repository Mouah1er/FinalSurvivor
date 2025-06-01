package fr.twah2em.survivor.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.game.player.corpse.CorpseManager;
import fr.twah2em.survivor.game.player.corpse.ReviveCooldownManager;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.LocationUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerToggleSneakListener implements SurvivorListener<PlayerToggleSneakEvent> {
    private final Main main;

    public PlayerToggleSneakListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        final UUID reviver = player.getUniqueId();
        final SurvivorPlayer survivorPlayer = SurvivorPlayer.survivorPlayer(player, main.gameInfos().players());

        if (survivorPlayer != null) {
            if (player.getGameMode() == GameMode.SPECTATOR) return;

            final ReviveCooldownManager reviveCooldownManager = main.gameLogic().reviveCooldownManager();

            if (event.isSneaking()) {
                final HashMap<UUID, String> playerCorpse = CorpseManager.PLAYER_CORPSE_NAME;
                if (playerCorpse.isEmpty()) return;

                final List<Entity> entities = LocationUtils.entitiesByLocation(player.getLocation(), 1, 1, 1);

                entities.stream().filter(entity -> !entity.getUniqueId().equals(player.getUniqueId())).forEach(entity -> {
                    System.out.println("test1");
                    if (entity instanceof Player) {
                        System.out.println("test2");
                        playerCorpse.forEach((revived, name) -> {
                            System.out.println(name);
                            if (!(name.equals(entity.getName()))) return;

                            if (reviveCooldownManager.isBeingRevivedBySomeone(revived)) return;

                            reviveCooldownManager.cooldownRunnable(reviver, revived, 80);
                        });
                    }
                });

                return;
            }

            if (reviveCooldownManager.isRevivingSomeone(reviver)) {
                final Player revived = Bukkit.getPlayer(reviveCooldownManager.whoIsBeingRevived(reviver));
                reviveCooldownManager.cancelReanimation(player, revived);
            }
        }
    }
}
