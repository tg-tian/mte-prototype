package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import demo.lowcode.platform.common.CommonConfig;
import demo.lowcode.platform.dto.*;
import demo.lowcode.platform.entity.*;
import demo.lowcode.platform.mapper.*;
import demo.lowcode.platform.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SceneBusiness {

    private final SceneMapper sceneMapper;
    private final DomainMapper domainMapper;
    private final DomainBusiness domainBusiness;
    private final AreaBusiness areaBusiness;
    private final AreaMapper areaMapper;
    private final TemplateMapper templateMapper;
    private final DomainTemplateMapper domainTemplateMapper;
    private final ComponentMapper componentMapper;
    private final DomainComponentMapper domainComponentMapper;
    private final DeviceModelMapper deviceModelMapper;
    private final DomainDeviceModelMapper domainDeviceModelMapper;

    @Autowired
    public SceneBusiness(
            SceneMapper sceneMapper,
            DomainMapper domainMapper,
            DomainBusiness domainBusiness,
            AreaBusiness areaBusiness,
            AreaMapper areaMapper,
            TemplateMapper templateMapper,
            DomainTemplateMapper domainTemplateMapper,
            ComponentMapper componentMapper,
            DomainComponentMapper domainComponentMapper,
            DeviceModelMapper deviceModelMapper,
            DomainDeviceModelMapper domainDeviceModelMapper
    ) {
        this.sceneMapper = sceneMapper;
        this.domainMapper = domainMapper;
        this.domainBusiness = domainBusiness;
        this.areaBusiness = areaBusiness;
        this.areaMapper = areaMapper;
        this.templateMapper = templateMapper;
        this.domainTemplateMapper = domainTemplateMapper;
        this.componentMapper = componentMapper;
        this.domainComponentMapper = domainComponentMapper;
        this.deviceModelMapper = deviceModelMapper;
        this.domainDeviceModelMapper = domainDeviceModelMapper;
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

        String currentStatus = normalizeStatus(existingScene.getStatus());
        String targetStatus;
        if (Objects.equals(currentStatus, "0")) {
            targetStatus = "1";
        } else {
            targetStatus = "0";
        }
        
        String targetUrl = pubInfo.getUrl() != null ? pubInfo.getUrl() : existingScene.getUrl();

        Object result = existingScene;

        if (Objects.equals(targetStatus, "1")){
            SceneTemInfo exportInfo = buildSceneExportInfo(existingScene, targetUrl);
            writeSceneInfo(exportInfo);
            result = exportInfo;
        } else {
            // Unpublish - delete scene configuration file
            deleteSceneInfo(existingScene.getSceneCode());
        }

        existingScene.setStatus(targetStatus);
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

        // Fetch Domain Information
        if (scene.getDomainId() != null) {
            Domain domain = domainMapper.selectById(scene.getDomainId());
            if (domain != null) {
                DomainTemInfo domainInfo = domainBusiness.buildDomainExportInfo(domain, null, DomainStatus.PUBLISHED.getCode(), domain.getUrl());
                exportInfo.setDomainInfo(domainInfo);
            }
        }
        
        // Fetch Area Tree
        List<Area> areas = areaBusiness.getAreaListByScene(scene.getSceneId());
        if (areas != null && !areas.isEmpty()) {
            List<NewArea> areaTree = buildAreaTree(areas);
            exportInfo.setAreaTree(areaTree);
        } else {
            exportInfo.setAreaTree(new ArrayList<>());
        }
        
        return exportInfo;
    }

    private List<NewArea> buildAreaTree(List<Area> allAreas) {
        List<NewArea> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        
        Map<Long, NewArea> areaMap = new HashMap<>();
        for (Area area : allAreas) {
            NewArea newArea = mapper.convertValue(area, NewArea.class);
            areaMap.put(area.getId(), newArea);
        }
        
        for (Area area : allAreas) {
            NewArea newArea = areaMap.get(area.getId());
            if (area.getParentId() == null || area.getParentId() == -1) {
                result.add(newArea);
            } else {
                NewArea parent = areaMap.get(area.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(newArea);
                } else {
                    result.add(newArea); // If parent not found, add to root
                }
            }
        }
        return result;
    }

    public void writeSceneInfo(SceneTemInfo temInfo){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
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

    public byte[] downloadScene(Long id) {
        Scene existScene = sceneMapper.selectById(id);
        if (existScene == null) {
            throw new RuntimeException("场景不存在");
        }

        try {
            SceneTemInfo exportInfo = buildSceneExportInfo(existScene, existScene.getUrl());
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            byte[] sceneJsonBytes = mapper.writeValueAsBytes(exportInfo);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8)) {
                writeZipEntry(zipOutputStream, "scene.json", sceneJsonBytes);
                appendSqlDirectory(zipOutputStream);
                zipOutputStream.finish();
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("场景配置压缩包生成失败: " + e.getMessage(), e);
        }
    }

    private void appendSqlDirectory(ZipOutputStream zipOutputStream) throws IOException {
        Path sqlDir = getSqlDirectoryPath();
        if (!Files.exists(sqlDir) || !Files.isDirectory(sqlDir)) {
            return;
        }

        try (var stream = Files.walk(sqlDir)) {
            for (Path path : stream.filter(Files::isRegularFile).toList()) {
                Path relativePath = sqlDir.relativize(path);
                String entryName = "sql/" + relativePath.toString().replace('\\', '/');
                writeZipEntry(zipOutputStream, entryName, Files.readAllBytes(path));
            }
        }
    }

    private Path getSqlDirectoryPath() {
        String projectRoot = System.getProperty("user.dir");
        return Paths.get(projectRoot, "template", "sql");
    }

    private void writeZipEntry(ZipOutputStream zipOutputStream, String entryName, byte[] content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content);
        zipOutputStream.closeEntry();
    }

    @Transactional
    public Scene importScene(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("导入文件不能为空");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            SceneTemInfo sceneTemInfo = mapper.readValue(file.getInputStream(), SceneTemInfo.class);
            return importScene(sceneTemInfo);
        } catch (IOException e) {
            throw new RuntimeException("场景配置解析失败: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Scene importScene(SceneTemInfo sceneTemInfo) {
        if (sceneTemInfo == null || sceneTemInfo.getSceneData() == null) {
            throw new RuntimeException("场景配置不能为空");
        }

        NewScene sceneData = sceneTemInfo.getSceneData();
        if (sceneData.getCode() == null || sceneData.getCode().trim().isEmpty()) {
            throw new RuntimeException("场景编码不能为空");
        }
        if (sceneMapper.selectBySceneCode(sceneData.getCode().trim()) != null) {
            throw new RuntimeException("场景编码已存在");
        }

        Long domainId = resolveImportedDomain(sceneTemInfo.getDomainInfo(), sceneData.getDomainId());
        sceneData.setDomainId(domainId);
        Scene scene = createScene(sceneData);

        if (sceneTemInfo.getAreaTree() != null) {
            for (NewArea rootArea : sceneTemInfo.getAreaTree()) {
                importAreaTree(scene.getSceneId(), rootArea, -1L);
            }
        }
        return scene;
    }

    private Long resolveImportedDomain(DomainTemInfo domainInfo, Long fallbackDomainId) {
        if (domainInfo != null && domainInfo.getDomainData() != null) {
            String domainCode = domainInfo.getDomainData().getCode();
            if (domainCode == null || domainCode.trim().isEmpty()) {
                throw new RuntimeException("导出中的领域编码不能为空");
            }
            Domain existingDomain = domainMapper.getDomainByCode(domainCode.trim());
            if (existingDomain == null) {
                existingDomain = createImportedDomain(domainInfo);
            } else {
                syncImportedDomainResources(existingDomain, domainInfo);
            }
            return existingDomain.getDomainId();
        }
        if (fallbackDomainId == null) {
            throw new RuntimeException("场景缺少关联领域信息");
        }
        Domain domain = domainMapper.selectById(fallbackDomainId);
        if (domain == null) {
            throw new RuntimeException("场景关联领域不存在");
        }
        return domain.getDomainId();
    }

    private Domain createImportedDomain(DomainTemInfo domainInfo) {
        NewDomain domainData = domainInfo.getDomainData();
        Domain domain = new Domain();
        domain.setDomainCode(domainData.getCode());
        domain.setDomainName(domainData.getName());
        domain.setDomainDescription(domainData.getDescription());
        domain.setStatus(DomainStatus.normalizeCode(domainData.getStatus()));
        domain.setCodeEditor(domainData.getCodeEditor());
        domain.setModelEditor(domainData.getModelEditor());
        domain.setFramework(domainData.getBaseFramework());
        domain.setDsl(domainData.getDslStandard());
        domain.setUrl(domainData.getUrl());
        Date now = new Date();
        domain.setCreateTime(now);
        domain.setUpdateTime(now);
        domainMapper.insert(domain);
        syncImportedDomainResources(domain, domainInfo);
        return domain;
    }

    private void syncImportedDomainResources(Domain domain, DomainTemInfo domainInfo) {
        Long domainId = domain.getDomainId();

        if (domainInfo.getTemplates() != null) {
            for (NewTemplate templateInfo : domainInfo.getTemplates()) {
                Template template = upsertTemplate(templateInfo);
                if (template != null && template.getId() != null) {
                    try {
                        domainTemplateMapper.insertDomainTemplateRelation(domainId, template.getId());
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        if (domainInfo.getComponents() != null) {
            for (ComponentDto componentInfo : domainInfo.getComponents()) {
                Component component = upsertComponent(componentInfo);
                if (component != null && component.getComponentId() != null
                        && domainComponentMapper.selectByDomainAndComponent(domainId, component.getComponentId()) == null) {
                    domainComponentMapper.batchInsertDomainComponents(domainId, java.util.Collections.singletonList(component.getComponentId()));
                }
            }
        }

        if (domainInfo.getDeviceTypes() != null) {
            for (DeviceModel deviceModelInfo : domainInfo.getDeviceTypes()) {
                DeviceModel deviceModel = upsertDeviceModel(deviceModelInfo);
                if (deviceModel != null && deviceModel.getId() != null
                        && domainDeviceModelMapper.selectByDomainAndDeviceModel(domainId, deviceModel.getId()) == null) {
                    domainDeviceModelMapper.insertDomainDeviceModelRelation(domainId, deviceModel.getId());
                }
            }
        }
    }

    private Template upsertTemplate(NewTemplate templateInfo) {
        if (templateInfo == null) {
            return null;
        }
        Template existing = null;
        if (templateInfo.getId() != null) {
            existing = templateMapper.selectByTemplateId(templateInfo.getId());
        }
        if (existing == null && templateInfo.getTemplate_id() != null) {
            existing = templateMapper.selectById(templateInfo.getTemplate_id());
        }
        if (existing == null) {
            existing = new Template();
        }
        if (templateInfo.getId() != null) {
            existing.setTemplate_id(templateInfo.getId());
        } else if (existing.getTemplate_id() == null && templateInfo.getTemplate_id() != null) {
            existing.setTemplate_id(templateInfo.getTemplate_id());
        }
        existing.setName(templateInfo.getName());
        existing.setDescription(templateInfo.getDescription());
        existing.setCategory(templateInfo.getCategory());
        existing.setTags(templateInfo.getTags());
        existing.setDomain(templateInfo.getDomain());
        existing.setImage_url(templateInfo.getImage_url());
        existing.setDescribing_the_model(templateInfo.getDescribing_the_model());
        existing.setUrl(templateInfo.getUrl());
        if (existing.getId() == null) {
            templateMapper.insert(existing);
        } else {
            templateMapper.updateById(existing);
        }
        return existing;
    }

    private Component upsertComponent(ComponentDto componentInfo) {
        if (componentInfo == null) {
            return null;
        }
        Component existing = null;
        if (componentInfo.getCode() != null && !componentInfo.getCode().isBlank()) {
            existing = componentMapper.selectByCode(componentInfo.getCode());
        }
        if (existing == null && componentInfo.getId() != null) {
            existing = componentMapper.selectById(componentInfo.getId());
        }
        if (existing == null) {
            existing = new Component();
            existing.setCreateTime(new Date());
        }
        existing.setComponentCode(componentInfo.getCode());
        existing.setComponentName(componentInfo.getName());
        existing.setComponentDescription(componentInfo.getDescription());
        existing.setComponentType(componentInfo.getType());
        existing.setComponentPurpose(componentInfo.getPurpose());
        existing.setConstraints(buildComponentConstraintsJson(componentInfo));
        existing.setProperties(toJson(componentInfo.getProperties()));
        existing.setInputs(toJson(componentInfo.getInputs()));
        existing.setOutputs(toJson(componentInfo.getOutputs()));
        existing.setUpdateTime(new Date());
        if (existing.getComponentId() == null) {
            componentMapper.insert(existing);
        } else {
            componentMapper.updateById(existing);
        }
        return existing;
    }

    private DeviceModel upsertDeviceModel(DeviceModel deviceModelInfo) {
        if (deviceModelInfo == null) {
            return null;
        }
        DeviceModel existing = null;
        if (deviceModelInfo.getModelId() != null && !deviceModelInfo.getModelId().isBlank()) {
            existing = deviceModelMapper.selectByModelId(deviceModelInfo.getModelId());
        }
        if (existing == null && deviceModelInfo.getId() != null) {
            existing = deviceModelMapper.selectById(deviceModelInfo.getId());
        }
        if (existing == null) {
            existing = new DeviceModel();
            existing.setCreateTime(LocalDateTime.now());
        }
        existing.setModelId(deviceModelInfo.getModelId());
        existing.setModelName(deviceModelInfo.getModelName());
        existing.setProvider(deviceModelInfo.getProvider());
        existing.setCategory(deviceModelInfo.getCategory());
        existing.setModel(deviceModelInfo.getModel());
        existing.setUpdateTime(LocalDateTime.now());
        if (existing.getId() == null) {
            deviceModelMapper.insert(existing);
        } else {
            deviceModelMapper.updateById(existing);
        }
        return existing;
    }

    private void importAreaTree(Long sceneId, NewArea areaInfo, Long parentId) {
        if (areaInfo == null) {
            return;
        }
        Area area = new Area();
        area.setName(areaInfo.getName());
        area.setSceneId(sceneId);
        area.setDescription(areaInfo.getDescription());
        area.setImage(areaInfo.getImage());
        area.setPosition(areaInfo.getPosition());
        area.setParentId(parentId == null ? -1L : parentId);
        areaMapper.insert(area);

        if (areaInfo.getChildren() != null) {
            for (NewArea child : areaInfo.getChildren()) {
                importAreaTree(sceneId, child, area.getId());
            }
        }
    }

    private String buildComponentConstraintsJson(ComponentDto componentInfo) {
        Map<String, Object> constraints = new LinkedHashMap<>();
        if (componentInfo.getInputConstraint() != null) {
            constraints.put("inputConstraint", componentInfo.getInputConstraint());
        }
        if (componentInfo.getOutputConstraint() != null) {
            constraints.put("outputConstraint", componentInfo.getOutputConstraint());
        }
        if (componentInfo.getStartConstraint() != null) {
            constraints.put("startConstraint", componentInfo.getStartConstraint());
        }
        if (componentInfo.getEndConstraint() != null) {
            constraints.put("endConstraint", componentInfo.getEndConstraint());
        }
        return toJson(constraints.isEmpty() ? null : constraints);
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON 序列化失败: " + e.getMessage(), e);
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
        if (Objects.equals(value, "1") || Objects.equals(value, "published") || Objects.equals(value, "true")) {
            return "1";
        }
        return "0";
    }
}
