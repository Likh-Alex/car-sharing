package taxi.dao.impl;

import java.util.List;
import java.util.Optional;
import taxi.dao.DriverDao;
import taxi.db.Storage;
import taxi.lib.Dao;
import taxi.model.Driver;

@Dao
public class DriverDaoImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        Storage.addDriver(driver);
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        return Storage.drivers
                .stream()
                .filter(d -> d.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Driver> getAll() {
        return Storage.drivers;
    }

    @Override
    public Driver update(Driver driver) {
        for (int i = 0; i < Storage.drivers.size(); i++) {
            Driver storageDriver = Storage.drivers.get(i);
            if (driver.getId().equals(storageDriver.getId())) {
                Storage.drivers.set(i, driver);
            }
        }
        return driver;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.drivers.removeIf(d -> d.getId().equals(id));
    }
}
