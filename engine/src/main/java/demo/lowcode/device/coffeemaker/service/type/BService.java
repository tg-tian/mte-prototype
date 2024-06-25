package demo.lowcode.device.coffeemaker.service.type;

import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BService extends CoffeeMakerService {
    private Class<?> BClass;
    private Object coffeeMakerB;

    public BService() {
        try {
            File jarDir = new File("libs");
            List<URL> jarUrls = new ArrayList<>();

            String jarName = "coffeeMakerB-0.0.1.jar";

            if (jarDir.exists() && jarDir.isDirectory()) {
                for (File file : Objects.requireNonNull(jarDir.listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".jar") && file.getName().equals(jarName)) {
                        jarUrls.add(file.toURI().toURL());
                        break;
                    }
                }
            }

            if (jarUrls.isEmpty()) {
                System.out.println("在libs目录中未找到coffeeMakerB-0.0.1.jar文件。");
                return;
            }

            URLClassLoader urlClassLoader = new URLClassLoader(jarUrls.toArray(new URL[0]), this.getClass().getClassLoader());

            this.BClass = urlClassLoader.loadClass("demo.device.coffeemaker.CoffeeMakerB");
            this.coffeeMakerB = this.BClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            BClass.getDeclaredMethod("start").invoke(coffeeMakerB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeCoffee() {
        try {
            BClass.getDeclaredMethod("makeCoffee").invoke(coffeeMakerB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void check(Object params) {
        try {
            BClass.getDeclaredMethod("check").invoke(coffeeMakerB);
            System.out.println("Checking code is "+params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
