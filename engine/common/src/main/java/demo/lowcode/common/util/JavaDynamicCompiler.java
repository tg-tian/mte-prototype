package demo.lowcode.common.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaDynamicCompiler {
    public static URLClassLoader loadJar(String jarFilePath) throws Exception {
        File jarFile = new File(jarFilePath);
        URL jarURL = jarFile.toURI().toURL();

        return new URLClassLoader(new URL[] { jarURL }, JavaDynamicCompiler.class.getClassLoader());
    }

    public static URLClassLoader compileAndLoadEventFile(String jarPath, String javaFilePath, String outputDir, URLClassLoader jarClassLoader) throws Exception {
        File javaFile = new File(javaFilePath);
        String className = javaFile.getName().replace(".java", "");

        new File(outputDir).mkdirs();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Cannot find the system Java compiler. Check that your class path includes tools.jar");
        }

        Path outputDirectory = Paths.get(outputDir);

        // 获取当前项目的类路径
        String currentClassPath = System.getProperty("java.class.path");
        // 拼接当前类路径和 jar 包路径
        List<String> allClassPaths = new ArrayList<>();
        allClassPaths.add(currentClassPath);  // 添加当前项目的类路径
        allClassPaths.add(jarPath);       // 添加外部 jar 包路径
        // 拼接所有的类路径
        String classpath = String.join(File.pathSeparator, allClassPaths);

        // 设置编译选项
        String[] compileOptions = new String[]{
                "-d", outputDirectory.toString(),
                "-classpath", classpath,
                "-encoding", "UTF-8",
                javaFile.getAbsolutePath()
        };
        int result = compiler.run(null, null, null, compileOptions);
        if (result != 0) {
            throw new IllegalArgumentException("Compilation failed. Check the Java file.Error Code: "+result);
        }

        // 创建 URL 列表，将输出目录和 jar 包路径都加入到 URLClassLoader 中
        List<URL> urls = new ArrayList<>();
        urls.add(outputDirectory.toUri().toURL());  // 添加编译输出目录
        File jarFile = new File(jarPath);
        urls.add(jarFile.toURI().toURL());      // 添加 jar 包路径

        // 创建一个新的 URLClassLoader，包含编译输出目录和依赖的 jar 包;使用jar类加载器作为父类加载器，避免重复加载
        return new URLClassLoader(urls.toArray(new URL[0]), jarClassLoader);
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
