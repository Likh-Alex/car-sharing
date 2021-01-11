import taxi.lib.Injector;
import taxi.model.Manufacturer;
import taxi.service.ManufacturerService;

public class Application {
    private static final Injector injector = Injector.getInstance("taxi");

    public static void main(String[] args) {
        ManufacturerService manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);

        Manufacturer volvo = new Manufacturer("Volvo", "Sweden");
        Manufacturer fiat = new Manufacturer("Fiat", "Italy");
        Manufacturer opel = new Manufacturer("Opel", "Germany");

        manufacturerService.create(volvo);
        manufacturerService.create(fiat);
        manufacturerService.create(opel);

        volvo.setName("Toyota");
        volvo.setCountry("Japan");
        manufacturerService.update(volvo);

        manufacturerService.delete(volvo.getId());
        manufacturerService.delete(volvo.getId());
    }
}
