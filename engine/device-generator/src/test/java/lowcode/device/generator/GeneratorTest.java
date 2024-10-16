package lowcode.device.generator;

import demo.lowcode.common.util.FileUtil;
import demo.lowcode.common.util.StringUtil;
import lowcode.device.generator.core.DeviceGenerator;
import lowcode.device.generator.core.EventGenerator;
import lowcode.device.generator.core.ProjectGenerator;
import lowcode.device.generator.core.ServiceGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GeneratorTest {

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
//        generator.generate();
//        System.out.println("项目生成完成，准备打包...");
        generator.buildAndPackage();

        generator.copyJarFile();
    }

    @Test
    public void generateEventJava() {// 模拟应用编辑阶段根据eve nt.json生成对应代码
        String deviceType = "CoffeeMaker";
        String devicePath = "D:/projects/ubml/mte-prototype/engine/workplace/SmartBuilding/BuildingA/device/";
        String jarPath = devicePath+deviceType+"/"+deviceType.toLowerCase()+"-1.0.0.jar";
        String eventPath = devicePath+deviceType+"/event/";
        List<String> operations = List.of("start", "makeCoffee", "check");
        for (String operation: operations){
            // 读取jar包中的json文件
            String jsonFile = "events/"+ StringUtil.capitalizeFirstLetter(operation)+"Event.json";
            JSONObject jsonObject = FileUtil.readJarJson(jarPath, jsonFile);
            // 根据json文件在对应文件夹生成对应java文件
            EventGenerator eventGenerator = new EventGenerator();
            String groupId = "lowcode.device.coffeemaker";
            eventGenerator.generateEventFile(groupId, eventPath, jsonObject);
        }
    }
}
