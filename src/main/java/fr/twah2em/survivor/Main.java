package fr.twah2em.survivor;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.twah2em.survivor.commands.StartCommand;
import fr.twah2em.survivor.commands.internal.SurvivorCommandRegistration;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.GameLogic;
import fr.twah2em.survivor.listeners.*;
import fr.twah2em.survivor.listeners.internal.SurvivorListenerRegistration;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private GameLogic gameLogic;
    private GameInfos gameInfos;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        getLogger().info("Main plugin is starting !");
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        SurvivorCommandRegistration.registerCommands(this,
                StartCommand::new
        );
        SurvivorListenerRegistration.registerListeners(this,
                AsyncChatListener::new,
                PlayerJoinListener::new,
                PlayerQuitListener::new,
                EntityDamageListener::new,
                EntityDeathListener::new
        );

        this.gameLogic = new GameLogic(this);
        this.gameInfos = new GameInfos();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.kick(Messages.RESTART_MESSAGE));
    }

    public ProtocolManager protocolManager() {
        return protocolManager;
    }

    public GameLogic gameLogic() {
        return gameLogic;
    }

    public GameInfos gameInfos() {
        return gameInfos;
    }
}
