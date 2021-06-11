package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

interface Cryptographer {

    String encrypt(String s);

    String decrypt(String s);

    void setKey(int k);
}

class ShiftAlgorithm implements Cryptographer {

    private int key;

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String encrypt(String message) {
        char[] ch = message.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            int p = (int) ch[i];
            int next_pos = p + (key % 26);
            if ((p >= 65 && p <= 90) || (p >= 97 && p < 122)) {
                if (next_pos > 122 && p >= 97) {
                    ch[i] =  (char) (96 + (next_pos % 122));
                } else if (next_pos > 90 && p < 90) {
                    ch[i] = (char) (64 + (next_pos % 90));
                } else {
                    ch[i] = (char) next_pos;
                }
            }
        }
        return new String(ch);
    }

    @Override
    public String decrypt(String message) {
        char[] ch = message.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            int p = (int) ch[i];
            int next_pos = (int) ch[i] - (key % 26);
            if ((p >= 65 && p <= 90) || (p >= 97 && p < 122)) {
                if ((int) ch[i] <= 90 && next_pos < 65) {
                    ch[i] = (char) (91 - (65 % next_pos));
                } else if ((int) ch[i] >= 97 && next_pos < 97) {
                    ch[i] = (char) (123 - (97 % next_pos));
                } else {
                    ch[i] = (char) next_pos;
                }
            }
        }
        return new String(ch);
    }
}

class UnicodeAlgorithm implements Cryptographer {

    private int key;

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String encrypt(String message) {
        char[] ch = message.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            ch[i] = (char)((int)ch[i] + key);
        }
        return new String(ch);
    }

    @Override
    public String decrypt(String message) {
        char[] ch = message.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            ch[i] = (char)((int)ch[i] - key);
        }
        return new String(ch);
    }
}

class EncoderFactory {
    public static Cryptographer getAlgorithm(String type) {
        switch (type) {
            case "shift":
                return new ShiftAlgorithm();
            case "unicode":
                return new UnicodeAlgorithm();
            default:
                return new ShiftAlgorithm();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        String method = "enc";
        Cryptographer engine = null;
        int key = 0;
        String message = null;
        String in = null;
        String out = null;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode": {
                    method = args[++i];
                    break;
                }
                case "-key": {
                    key = Integer.valueOf(args[++i]);
                    break;
                }
                case "-data": {
                    message = args[++i];
                    break;
                }
                case "-in": {
                    in = args[++i];
                    break;
                }
                case "-out": {
                    out = args[++i];
                    break;
                }
                case "-alg": {
                    engine = EncoderFactory.getAlgorithm(args[++i]);
                }
            }
        }
        engine.setKey(key);
        try {
            String text = (in != null) ? new String(Files.readAllBytes(Paths.get(in))) : message;
            //System.out.print(text);
            String processed = method.equals("enc") ? engine.encrypt(text) : engine.decrypt(text);
            if (out != null) {
                FileWriter out_file = new FileWriter(out);
                out_file.write(processed);
                out_file.close();
            } else {
                System.out.print(processed);
            }
        } catch (Exception e) {
            System.out.print("Error: something went wrong...");
        }
    }
}
