import taxi.lib.Injector;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.service.CarService;
import taxi.service.DriverService;
import taxi.service.ManufacturerService;

import java.util.List;

public class Application {
    private static final Injector injector = Injector.getInstance("taxi");

    public static void main(String[] args) {
        final ManufacturerService manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);
        final CarService carService = (CarService) injector
                .getInstance(CarService.class);
        final DriverService driverService = (DriverService) injector
                .getInstance(DriverService.class);

        Manufacturer toyotaMaker = new Manufacturer("NotReallyToyota", "NotReallyJapan");
        manufacturerService.create(toyotaMaker);
        Car toyotaCar = new Car("celica",toyotaMaker);
        System.out.println("CREATE");
        System.out.println(carService.create(toyotaCar));
        System.out.println("GET");
        System.out.println(carService.get(toyotaCar.getId()));
        Manufacturer kiaManufacturer = new Manufacturer("kia", "korea");
        manufacturerService.create(kiaManufacturer);
        toyotaCar.setManufacturer(kiaManufacturer);
        System.out.println(carService.update(toyotaCar));
        System.out.println(carService.delete(toyotaCar.getId()));
        System.out.println(carService.delete(toyotaCar.getId()));
        System.out.println(carService.get(toyotaCar.getId()));
//          List<Car> allByDriver = carService.getAllByDriver(1L);
//        for (Car car: allByDriver ) {
//            System.out.println(car.toString());
//        }

//        for (Car car : carService.getAll()) {
//            System.out.println(car.getModel() + " I AM A CAR");
//            for (Driver driver: car.getDrivers()) {
//                System.out.println(driver.getName() + " DRIVER");
//            }
//        }


        /*
        Manufacturer toyotaMaker = new Manufacturer("Toyota", "Japan");
        System.out.println(manufacturerService.create(toyotaMaker));
        System.out.println(manufacturerService.get(1L));
        toyotaMaker.setName("Machindra");
        toyotaMaker.setCountry("India");
        System.out.println(manufacturerService.update(toyotaMaker));
        System.out.println(manufacturerService.get(toyotaMaker.getId()));
        System.out.println(manufacturerService.delete(1L));

        Manufacturer volvoMaker = new Manufacturer("Volvo", "Sweden");
        Manufacturer fiatMaker = new Manufacturer("Fiat", "Italy");
        Manufacturer opelMaker = new Manufacturer("Opel", "Germany");
        Manufacturer tavriaMaker = new Manufacturer("Tavria", "Ukraine");
        Manufacturer ladaMaker = new Manufacturer("Lada", "Russia");
        manufacturerService.create(volvoMaker);
        manufacturerService.create(fiatMaker);
        manufacturerService.create(opelMaker);
        manufacturerService.create(tavriaMaker);
        manufacturerService.create(ladaMaker);
        System.out.println(manufacturerService.getAll().size());
        manufacturerService.delete(volvoMaker.getId());
        System.out.println(manufacturerService.getAll().size());
        manufacturerService.delete(fiatMaker.getId());
        System.out.println(manufacturerService.getAll().size());

        //Driver service
        Driver opelDriver = new Driver("Hans", "einzwei");
        Driver additionalDriver = new Driver("Stepan", "razdvatri");
        driverService.create(opelDriver);
        driverService.create(additionalDriver);
        System.out.println(driverService.getAll());
        additionalDriver.setName("Mykola");
        driverService.update(additionalDriver);
        System.out.println(additionalDriver.toString());

        //Car service
        Car fiatCar = new Car("500", fiatMaker);
        Car opelCar = new Car("Blitz", fiatMaker);
        Car tavriaCar = new Car("Slavuta", tavriaMaker);
        Car ladaCar = new Car("Granta", ladaMaker);
        final Manufacturer fordMaker = new Manufacturer("Ford", "USA");
        carService.create(fiatCar);
        carService.create(opelCar);
        carService.create(tavriaCar);
        carService.create(ladaCar);
        System.out.println(carService.get(1L).toString());
        System.out.println(carService.get(2L).toString());
        System.out.println(carService.get(3L).toString());
        fiatCar.setManufacturer(fordMaker);
        carService.update(fiatCar);
        System.out.println(fiatMaker.toString());
        System.out.println(carService.delete(1L));
        System.out.println(carService.delete(1L));
        System.out.println(carService.getAll());

        //Car service addDriverToCar
        carService.addDriverToCar(opelDriver, opelCar);
        System.out.println("Adding driver to Opel");
        System.out.println(opelCar.toString());

        //Car service removeDriverFromCar
        carService.removeDriverFromCar(opelDriver, opelCar);
        System.out.println("Removing driver from Opel");
        System.out.println(opelCar.toString());

        //Car service getAllCarsByDriver
        Driver manyCarsDriver = new Driver("Vasiliy", "123");
        driverService.create(manyCarsDriver);
        System.out.println(manyCarsDriver.toString());
        carService.addDriverToCar(manyCarsDriver, opelCar);
        carService.addDriverToCar(manyCarsDriver, tavriaCar);
        carService.addDriverToCar(manyCarsDriver, ladaCar);
        System.out.println(carService.getAllByDriver(3L));

         */
    }
}
