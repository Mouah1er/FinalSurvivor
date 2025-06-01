package fr.twah2em.survivor.utils;

import java.util.Objects;

public interface Pair<L, R> {
    static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>() {
            @Override
            public L left() {
                return left;
            }

            @Override
            public R right() {
                return right;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof Pair<?, ?>)) return false;
                Pair<?, ?> pair = (Pair<?, ?>) obj;
                return Objects.equals(left(), pair.left()) &&
                        Objects.equals(right(), pair.right());
            }

            @Override
            public int hashCode() {
                return Objects.hash(left(), right());
            }

            @Override
            public String toString() {
                return "Pair[" + left() + ", " + right() + "]";
            }
        };
    }

    L left();

    R right();
}
