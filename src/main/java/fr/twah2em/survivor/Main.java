package fr.twah2em.survivor;

import fr.twah2em.survivor.commands.weapons.GiveWeaponCommand;
import fr.twah2em.survivor.commands.start.StartCommand;
import fr.twah2em.survivor.commands.internal.SurvivorCommandRegistration;
import fr.twah2em.survivor.commands.room.RoomCommand;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.GameLogic;
import fr.twah2em.survivor.game.player.ScoreboardManager;
import fr.twah2em.survivor.listeners.*;
import fr.twah2em.survivor.listeners.internal.SurvivorListenerRegistration;
import fr.twah2em.survivor.listeners.internal.inventories.InventoryClickListener;
import fr.twah2em.survivor.listeners.internal.inventories.InventoryCloseListener;
import fr.twah2em.survivor.listeners.internal.inventories.InventoryOpenListener;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private GameLogic gameLogic;
    private GameInfos gameInfos;

    @Override
    public void onEnable() {
        getLogger().info("Main plugin is starting !");

        saveDefaultConfig();

        SurvivorCommandRegistration.registerCommands(this,
                StartCommand::new,
                RoomCommand::new,
                GiveWeaponCommand::new
        );
        SurvivorListenerRegistration.registerListeners(this,
                InventoryClickListener::new,
                InventoryCloseListener::new,
                InventoryOpenListener::new,
                AsyncChatListener::new,
                PlayerJoinListener::new,
                PlayerQuitListener::new,
                EntityDamageByEntityListener::new,
                EntityDeathListener::new,
                PlayerMoveListener::new,
                PlayerEnterCuboidListener::new,
                PlayerDropItemListener::new,
                BlockBreakListener::new,
                PlayerInteractListener::new
        );

        this.gameLogic = new GameLogic(this);
        this.gameInfos = new GameInfos(this, this.getConfig());
        new ScoreboardManager(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getWorlds().get(0).getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
        Bukkit.getOnlinePlayers().forEach(player -> player.kick(Messages.RESTART_MESSAGE));
    }

    public GameLogic gameLogic() {
        return gameLogic;
    }

    public GameInfos gameInfos() {
        return gameInfos;
    }
}
