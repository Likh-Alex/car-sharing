package taxi.db;

import java.util.ArrayList;
import java.util.List;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;

public class Storage {
    public static final List<Car> cars = new ArrayList<>();
    public static final List<Driver> drivers = new ArrayList<>();
    public static final List<Manufacturer> manufacturers = new ArrayList<>();
    private static Long carId = 0L;
    private static Long driverId = 0L;
    private static Long manufacturerId = 0L;

    public static void addCar(Car car) {
        car.setId(++carId);
        cars.add(car);
    }

    public static void addDriver(Driver driver) {
        driver.setId(++driverId);
        drivers.add(driver);
    }

    public static void addManufacturer(Manufacturer manufacturer) {
        manufacturer.setId(++manufacturerId);
        manufacturers.add(manufacturer);
    }
}
