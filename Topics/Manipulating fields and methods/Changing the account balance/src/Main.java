import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final class AccountUtils {

    private AccountUtils() { }

    public static void increaseBalance(Account account, long amount) throws IllegalAccessException {
        Field[] fields = account.getClass().getDeclaredFields();
        for (Field field: fields) {
            if (Modifier.isPrivate(field.getModifiers()) && field.getName().equals("balance")) {
                field.setAccessible(true);
                field.set(account, (long) field.get(account) + amount);
            }
        }
    }
}
