package fr.twah2em.survivor.utils;

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
        };
    }

    L left();

    R right();
}
