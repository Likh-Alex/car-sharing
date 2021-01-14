package taxi.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import taxi.dao.ManufacturerDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

@Dao
public class ManufacturerDaoJdbcImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String query = "INSERT INTO manufacturers "
                + "(manufacturer_name, manufacturer_country)"
                + " VALUES (?, ?)";

        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            String name = manufacturer.getName();
            String country = manufacturer.getCountry();
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, country);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                manufacturer.setId(resultSet.getObject(1, Long.class));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not add new manufacturer"
                    + manufacturer.getName(), e);
        }
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        String query = "SELECT * FROM manufacturers "
                + "WHERE manufacturer_id = ? "
                + "AND deleted = FALSE;";

        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Optional<Manufacturer> newManufacturer = Optional
                        .of(createManufacturer(resultSet));
                preparedStatement.close();
                resultSet.close();
                return newManufacturer;
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find manufacturer with id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Manufacturer> getAll() {
        String query = "SELECT * FROM manufacturers WHERE deleted = FALSE;";
        List<Manufacturer> manufacturerList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                manufacturerList.add(createManufacturer(resultSet));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new DataProcessingException("No manufacturers found", e);
        }
        return manufacturerList;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String query = "UPDATE manufacturers"
                + " SET manufacturer_name = ?, manufacturer_country = ?"
                + " WHERE manufacturer_id = ?"
                + " AND deleted = FALSE;";

        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.setLong(3, manufacturer.getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                manufacturer = createManufacturer(resultSet);
                preparedStatement.close();
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find manufacturer with id: "
                    + manufacturer.getId(), e);
        }
        return manufacturer;
    }

    private Manufacturer createManufacturer(ResultSet resultSet) throws SQLException {
        try {
            long manufacturerId = resultSet.getObject("manufacturer_id", Long.class);
            String manufacturerCountry = resultSet.getObject("manufacturer_country", String.class);
            String manufacturerName = resultSet.getObject("manufacturer_name", String.class);
            Manufacturer manufacturer = new Manufacturer(manufacturerName, manufacturerCountry);
            manufacturer.setId(manufacturerId);
            return manufacturer;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not parse data"
                    + " from manufacturer with id: "
                    + resultSet.getObject("manufacturer_id", Long.class), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE manufacturers"
                + " SET deleted = TRUE"
                + " WHERE manufacturer_id = ? ";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(query);
            preparedStatement.setLong(1, id);
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not find manufacturer with id: " + id, e);
        }
    }
}
