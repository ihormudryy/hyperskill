import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class UserProfile implements Serializable {
    private static final long serialVersionUID = 26292552485L;

    private String login;
    private String email;
    private transient String password;

    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        String encryptPassword = "";
        for (int i = 0; i < password.length(); i++) {
            encryptPassword += String.valueOf((char)(password.charAt(i) + 1));
        }
        oos.writeObject(encryptPassword);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        String encryptPassword = (String) ois.readObject();
        password = "";
        for (int i = 0; i < encryptPassword.length(); i++) {
            password += String.valueOf((char)(encryptPassword.charAt(i) - 1));
        }
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}