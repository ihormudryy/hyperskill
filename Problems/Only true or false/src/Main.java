class Primitive {
    public static boolean toPrimitive(Boolean b) {
        return b == null ? false : b.booleanValue();
    }
}