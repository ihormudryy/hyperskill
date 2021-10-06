class Problem {

    public static void main(String[] args) {
        switch(args[0]) {
            case "+":
                System.out.print(Integer.parseInt(args[1]) + Integer.parseInt(args[2]));
                break;
            case "-":
                System.out.print(Integer.parseInt(args[1]) - Integer.parseInt(args[2]));
                break;
            case "*":
                System.out.print(Integer.parseInt(args[1]) * Integer.parseInt(args[2]));
                break;
            default:
                System.out.print("Unknown operator");
        }
    }
}