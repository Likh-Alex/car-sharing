package taxi.dao.impl;

import java.util.List;
import java.util.Optional;
import taxi.dao.ManufacturerDao;
import taxi.db.Storage;
import taxi.exception.NoSuchManufacturerException;
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
                .filter(man -> man.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Manufacturer> getAll() {
        return Storage.manufacturers;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        Optional<Manufacturer> targetManufacturer = get(manufacturer.getId());
        for (Manufacturer storageManufacturer : Storage.manufacturers) {
            if (targetManufacturer.isPresent()) {
                if (storageManufacturer.getId().equals(targetManufacturer.get().getId())) {
                    storageManufacturer.setCountry(manufacturer.getCountry());
                    storageManufacturer.setName(manufacturer.getName());
                }
            }
        }
        return targetManufacturer.orElseThrow(()
                -> new NoSuchManufacturerException("No manufacturer with name: "
                + manufacturer.getName()));
    }

    @Override
    public boolean delete(Long id) {
        Optional<Manufacturer> manufacturer = get(id);
        if (manufacturer.isPresent()) {
            Storage.manufacturers.remove(manufacturer.get());
            return true;
        }
        return false;
    }
}
