package taxi.dao.jdbcimpl;

import taxi.dao.CarDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Car;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class CarDaoJdbcImpl implements CarDao {
    @Override
    public Car create(Car car) {
        String insertQuery = "INSERT INTO cars (car_name) VALUES (?);";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                car.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }

        } catch (SQLException e) {
            throw new DataProcessingException("Can not add new manufacturer "
                    + car.getModel(), e);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        String selectQuery = "SELECT c.id AS car_id, model, m.manufacturer_name "
                + "AS manufacturer_name, m.manufacturer_country "
                + "AS manufacturer_name, m.id AS manufacturer_id "
                + "FROM cars c "
                + "INNER JOIN manufacturers m ON c.id = m.id "
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
        return null;
    }

    private Car createCar(ResultSet resultSet) {
        try {
            Long carId = resultSet.getObject("car_id", Long.class);
            String carModel = resultSet.getObject("model", String.class);
            String manufacturerName = resultSet.getObject("manufacturer_name", String.class);
            String manufacturerCountry = resultSet.getObject("manufacturer_name", String.class);
            Long manufacturersId = resultSet.getObject("manufacturer_id", Long.class);
            Manufacturer manufacturer = new Manufacturer(manufacturerName, manufacturerCountry);
            manufacturer.setId(manufacturersId);
            Car newCar = new Car(carModel, manufacturer);
            newCar.setId(carId);
            return newCar;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not create a 'car',"
                    + " can not parse data", e);
        }
    }

    @Override
    public Car update(Car car) {
        String updateQuery = "UPDATE cars SET model = ? WHERE id = ? AND deleted = 'FALSE';";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find car with id: " + car.getId(), e);
        }
        return car;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String selectQuery = "SELECT * FROM cars_drivers " +
                "LEFT JOIN cars c ON c.id = cars_drivers.car_id " +
                "LEFT JOIN drivers d ON d.id = cars_drivers.driver_id " +
                "c.deleted = 'FALSE' AND d.deleted = 'FALSE' AND d.id = ?;";
        List<Car> carList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)){
        preparedStatement.setLong(1,driverId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            carList.add(createCar(resultSet));
        }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find cars for driver"
                    + " with id: " + driverId, e);
        }
        return null;
    }
}
