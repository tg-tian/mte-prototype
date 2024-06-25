package demo.lowcode.device.coffeemaker.service.type;

import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AService extends CoffeeMakerService {
    private Class<?> AClass;
    private Object coffeeMakerA;

    public AService() {
        try {
            File jarDir = new File("libs");
            List<URL> jarUrls = new ArrayList<>();

            String jarName = "coffeeMakerA-0.0.1.jar";

            if (jarDir.exists() && jarDir.isDirectory()) {
                for (File file : Objects.requireNonNull(jarDir.listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".jar") && file.getName().equals(jarName)) {
                        jarUrls.add(file.toURI().toURL());
                        break;
                    }
                }
            }

            if (jarUrls.isEmpty()) {
                System.out.println("在libs目录中未找到coffeeMakerA-0.0.1.jar文件。");
                return;
            }

            URLClassLoader urlClassLoader = new URLClassLoader(jarUrls.toArray(new URL[0]), this.getClass().getClassLoader());

            this.AClass = urlClassLoader.loadClass("demo.device.coffeemaker.CoffeeMakerA");
            this.coffeeMakerA = this.AClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            AClass.getDeclaredMethod("on").invoke(coffeeMakerA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeCoffee() {
        try {
            AClass.getDeclaredMethod("makeCoffee").invoke(coffeeMakerA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void check(Object params) {
        throw new RuntimeException("NoSupportedOperation");
    }
}
