package taxi.dao.impl;

import java.util.List;
import java.util.Optional;
import taxi.dao.CarDao;
import taxi.db.Storage;
import taxi.lib.Dao;
import taxi.model.Car;

@Dao
public class CarDaoImpl implements CarDao {
    @Override
    public Car create(Car car) {
        Storage.addCar(car);
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        return Storage.cars
                .stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Car> getAll() {
        return Storage.cars;
    }

    @Override
    public Car update(Car car) {
        for (int i = 0; i < Storage.cars.size(); i++) {
            Car storageCar = Storage.cars.get(i);
            if (car.getId().equals(storageCar.getId())) {
                Storage.cars.set(i, car);
            }
        }
        return car;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.cars.removeIf(c -> c.getId().equals(id));
    }
}
