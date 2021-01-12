package taxi.dao.impl;

import java.util.List;
import java.util.Optional;
import taxi.dao.ManufacturerDao;
import taxi.db.Storage;
import taxi.lib.Dao;
import taxi.model.Manufacturer;

@Dao
public class ManufacturerDaoImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        Storage.addManufacturer(manufacturer);
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        return Storage.manufacturers
                .stream()
                .filter(m -> m.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Manufacturer> getAll() {
        return Storage.manufacturers;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        for (int i = 0; i < Storage.manufacturers.size(); i++) {
            Manufacturer storageManufacturer = Storage.manufacturers.get(i);
            if (manufacturer.getId().equals(storageManufacturer.getId())) {
                Storage.manufacturers.set(i, manufacturer);
            }
        }
        return manufacturer;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.manufacturers.removeIf(m -> m.getId().equals(id));
    }
}
