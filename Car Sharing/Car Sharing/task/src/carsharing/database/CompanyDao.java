package carsharing.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDao implements Dao<Company> {

    private final String tableName;
    private final Connection connection;

    public CompanyDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        String sqlCompany = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(ID INTEGER NOT NULL AUTO_INCREMENT," +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "PRIMARY KEY ( ID ))";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlCompany);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Company> get(int id) {
        Company company = null;
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE ID=%s", tableName, id);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                company = new Company(rs.getString("NAME"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.ofNullable(company);
    }

    @Override
    public List<Company> getAll() {
        List<Company> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Company company = new Company(rs.getInt("id"), rs.getString("name"));
                result.add(company);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void save(Company company) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO %s (name) VALUES (\'%s\')", tableName, company.getName());
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Company company, String[] params) {
    }

    @Override
    public void delete(Company company) {
    }

    @Override
    public void eraseById(Integer id) {

    }

    @Override
    public void eraseByName(String name) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("DELETE FROM %s WHERE name='%s'", tableName, name);
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
