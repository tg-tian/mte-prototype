package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import demo.lowcode.platform.common.CommonConfig;
import demo.lowcode.platform.dto.DomainTemInfo;
import demo.lowcode.platform.dto.NewScene;
import demo.lowcode.platform.dto.ScenePubInfo;
import demo.lowcode.platform.dto.SceneTemInfo;
import demo.lowcode.platform.entity.Domain;
import demo.lowcode.platform.entity.Scene;
import demo.lowcode.platform.mapper.DomainMapper;
import demo.lowcode.platform.mapper.SceneMapper;
import demo.lowcode.platform.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class SceneBusiness {

    private final SceneMapper sceneMapper;
    private final DomainMapper domainMapper;

    @Autowired
    public SceneBusiness(SceneMapper sceneMapper, DomainMapper domainMapper) {
        this.sceneMapper = sceneMapper;
        this.domainMapper = domainMapper;
    }

    public ScenarioJson addScenarioJson(String scenarioId, String scenarioName, String domainId, String mapPath, List<Map<String,String>> mapList)
    {
        ScenarioJson scenarioJson = new ScenarioJson();
        scenarioJson.setScenarioId(scenarioId);
        scenarioJson.setScenarioName(scenarioName);
        scenarioJson.setDomainId(domainId);
        scenarioJson.setMapPath(mapPath);
        scenarioJson.setMaplist(mapList);

        return scenarioJson;
    }
    //获取领域基本信息json
    public ScenarioJson loadScenarioJson() throws IOException{
        File file = new File(CommonConfig.getWorkspacePath()+"SmartBuilding/PhysicalBuilding/PhysicalBuilding.sce"); //获取文件夹
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        //JsonNode ScenarioNodeList = rootNode.path("");
        String scenarioId = rootNode.path("scenarioId").asText();
        String scenarioName = rootNode.path("scenarioName").asText();
        String domainId = rootNode.path("domainId").asText();

        JsonNode mapNodeList = rootNode.path("map");
        String mapPath = mapNodeList.path("mapPath").asText();
        JsonNode mapListNodeList = mapNodeList.path("mapList");

        List<Map<String,String>> mapList = new ArrayList<>();
        for(JsonNode mapListNode : mapListNodeList){
            //声明楼层字典存储楼层信息
            Map<String,String> map_imf = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = mapListNode.fields();
            while (fieldsIterator.hasNext()){
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                map_imf.put(field.getKey(), field.getValue().asText());
            }
            mapList.add(map_imf);
        }

        return addScenarioJson(scenarioId, scenarioName,domainId,mapPath,mapList);
    }

    public List<Scene> getSceneList(Long domainId){
        return sceneMapper.selectByDomainId(domainId);
    }
    
    /**
     * 获取所有场景列表
     * @return 所有场景列表
     */
    public List<Scene> getAllScenes(){
        return sceneMapper.selectAll();
    }

    public Scene getSceneById (Long sceneId){
        return sceneMapper.selectById(sceneId);
    }

    public Scene createScene(NewScene newScene){
        String code = newScene.getCode() == null ? "" : newScene.getCode().trim();
        String name = newScene.getName() == null ? "" : newScene.getName().trim();
        if (code.isEmpty()) {
            throw new IllegalArgumentException("场景编码不能为空");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("场景名称不能为空");
        }
        if (newScene.getDomainId() == null) {
            throw new IllegalArgumentException("领域ID不能为空");
        }
        Domain domain = domainMapper.selectById(newScene.getDomainId());
        if (domain == null) {
            throw new IllegalArgumentException("领域不存在");
        }

        Scene existingScene = sceneMapper.selectBySceneCode(code);
        if (existingScene != null) {
            throw new IllegalArgumentException("场景已存在");
        }

        Scene scene = new Scene();
        scene.setSceneCode(code);
        scene.setSceneName(name);
        scene.setSceneDescription(newScene.getDescription());
        scene.setStatus(normalizeStatus(newScene.getStatus()));
        scene.setDomainId(newScene.getDomainId());
        if(newScene.getLocation()!=null){
            scene.setLongitude(newScene.getLocation().getLng());
            scene.setLatitude(newScene.getLocation().getLat());
        }
        Date now = new Date();
        scene.setCreateTime(now);
        scene.setUpdateTime(now);
        scene.setUrl(newScene.getUrl());
        scene.setImageUrl(newScene.getImageUrl());
        sceneMapper.insert(scene);
        return scene;
    }

    public Scene changeScene(Long id, NewScene newScene){

        Scene existingScene = sceneMapper.selectById(id);
        if (existingScene == null) {
            throw new IllegalArgumentException("场景不存在");
        }

        String code = newScene.getCode() == null ? "" : newScene.getCode().trim();
        String name = newScene.getName() == null ? "" : newScene.getName().trim();
        if (code.isEmpty()) {
            throw new IllegalArgumentException("场景编码不能为空");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("场景名称不能为空");
        }

        Scene sceneWithSameCode = sceneMapper.selectBySceneCode(code);
        if (sceneWithSameCode != null && !Objects.equals(sceneWithSameCode.getSceneId(), id)) {
            throw new IllegalArgumentException("场景编码已存在");
        }

        existingScene.setSceneId(id);
        existingScene.setSceneCode(code);
        existingScene.setSceneName(name);
        existingScene.setSceneDescription(newScene.getDescription());
        existingScene.setStatus(normalizeStatus(newScene.getStatus()));
        existingScene.setUrl(newScene.getUrl());
        if(newScene.getLocation() != null){
            existingScene.setLongitude(newScene.getLocation().getLng());
            existingScene.setLatitude(newScene.getLocation().getLat());
        }
        if(newScene.getImageUrl() != null){
            existingScene.setImageUrl(newScene.getImageUrl());
        }
        if (newScene.getDomainId() != null) {
            Domain domain = domainMapper.selectById(newScene.getDomainId());
            if (domain == null) {
                throw new IllegalArgumentException("领域不存在");
            }
            existingScene.setDomainId(newScene.getDomainId());
        }
        existingScene.setUpdateTime(new Date());
        sceneMapper.updateById(existingScene);
        return existingScene;
    }

    public void deleteSceneByID(Long id){
        sceneMapper.deleteById(id);
    }

    public Object publishScene(ScenePubInfo pubInfo) {
        Scene existingScene = sceneMapper.selectById(pubInfo.getSceneId());
        if (existingScene == null) {
            throw new RuntimeException("场景不存在");
        }

        String normalizedStatus = normalizeStatus(pubInfo.getStatus());
        String targetUrl = pubInfo.getUrl() != null ? pubInfo.getUrl() : existingScene.getUrl();

        Object result = existingScene;

        if (Objects.equals(normalizedStatus, "1")){
            SceneTemInfo exportInfo = buildSceneExportInfo(existingScene, targetUrl);
            writeSceneInfo(exportInfo);
            result = exportInfo;
        } else {
            // Unpublish - delete scene configuration file
            deleteSceneInfo(existingScene.getSceneCode());
        }

        existingScene.setStatus(normalizedStatus);
        existingScene.setUrl(targetUrl);
        existingScene.setUpdateTime(new Date());
        sceneMapper.updateById(existingScene);

        return result;
    }

    private SceneTemInfo buildSceneExportInfo(Scene scene, String targetUrl) {
        SceneTemInfo exportInfo = new SceneTemInfo();
        
        NewScene sceneData = new NewScene();
        sceneData.setCode(scene.getSceneCode());
        sceneData.setName(scene.getSceneName());
        sceneData.setDescription(scene.getSceneDescription());
        sceneData.setStatus("1");
        sceneData.setUrl(targetUrl);
        sceneData.setDomainId(scene.getDomainId());
        if (scene.getLongitude() != null && scene.getLatitude() != null) {
            demo.lowcode.platform.dto.Location loc = new demo.lowcode.platform.dto.Location();
            loc.setLng(scene.getLongitude());
            loc.setLat(scene.getLatitude());
            sceneData.setLocation(loc);
        }
        sceneData.setImageUrl(scene.getImageUrl());
        exportInfo.setSceneData(sceneData);
        
        exportInfo.setDevices(new ArrayList<>());
        
        return exportInfo;
    }

    public void writeSceneInfo(SceneTemInfo temInfo){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // 格式化 JSON

            // 计算目标路径
            String projectRoot = System.getProperty("user.dir"); // 获取项目根目录
            String targetDir = Paths.get(projectRoot,  "template", "scene").toString();
            // 确保目录存在
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 创建所有不存在的父目录
            }

            File file = new File(targetDir, temInfo.getSceneData().getCode()+".json");

            //写文件
            mapper.writeValue(file, temInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("保存场景配置失败 " + e.getMessage());
        }
    }

    public void deleteSceneInfo(String sceneCode) {
        try {
            String projectRoot = System.getProperty("user.dir");
            String targetDir = Paths.get(projectRoot, "template", "scene").toString();
            File file = new File(targetDir, sceneCode + ".json");

            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("文件删除失败: " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除场景配置失败: " + e.getMessage(), e);
        }
    }

    public Resource downloadScene(Long id) {
        Scene existScene = sceneMapper.selectById(id);
        if (existScene == null) {
            throw new RuntimeException("场景不存在");
        }

        try {
            String projectRoot = System.getProperty("user.dir");
            String targetDir = Paths.get(projectRoot, "template", "scene").toString();
            Path filePath = Paths.get(targetDir, existScene.getSceneCode() + ".json");
            if (!filePath.toFile().exists()) {
                throw new RuntimeException("场景配置文件不存在");
            }
            return new FileSystemResource(filePath);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("场景配置文件不存在: " + e.getMessage());
        }
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return "0";
        }
        String value = status.trim().toLowerCase();
        if (value.isEmpty()) {
            return "0";
        }
        if (Objects.equals(value, "1") || Objects.equals(value, "published")) {
            return "1";
        }
        return "0";
    }
}
