package taxi.dao.jdbcimpl;

import taxi.dao.CarDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class CarDaoJdbcImpl implements CarDao {
    @Override
    public Car create(Car car) {
        String insertQuery = "INSERT INTO cars (model, manufacturer_id) VALUES (?, ?);";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setLong(2, car.getManufacturer().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                car.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not add new manufacturer "
                    + car.getModel(), e);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        String selectQuery = "SELECT c.id AS car_id, model, "
                + "m.manufacturer_name AS manufacturer_name,"
                + "m.manufacturer_country AS manufacturer_country, "
                + "m.id AS manufacturer_id "
                + "FROM cars c "
                + "INNER JOIN manufacturers m ON c.manufacturer_id = m.id "
                + "WHERE c.id = ? AND c.deleted = 'FALSE';";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createCar(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find manufacturer with id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Car> getAll() {
        List<Car> carList = new ArrayList<>();
        String getAllQuery = "SELECT DISTINCT car_id, model, " +
                "m.manufacturer_name AS manufacturer_name, " +
                "m.manufacturer_country AS manufacturer_country, " +
                "m.id AS manufacturer_id " +
                "from cars_drivers " +
                "LEFT JOIN cars c ON cars_drivers.car_id = c.id " +
                "INNER JOIN manufacturers m ON m.id = c.manufacturer_id " +
                "WHERE c.deleted = 'FALSE';";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAllQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                carList.add(createCar(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find any drivers", e);
        }
        return carList;
    }

    private Car createCar(ResultSet resultSet) {
        try {
            Long carId = resultSet.getObject("car_id", Long.class);
            String carModel = resultSet.getObject("model", String.class);
            String manufacturerName = resultSet.getObject("manufacturer_name", String.class);
            String manufacturerCountry = resultSet.getObject("manufacturer_country", String.class);
            Long manufacturersId = resultSet.getObject("manufacturer_id", Long.class);
            Manufacturer manufacturer = new Manufacturer(manufacturerName, manufacturerCountry);
            manufacturer.setId(manufacturersId);
            Car newCar = new Car(carModel, manufacturer);
            newCar.setDrivers(getAllDriversByCarId(carId));
            newCar.setId(carId);
            return newCar;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not create a 'car',"
                    + " can not parse data", e);
        }
    }

    private List<Driver> getAllDriversByCarId(Long carId) {
        String selectQuery = "SELECT d.id, d.driver_name, d.license_number " +
                "FROM cars_drivers " +
                "LEFT JOIN drivers d ON d.id = cars_drivers.driver_id " +
                "LEFT JOIN cars c ON c.id = cars_drivers.car_id " +
                "WHERE car_id = ? AND d.deleted = 'FALSE';";
        List<Driver> driverList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, carId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Driver newDriver = new Driver(
                        resultSet.getObject("driver_name", String.class),
                        resultSet.getObject("license_number", String.class));
                newDriver.setId(resultSet.getObject("id", Long.class));
                driverList.add(newDriver);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find drivers for car with id: " + carId, e);
        }
        return driverList;
    }

    @Override
    public Car update(Car car) {
        String updateQuery = "UPDATE cars SET model = ?, " +
                "manufacturer_id = ? " +
                "WHERE cars.id = ? AND cars.deleted = 'FALSE'";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setLong(2, car.getManufacturer().getId());
            preparedStatement.setLong(3, car.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not update car with id: " + car.getId(), e);
        }
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String deleteQuery = "UPDATE cars c"
                + " SET deleted = TRUE"
                + " WHERE c.id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows != 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not delete car with id: " + id, e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String selectQuery = "SELECT * " +
                "FROM cars_drivers " +
                "LEFT JOIN cars c ON c.id = cars_drivers.car_id " +
                "LEFT JOIN drivers d ON d.id = cars_drivers.driver_id " +
                "LEFT JOIN manufacturers m ON m.id = c.manufacturer_id " +
                "WHERE c.deleted = 'FALSE' AND d.deleted = 'FALSE' " +
                "AND d.id = ?;";
        List<Car> carList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                carList.add(createCar(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find cars for driver"
                    + " with id: " + driverId, e);
        }
        return carList;
    }
}
