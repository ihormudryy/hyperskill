class Predicate {

    @FunctionalInterface
    public interface TernaryIntPredicate {
        public boolean test(int a, int b, int c);
    }

    public static final TernaryIntPredicate allValuesAreDifferentPredicate =
            new TernaryIntPredicate() {
                @Override
                public boolean test(int a, int b, int c) {
                    return a != b && b != c && a != c;
                }
    };
}