package carsharing.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao implements Dao<Customer> {

    private final String tableName;
    private final Connection connection;

    public CustomerDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        String sqlCompany = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                "RENTED_CAR_ID INTEGER, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "PRIMARY KEY ( ID ), " +
                "CONSTRAINT FK_RENTED_CAR FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID));";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlCompany);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> get(int id) {
        return null;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("ID"),
                                                rs.getString("NAME"),
                                                rs.getInt("RENTED_CAR_ID"));
                result.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void save(Customer customer) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO %s (NAME) VALUES (\'%s\')",
                    tableName, customer.getName());
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Customer customer, String[] params) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("UPDATE %s SET RENTED_CAR_ID=%s WHERE ID=%s",
                    tableName, params[0], customer.getId());
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(Customer customer) {

    }

    @Override
    public void eraseById(Integer id) {

    }

    @Override
    public void eraseByName(String name) {

    }

    public Optional<Car> getRentedCar(Integer id) {
        Car car = null;
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * " +
                                        "FROM CAR " +
                                        "INNER JOIN CUSTOMER " +
                                        "ON CAR.ID=CUSTOMER.RENTED_CAR_ID " +
                                        "WHERE CUSTOMER.ID=%s",
                                        id);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                car = new Car(rs.getInt("CAR.ID"),
                        rs.getString("CAR.NAME"),
                        rs.getInt("CAR.COMPANY_ID"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.ofNullable(car);
    }

    public boolean isCarLocked(Integer id) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT 1 FROM %s WHERE RENTED_CAR_ID=%s", tableName, id);
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void releaseCar(Integer id) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("UPDATE %s SET RENTED_CAR_ID = NULL WHERE ID=%s", tableName, id);
            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
