package demo.lowcode.common;

import lombok.Data;

@Data
public class CommonConfig {
    // maven目录
    private static String mavenPath = "/usr/tool/apache-maven-3.6.3/bin/mvn.cmd";
    // 设备打包临时目录
    private static String projectPath = "/usr/tool/workplace/temp/";
    // 工作区目录
    private static String workspacePath = "/usr/tool/workplace/";
    // 设备定义工作目录
    private static String definitionPath = "/usr/tool/workplace/deviceType/";

    public static void setMavenPath(String mavenPath) {
        CommonConfig.mavenPath = mavenPath;
    }

    public static String getMavenPath() {
        return mavenPath;
    }

    public static void setProjectPath(String projectPath) {
        CommonConfig.projectPath = projectPath;
    }

    public static String getProjectPath() {
        return projectPath;
    }

    public static String getDefinitionPath() {
        return definitionPath;
    }

    public static void setDefinitionPath(String definitionPath) {
        CommonConfig.definitionPath = definitionPath;
    }

    public static void setWorkspacePath(String workspacePath) {
        CommonConfig.workspacePath = workspacePath;
    }

    public static String getWorkspacePath() {
        return workspacePath;
    }
}
