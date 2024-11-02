package fr.twah2em.survivor.utils;

import fr.twah2em.survivor.game.player.SurvivorPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;

public class StreamUtils {
    public static <T> void forEachIndexed(List<T> list, BiConsumer<Integer, T> biConsumer) {
        for (int i = 0; i < list.size(); i++) {
            biConsumer.accept(i, list.get(i));
        }
    }

    public static <T> void fillEmptyElements(List<T> list, T element) {
        forEachIndexed(list, (index, e) -> {
            if (e == null) {
                list.set(index, element);
            }
        });
    }
}
