class BigIntegerConverter {

    /**
     * @param number string representing the number
     * @return BigInteger instance
     */
    public static java.math.BigInteger getBigInteger(String number) {
        java.math.BigInteger a = new java.math.BigInteger(number);
        return a;
    }
}