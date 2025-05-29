package fr.twah2em.survivor.game.player.corpse;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import fr.twah2em.survivor.Main;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Pose;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CorpseManager {
    public static final HashMap<UUID, Integer> PLAYER_CORPSE = new HashMap<>();

    public static void createCorpse(Player player, Main main) {
        final ProtocolManager protocolManager = main.protocolManager();
        player.setGameMode(GameMode.SPECTATOR);

        final WrappedGameProfile playerProfile = WrappedGameProfile.fromPlayer(player);
        final WrappedSignedProperty textures = playerProfile.getProperties().get("textures").stream().findFirst().orElse(null);

        final WrappedGameProfile corpse = new WrappedGameProfile(UUID.randomUUID(), player.getName());

        if (textures != null) {
            corpse.getProperties().put("textures", new WrappedSignedProperty("textures", textures.getValue(), textures.getSignature()));
        }

        final int id = (int) (Math.random() * Integer.MAX_VALUE);

        final PacketContainer playerInfoPacket = playerInfoPacket(corpse, protocolManager);
        final PacketContainer spawnEntityPacket = spawnEntityPacket(corpse, player, protocolManager, id);
        final PacketContainer entityMetadataPacket = entityMetadataPacket(protocolManager, id);
        final PacketContainer rotationPacket = rotationPacket(protocolManager, id);

        player.getWorld().getPlayers().forEach(player1 -> {
            protocolManager.sendServerPacket(player1, playerInfoPacket);
            protocolManager.sendServerPacket(player1, spawnEntityPacket);
            protocolManager.sendServerPacket(player1, entityMetadataPacket);
            protocolManager.sendServerPacket(player1, rotationPacket);

            for (final EnumWrappers.ItemSlot itemSlot : EnumWrappers.ItemSlot.values()) {
                protocolManager.sendServerPacket(player1, armorPacket(protocolManager, id, itemSlot));
            }
        });

        PLAYER_CORPSE.put(player.getUniqueId(), id);
    }

    private static PacketContainer playerInfoPacket(WrappedGameProfile corpse, ProtocolManager protocolManager) {
        PlayerInfoData playerInfoData = new PlayerInfoData(
                corpse.getUUID(),
                0,
                false,
                EnumWrappers.NativeGameMode.CREATIVE,
                corpse,
                null
        );

        final PacketContainer playerInfoPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        playerInfoPacket.getPlayerInfoActions().write(0, Collections.singleton(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
        playerInfoPacket.getPlayerInfoDataLists().write(1, Collections.singletonList(playerInfoData));

        return playerInfoPacket;
    }

    private static PacketContainer spawnEntityPacket(WrappedGameProfile corpse, Player player, ProtocolManager protocolManager, int id) {
        final PacketContainer spawnEntityPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnEntityPacket.getUUIDs().write(0, corpse.getUUID());
        spawnEntityPacket.getIntegers().write(0, id);

        final Location location = player.getLocation();
        spawnEntityPacket.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());
        spawnEntityPacket.getBytes()
                .write(0, (byte) 0)
                .write(1, (byte) -90);
        spawnEntityPacket.getEntityTypeModifier().write(0, EntityType.PLAYER);

        return spawnEntityPacket;
    }

    private static PacketContainer entityMetadataPacket(ProtocolManager protocolManager, int id) {
        final WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        final WrappedDataWatcher.Serializer serializer = new WrappedDataWatcher.Serializer(Pose.class, EntityDataSerializers.POSE, false);

        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(6, serializer), Pose.SLEEPING);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(17, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x7F);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x40);

        final PacketContainer entityMetadataPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        final List<WrappedDataValue> wrappedDataValues = dataWatcher.getWatchableObjects().stream()
                .map(watchableObject -> new WrappedDataValue(
                        watchableObject.getIndex(), watchableObject.getWatcherObject().getSerializer(), watchableObject.getValue())
                )
                .toList();

        entityMetadataPacket.getIntegers().write(0, id);
        entityMetadataPacket.getDataValueCollectionModifier().write(0, wrappedDataValues);

        return entityMetadataPacket;
    }

    private static PacketContainer rotationPacket(ProtocolManager protocolManager, int id) {
        PacketContainer rotationPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        rotationPacket.getIntegers().write(0, id);
        rotationPacket.getBytes().write(0, (byte) 0);

        return rotationPacket;
    }

    private static PacketContainer armorPacket(ProtocolManager protocolManager, int id, EnumWrappers.ItemSlot itemSlot) {
        PacketContainer armorPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);

        armorPacket.getIntegers().write(0, id);

        final Pair<EnumWrappers.ItemSlot, ItemStack> pair = new Pair<>(itemSlot, null);
        final List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipmentList = List.of(pair);

        armorPacket.getSlotStackPairLists().write(0, equipmentList);

        return armorPacket;
    }

    public static void destroyCorpse(Main main, Player player) {
        final Integer corpseId = PLAYER_CORPSE.get(player.getUniqueId());

        if (corpseId != null) {
            final PacketContainer entityDestroyPacket = main.protocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            entityDestroyPacket.getModifier().write(0, IntList.of(corpseId));

            player.getWorld().getPlayers().forEach(player1 -> main.protocolManager().sendServerPacket(player1, entityDestroyPacket));

            PLAYER_CORPSE.remove(player.getUniqueId());
        }
    }
}
