class Problem {

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            System.out.format("%s=%s\n", args[i], args[i + 1]);
        }
    }
}
