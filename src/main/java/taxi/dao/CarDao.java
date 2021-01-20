package taxi.dao;

import java.util.List;
import taxi.model.Car;

public interface CarDao extends GenericDao<Car, Long> {
    List<Car> getAllByDriver(Long driverId);
}
