package demo.lowcode.engine.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class JavaDynamicCompiler {
    public static Class<?> compileAndLoadEventFile(String javaFilePath) throws Exception {
        File javaFile = new File(javaFilePath);
        String className = javaFile.getName().replace(".java", "");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Cannot find the system Java compiler. Check that your class path includes tools.jar");
        }

        Path outputDirectory = Paths.get("src/main/java/");
        // 设置编译选项
        String[] compileOptions = new String[]{
                "-d", outputDirectory.toString(),
                "-encoding", "UTF-8",
                javaFile.getAbsolutePath()
        };
        int result = compiler.run(null, null, null, compileOptions);
        if (result != 0) {
            throw new IllegalArgumentException("Compilation failed. Check the Java file.Error Code: "+result);
        }

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{outputDirectory.toUri().toURL()});
        return Class.forName("demo.lowcode.platform.eventhandler.classes." + className, true, classLoader);
    }

    public static void generateDeviceServiceFile(String packageName, String deviceType, String className, Map<String, String> operations) throws Exception {
        String directory = "src/main/java/" + packageName.replace('.', '/');
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File oldJavaFile = new File("src/main/java/" + packageName.replace('.', '/'), className+".java");
        File oldClassFile = new File("src/main/java/" + packageName.replace('.', '/'), className+".class");
        if (oldJavaFile.exists()) {
            oldJavaFile.delete();
        }
        if (oldClassFile.exists()) {
            oldClassFile.delete();
        }

        File javaFile = new File(dir, className + ".java");
        FileWriter writer = null;
        try {
            writer = new FileWriter(javaFile);
            writer.write("package " + packageName + ";\n");
            writer.write("import demo.lowcode.device." + packageName.split("\\.")[3] + ".service."+deviceType+"Service;\n");
            writer.write("public class " + className + " extends "+deviceType+"Service {\n");
            writer.write("    private String uri;\n");
            writer.write("    public " + className + "(String uri) { this.uri = uri; }\n");
            // 根据父类的方法生成覆盖的方法
            for (Map.Entry<String, String> entry : operations.entrySet()) {
                try {
                    Class<?> parentClass = Class.forName("demo.lowcode.device." + packageName.split("\\.")[3] + ".service."+deviceType+"Service");
                    Method[] methods = parentClass.getMethods();
                    Method method = null;
                    for (Method m: methods) {
                        if (m.getName().equals(entry.getKey())) {
                            method = m;
                        }
                    }

                    writer.write("    @Override\n");
                    assert method != null;
                    writer.write("    public " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");

                    // 生成方法参数
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (i > 0) {
                            writer.write(", ");
                        }
                        writer.write(parameterTypes[i].getSimpleName() + " param" + i);
                    }

                    writer.write(") {\n");
                    writer.write("        System.out.println(\"Accessing URI: \" + uri + \"" + entry.getValue() + "\");\n");

                    for (int i = 0; i < parameterTypes.length; i++) {
                        writer.write("        System.out.println(\"Param"+i+": \" + param"+i+");\n");
                    }

                    if (!method.getReturnType().equals(Void.TYPE)) {
                        writer.write("        return null;\n"); // 根据具体返回类型处理
                    }
                    writer.write("    }\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            writer.write("}\n");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static Class<?> compileJavaSourceFile(String packageName, String className) throws Exception{
        String fileName = "src/main/java/" + packageName.replace('.', '/') + "/" + className + ".java";

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Cannot find the system Java compiler. Check that your class path includes tools.jar");
        }

        Path outputDirectory = Paths.get("src/main/java/");
        // 设置编译选项
        String[] compileOptions = new String[]{
                "-d", outputDirectory.toString(),
                "-encoding", "UTF-8",
               fileName
        };
        int result = compiler.run(null, null, null, compileOptions);
        if (result != 0) {
            throw new IllegalArgumentException("Compilation failed. Check the Java file.Error Code: "+result);
        }

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{outputDirectory.toUri().toURL()});
        return classLoader.loadClass(packageName+ "." + className);
    }
}
