package taxi.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import taxi.dao.DriverDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;

@Dao
public class DriverDaoJdbcImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        String insertQuery = "INSERT INTO drivers (name, license_number, login, password) "
                + "VALUES (?, ?, ?, ?);";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery,
                         Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1,driver.getName());
            insertStatement.setString(2,driver.getLicenseNumber());
            insertStatement.setString(3, driver.getLogin());
            insertStatement.setString(4, driver.getPassword());
            insertStatement.executeUpdate();
            ResultSet resultSet = insertStatement.getGeneratedKeys();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not add new driver: "
                    + driver.toString(), e);
        }
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        String selectQuery = "SELECT * FROM drivers WHERE deleted = FALSE AND id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createDriver(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get a driver with id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Driver> getAll() {
        String selectAll = "SELECT * FROM drivers WHERE deleted = FALSE;";

        List<Driver> driverList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                driverList.add(createDriver(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get any drivers", e);
        }
        return driverList;
    }

    @Override
    public Driver update(Driver driver) {
        String updateQuery = "UPDATE drivers SET name = ?,"
                + "license_number = ? WHERE id = ? AND deleted = FALSE;";

        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {
            preparedStatement.setString(1, driver.getName());
            preparedStatement.setString(2, driver.getLicenseNumber());
            preparedStatement.setLong(3, driver.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not update driver: "
                    + driver.toString(), e);
        }
        return driver;
    }

    @Override
    public boolean delete(Long id) {
        String deleteQuery = "UPDATE drivers SET deleted = TRUE WHERE id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setLong(1, id);
            int affectedRows = deleteStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not delete driver with id : " + id, e);
        }
    }

    @Override
    public Optional<Driver> findByLogin(String login) {
        String selectQuery = "SELECT * FROM drivers WHERE login = ? AND deleted = FALSE;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createDriver(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get a driver with login: " + login, e);
        }
        return Optional.empty();
    }

    private Driver createDriver(ResultSet resultSet) {
        try {
            Long driverId = resultSet.getObject("id", Long.class);
            String driverName = resultSet.getObject("name", String.class);
            String licenseNumber = resultSet.getObject("license_number", String.class);
            String login = resultSet.getObject("login", String.class);
            String password = resultSet.getObject("password", String.class);
            Driver newDriver = new Driver(driverName, licenseNumber);
            newDriver.setId(driverId);
            newDriver.setLogin(login);
            newDriver.setPassword(password);
            return newDriver;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not create driver, can not parse data", e);
        }
    }
}
