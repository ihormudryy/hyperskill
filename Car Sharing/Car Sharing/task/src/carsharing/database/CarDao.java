package carsharing.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDao implements Dao<Car> {

    private final String tableName;
    private final Connection connection;

    public CarDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        String sqlCompany = "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INTEGER NOT NULL AUTO_INCREMENT, " +
                "COMPANY_ID INTEGER NOT NULL, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "PRIMARY KEY ( ID ), " +
                "CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID));";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlCompany);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Car> get(int id) {
        Car car = null;
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE ID=%s", tableName, id);
            ResultSet rs = stmt.executeQuery(sql);
            car = new Car(rs.getString("NAME"), rs.getInt("COMPANY_ID"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> getAll() {
        List<Car> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Car car = new Car(rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getInt("COMPANY_ID"));
                result.add(car);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public List<Car> getByCompanyId(Integer companyId) {
        List<Car> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s " +
                                        "WHERE COMPANY_ID=%s",
                                        tableName, companyId);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Car car = new Car(rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getInt("COMPANY_ID"));
                result.add(car);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void save(Car s) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO %s (NAME, COMPANY_ID) VALUES (\'%s\', %s)",
                    tableName, s.getName(), s.getCompanyId());
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Car s, String[] params) {

    }

    @Override
    public void delete(Car s) {

    }

    @Override
    public void eraseById(Integer id) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("DELETE FROM %s WHERE COMPANY_ID=%s", tableName, id);
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void eraseByName(String name) {

    }
}
