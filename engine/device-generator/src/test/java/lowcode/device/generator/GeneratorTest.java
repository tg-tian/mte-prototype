package lowcode.device.generator;

import lowcode.device.generator.core.DeviceGenerator;
import lowcode.device.generator.core.EventGenerator;
import lowcode.device.generator.core.ProjectGenerator;
import lowcode.device.generator.core.ServiceGenerator;
import org.junit.jupiter.api.Test;

public class GeneratorTest {
    @Test
    public void generateEventFile(){
        EventGenerator generator = new EventGenerator();

        generator.generateEventFile("demo.lowcode.device.coffeemaker","D:\\projects\\ubml\\mte-prototype\\engine\\workplace\\SmartBuilding\\floor1\\CoffeeMaker\\","D:\\projects\\ubml\\mte-prototype\\engine\\workplace\\SmartBuilding\\floor1\\CoffeeMaker\\definitions\\events\\MakeCoffeeEvent.json");
    }

    @Test
    public void generateServiceFile(){
        ServiceGenerator generator = new ServiceGenerator();

        generator.generateServiceFile("demo.lowcode.device.coffeemaker","CoffeeMaker","D:\\projects\\ubml\\mte-prototype\\engine\\workplace\\SmartBuilding\\floor1\\CoffeeMaker\\","definitions\\services\\AService.json");
    }

    @Test
    public void generateProject(){
        ProjectGenerator generator = new ProjectGenerator("D:\\projects\\ubml\\mte-prototype\\engine\\","demo.lowcode.device", "coffeemaker", "1.0.0");
        generator.generate();
    }

    @Test
    public void generateDevice(){
        DeviceGenerator generator = new DeviceGenerator("CoffeeMaker", "D:/projects/ubml/mte-prototype/engine/workplace/SmartBuilding/floor1/CoffeeMaker/");
//        generator.generate();
        System.out.println("项目生成完成，准备打包...");
        generator.buildAndPackage();
    }
}
