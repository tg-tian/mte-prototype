package lowcode.device.generator.core;

import demo.lowcode.common.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lowcode.device.generator.GeneratorConfig;

import java.io.File;

@Data
@AllArgsConstructor
public class ProjectGenerator {
    private String parentPath;
    private String groupId;
    private String artifactId;
    private String version;

    public void generate(){
        // 创建项目的根目录
        File projectDir = new File(parentPath+artifactId);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }

        // 创建目录结构
        createDirectories(projectDir);

        // 创建 pom.xml 文件
        createPomFile(projectDir);

        System.out.println("Maven 项目创建完成！");
    }

    private void createDirectories(File projectDir) {
        String baseDir = projectDir.getPath() + "/src/main/java/" + groupId.replace(".", "/");
        new File(baseDir).mkdirs();
        new File(projectDir.getPath() + "/src/test/java/" + groupId.replace(".", "/")).mkdirs();
    }

    private void createPomFile(File projectDir) {
        String pomContent =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>3.3.3</version>\n" +
                        "        <relativePath/> <!-- lookup parent from repository -->\n" +
                        "    </parent>\n" +
                        "\n" +
                        "    <groupId>" + groupId + "</groupId>\n" +
                        "    <artifactId>" + artifactId + "</artifactId>\n" +
                        "    <version>" + version + "</version>\n" +
                        "\n" +
                        "    <properties>\n" +
                        "        <java.version>17</java.version>\n" +
                        "    </properties>\n" +
                        "\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter</artifactId>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.projectlombok</groupId>\n" +
                        "            <artifactId>lombok</artifactId>\n" +
                        "            <optional>true</optional>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                        "            <scope>test</scope>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>demo.lowcode</groupId>\n" +
                        "            <artifactId>common</artifactId>\n" +
                        "            <version>0.0.1-SNAPSHOT</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "           <plugin>\n" +
                        "            <groupId>org.apache.maven.plugins</groupId>\n" +
                        "            <artifactId>maven-jar-plugin</artifactId>\n" +
                        "            <version>3.2.0</version>\n" +
                        "            <configuration>\n" +
                        "                <archive>\n" +
                        "                    <manifest>\n" +
                        "                        <addDefaultImplementationEntries>true</addDefaultImplementationEntries>\n" +
                        "                    </manifest>\n" +
                        "                </archive>\n" +
                        "            </configuration>\n" +
                        "           </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        FileUtil.writeFile(projectDir+"/pom.xml", pomContent);
    }

    public void buildAndPackage(File projectDir) {
        try {

            // 执行 Maven 命令编译并打包项目
            ProcessBuilder builder = new ProcessBuilder(
                    GeneratorConfig.getMavenPath(), "clean", "package"
            );
            builder.directory(projectDir); // 设置工作目录为项目根目录

            builder.inheritIO(); // 继承 I/O，打印构建输出到控制台
            Process process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
