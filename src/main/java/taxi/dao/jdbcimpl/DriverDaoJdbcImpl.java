package taxi.dao.jdbcimpl;

import taxi.dao.DriverDao;
import taxi.lib.Dao;
import taxi.model.Driver;

import java.util.List;
import java.util.Optional;

@Dao
public class DriverDaoJdbcImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        return null;
    }

    @Override
    public Optional<Driver> get(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Driver> getAll() {
        return null;
    }

    @Override
    public Driver update(Driver driver) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        //Just delete the driver from the table cars_drivers
        return false;
    }
}
