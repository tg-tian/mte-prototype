package demo.lowcode.engine.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaDynamicCompiler {
    public static Class<?> compileAndLoad(String javaFilePath) throws Exception {
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
}
