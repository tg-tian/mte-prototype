package lowcode.device.generator;

import lowcode.device.generator.core.DeviceGenerator;
import lowcode.device.generator.core.EventGenerator;
import lowcode.device.generator.core.ProjectGenerator;
import lowcode.device.generator.core.ServiceGenerator;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GeneratorTest {
    @Test
    public void generateEventFile(){
        DeviceGenerator generator = new DeviceGenerator("CoffeeMaker", "D:/projects/ubml/mte-prototype/engine/workplace/deviceType/CoffeeMaker/");
//        generator.generateEventJson("D:/projects/ubml/mte-prototype/engine/workplace/SmartBuilding/floor1/device/CoffeeMaker/event/");
    }

    @Test
    public void generateServiceFile(){
        ServiceGenerator generator = new ServiceGenerator();

        generator.generateServiceFile("demo.lowcode.device.coffeemaker","CoffeeMaker","D:\\projects\\ubml\\mte-prototype\\engine\\workplace\\deviceType\\CoffeeMaker\\","definitions\\services\\AService.json");
    }

    @Test
    public void generateProject(){
        ProjectGenerator generator = new ProjectGenerator("D:\\projects\\ubml\\mte-prototype\\engine\\","demo.lowcode.device", "coffeemaker", "1.0.0");
        generator.generate();
    }

    @Test
    public void generateDevice() throws Exception {
        DeviceGenerator generator = new DeviceGenerator("CoffeeMaker", "D:/projects/ubml/mte-prototype/engine/workplace/deviceType/CoffeeMaker/");
        generator.generate();
        System.out.println("项目生成完成，准备打包...");
        generator.buildAndPackage();

        generator.copyJarFile();
    }

    @Test
    public void readJarJson() {
        String jarFilePath = "D:/projects/ubml/mte-prototype/engine/workplace/deviceType/CoffeeMaker/generate/coffeemaker-1.0.0.jar";
        String jsonFile = "events/MakeCoffeeEvent.json";

        try {
            // 打开 JAR 文件
            JarFile jarFile = new JarFile(jarFilePath);

            // 获取 JAR 包内的 JSON 文件条目
            JarEntry jarEntry = jarFile.getJarEntry(jsonFile);

            if (jarEntry != null) {
                // 打开文件输入流
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                // 逐行读取文件内容
                String line;
                StringBuilder jsonContent = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line).append("\n");
                }
                reader.close();

                // 打印读取的 JSON 文件内容
                System.out.println("JSON File Content:\n" + jsonContent.toString());
            } else {
                System.out.println("JSON file not found in the JAR file.");
            }

            // 关闭 JAR 文件
            jarFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
