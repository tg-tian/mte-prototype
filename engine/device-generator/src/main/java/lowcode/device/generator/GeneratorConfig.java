package lowcode.device.generator;

import lombok.Data;

@Data
public class GeneratorConfig {
    private static String mavenPath = "D:/apache-maven-3.6.3/bin/mvn.cmd";
    private static String projectPath = "D:/projects/ubml/mte-prototype/engine/";
    private static String definitionPath = "D:/projects/ubml/mte-prototype/engine/workplace/deviceType/";

    public static void setMavenPath(String mavenPath) {
        GeneratorConfig.mavenPath = mavenPath;
    }

    public static String getMavenPath() {
        return mavenPath;
    }

    public static void setProjectPath(String projectPath) {
        GeneratorConfig.projectPath = projectPath;
    }

    public static String getProjectPath() {
        return projectPath;
    }

    public static String getDefinitionPath() {
        return definitionPath;
    }

    public static void setDefinitionPath(String definitionPath) {
        GeneratorConfig.definitionPath = definitionPath;
    }
}
