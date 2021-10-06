import java.io.Serializable;
import java.util.Scanner;

class User implements Serializable {

    private static final long serialVersionUID = 1L;

    String name;
    transient String password;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String password = s.nextLine();
        String encryptPassword = "";
        for (int i = 0; i < password.length(); i++) {
            encryptPassword += String.valueOf((char)(password.charAt(i) + 1));
        }
        String decryptPassword = "";
        for (int i = 0; i < encryptPassword.length(); i++) {
            decryptPassword += String.valueOf((char)(encryptPassword.charAt(i) - 1));
        }
        System.out.println(encryptPassword);
        System.out.println(decryptPassword.substring(0, 3).matches("[0]+"));

    }
}