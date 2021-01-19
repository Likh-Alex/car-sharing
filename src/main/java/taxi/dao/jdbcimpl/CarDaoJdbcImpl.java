package taxi.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import taxi.dao.CarDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

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
            if (resultSet.next() && resultSet.getObject("GENERATED_KEY", Long.class) != null) {
                car.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not add new car: "
                    + car.toString(), e);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        String selectQuery = "SELECT c.id AS car_id, model, "
                + "m.name AS manufacturer_name,"
                + "m.country AS manufacturer_country, "
                + "m.id AS manufacturer_id "
                + "FROM cars c "
                + "INNER JOIN manufacturers m ON c.manufacturer_id = m.id "
                + "WHERE c.id = ? AND c.deleted = FALSE;";
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                car = createCar(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find car with id: " + id, e);
        }
        if (car != null) {
            car.setDrivers(getAllDriversByCarId(id));
        }
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> getAll() {
        List<Car> carList = new ArrayList<>();
        String getAllQuery = "SELECT c.id AS car_id, model, "
                + "m.name AS manufacturer_name, "
                + "m.country AS manufacturer_country, "
                + "m.id AS manufacturer_id "
                + "FROM cars c "
                + "INNER JOIN manufacturers m ON m.id = c.manufacturer_id "
                + "WHERE c.deleted = FALSE;";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(getAllQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                carList.add(createCar(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find any drivers", e);
        }
        for (Car car: carList) {
            car.setDrivers(getAllDriversByCarId(car.getId()));
        }
        return carList;
    }

    private Car createCar(ResultSet resultSet) {
        try {
            Long carId = resultSet.getObject("car_id", Long.class);
            String carModel = resultSet.getObject("model", String.class);
            String manufacturerName = resultSet.getObject("manufacturer_name", String.class);
            String manufacturerCountry = resultSet.getObject("manufacturer_country", String.class);
            Long manufacturerId = resultSet.getObject("manufacturer_id", Long.class);
            Manufacturer manufacturer = new Manufacturer(manufacturerName, manufacturerCountry);
            manufacturer.setId(manufacturerId);
            Car newCar = new Car(carModel, manufacturer);
            newCar.setId(carId);
            return newCar;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not create a 'car', "
                    + "can not parse data", e);
        }
    }

    private List<Driver> getAllDriversByCarId(Long carId) {
        String selectQuery = "SELECT d.id, d.name, d.license_number "
                + "FROM cars_drivers cd "
                + "JOIN drivers d ON d.id = cd.driver_id "
                + "WHERE car_id = ? AND d.deleted = FALSE;";
        List<Driver> driverList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, carId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                driverList.add(createDriver(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find drivers for car with id: " + carId, e);
        }
        return driverList;
    }

    private Driver createDriver(ResultSet resultSet) {
        try {
            Driver newDriver = new Driver(resultSet.getObject("name", String.class),
                    resultSet.getObject("license_number", String.class));
            newDriver.setId(resultSet.getObject("id", Long.class));
            return newDriver;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not create a 'driver', "
                    + "can not parse data", e);
        }
    }

    @Override
    public Car update(Car car) {
        String updateQuery = "UPDATE cars c SET model = ?, "
                + "manufacturer_id = ? WHERE c.id = ? AND c.deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, car.getModel());
            updateStatement.setLong(2, car.getManufacturer().getId());
            updateStatement.setLong(3, car.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not update car: "
                    + car.toString(), e);
        }
        deleteDriversFromCar(car);
        insertDriversIntoCar(car);
        return car;
    }

    private void deleteDriversFromCar(Car car) {
        String deleteQuery = "DELETE FROM cars_drivers WHERE car_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setLong(1, car.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not delete drivers from car: "
                    + car.toString(), e);
        }
    }

    private void insertDriversIntoCar(Car car) {
        String insertQuery = "INSERT INTO cars_drivers (driver_id, car_id) VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setLong(2, car.getId());
            for (Driver driver : car.getDrivers()) {
                insertStatement.setLong(1, driver.getId());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not link car: "
                    + car.toString(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String deleteQuery = "UPDATE cars SET deleted = TRUE WHERE id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setLong(1, id);
            int affectedRows = deleteStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not delete car with id: " + id, e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String selectQuery = "SELECT c.id AS car_id, model, "
                + "m.name AS manufacturer_name, "
                + "m.country AS manufacturer_country, "
                + "m.id AS manufacturer_id "
                + "FROM cars_drivers "
                + "JOIN cars c ON c.id = cars_drivers.car_id "
                + "JOIN drivers d ON d.id = cars_drivers.driver_id "
                + "JOIN manufacturers m ON m.id = c.manufacturer_id "
                + "WHERE c.deleted = FALSE AND d.deleted = FALSE "
                + "AND d.id = ?;";
        List<Car> carList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                carList.add(createCar(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find cars for driver "
                    + "with id: " + driverId, e);
        }
        for (Car car: carList) {
            car.setDrivers(getAllDriversByCarId(car.getId()));
        }
        return carList;
    }
}
