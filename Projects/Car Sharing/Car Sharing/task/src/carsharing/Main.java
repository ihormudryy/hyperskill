package carsharing;

import com.beust.jcommander.JCommander;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    static final String JDBC_DRIVER = "org.h2.Driver";

    public static void main(String[] args) throws SQLException {
        Args argv = new Args();
        JCommander commander = JCommander.newBuilder().addObject(argv).build();
        try {
            Class.forName(JDBC_DRIVER);
            String filePath = "./src/carsharing/db/" + (argv.dbName == null ? "carsharing" : argv.dbName);
            Connection connection = DriverManager.getConnection("jdbc:h2:file:" + filePath);
            connection.setAutoCommit(true);
            CommandLineMenu.printMainMenu(connection);
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Wrong class");
            e.printStackTrace();
        } catch (JdbcSQLSyntaxErrorException e) {
            System.out.println("Table exists");
            e.printStackTrace();
        }
    }
}