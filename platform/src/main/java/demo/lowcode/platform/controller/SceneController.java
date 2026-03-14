package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.SceneBusiness;
import demo.lowcode.platform.dto.NewScene;
import demo.lowcode.platform.dto.ScenePubInfo;
import demo.lowcode.platform.entity.Scene;
import demo.lowcode.platform.model.ScenarioJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Api(value = "场景注册接口",tags={"场景注册管理"})
public class SceneController {

    @Resource
    public SceneBusiness sceneBusiness;
    @PostMapping(value = "/scenes/device")
    public ResponseEntity<?> uploadDeviceRegister(@RequestBody Map<String, Object> requestBody, String scenarioCode,String deviceName) {
        //注册设备
       try {
           return new ResponseEntity<>("设备注册成功", HttpStatus.OK);
        }catch (RuntimeException e)
        {
            System.err.println("设备注册失败: " + e.getMessage());
            return new ResponseEntity<>("请勿重复注册设备",HttpStatus.CONFLICT);
        }
    }

    /**
     * @param scenarioCode
     * @return
     */
    @GetMapping(value = "/scenes/device")
    @ApiOperation(value = "获取场景注册数据",notes = "用于获取该场景下已经注册的设备信息")
    public ResponseEntity<?> loadDeviceRegister(String scenarioCode){
        List<Map<String,String>> resultList = new ArrayList<>(); //构建map存储所需数据
        return new ResponseEntity<>(resultList,HttpStatus.OK);
    }

    @GetMapping (value = "/load-scenario-json")
    public ResponseEntity<?> loadScenarioJson(){
        try {
            ScenarioJson scenarioJson =  sceneBusiness.loadScenarioJson();
            System.out.println("场景数据loading成功！");
            return new ResponseEntity<>(scenarioJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("场景数据读取错误",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取场景列表
     * @param domainId
     * @return
     */
    @GetMapping(value = "/scenes")
    @ApiOperation(value = "获取场景列表",notes = "用于获取场景的列表")
    public ResponseEntity<?> getSceneList(Long domainId){
        List<Scene> sceneList = sceneBusiness.getSceneList(domainId);
        return new ResponseEntity<>(sceneList,HttpStatus.OK);
    }
    
    /**
     * 获取所有场景列表
     * @return 所有场景列表
     */
    @GetMapping(value = "/all-scenes")
    @ApiOperation(value = "获取所有场景列表",notes = "用于获取所有场景的列表，不需要指定领域ID")
    public ResponseEntity<?> getAllScenes(){
        List<Scene> allScenes = sceneBusiness.getAllScenes();
        return new ResponseEntity<>(allScenes,HttpStatus.OK);
    }

    /**
     * 通过id获取场景信息
     * @param id
     * @return
     */
    @GetMapping(value = "/scenes/{id}")
    @ApiOperation(value = "通过对应Id获取场景",notes = "通过id获取场景信息")
    public ResponseEntity<?> getSceneById(@PathVariable Long id){
        try{
            Scene scene = sceneBusiness.getSceneById(id);
            return new ResponseEntity<>(scene,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("场景获取失败",HttpStatus.CONFLICT);
        }
    }

    /**
     * 创建新的场景信息
     * @param newScene
     * @return
     */
    @PostMapping(value = "/scenes")
    @ApiOperation(value = "创建新场景",notes = "创建新场景")
    public ResponseEntity<?> createScene(@RequestBody NewScene newScene) {
        try {
            Scene scene = sceneBusiness.createScene(newScene);
            return new ResponseEntity<>(scene ,HttpStatus.OK);
        }catch (RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>("场景创建失败",HttpStatus.CONFLICT);
        }
    }

    /**
     * 修改场景信息
     * @param id
     * @param newScene
     * @return
     */
    @PutMapping(value = "/scenes/{id}")
    @ApiOperation(value = "修改场景信息",notes = "修改场景信息")
    public ResponseEntity<?> changeScene(@PathVariable Long id,@RequestBody NewScene newScene){
        try {
            Scene scene = sceneBusiness.changeScene(id,newScene);
            return new ResponseEntity<>(scene ,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("场景修改失败",HttpStatus.CONFLICT);
        }
    }

    /**
     * 删除场景信息
     * @param id
     * @return
     */
    @DeleteMapping(value = "/scenes/{id}")
    @ApiOperation(value = "删除场景信息",notes = "删除场景信息")
    public ResponseEntity<?> deleteScene(@PathVariable Long id){
        try {
            sceneBusiness.deleteSceneByID(id);
            return new ResponseEntity<>("删除成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("删除失败",HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/scenes/publish")
    @ApiOperation(value = "发布场景")
    public ResponseEntity<?> publishScene(@RequestBody ScenePubInfo pubInfo){
        try {
            Scene scene = sceneBusiness.publishScene(pubInfo);
            return new ResponseEntity<>(scene,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("发布失败",HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/scenes/download/{id}")
    @ApiOperation(value = "下载场景配置文件")
    public ResponseEntity<?> downloadScene(@PathVariable Long id){
        try {
            org.springframework.core.io.Resource scene = sceneBusiness.downloadScene(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + scene.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(scene);
        }catch (RuntimeException e){
            return new ResponseEntity<>("文件下载失败",HttpStatus.CONFLICT);
        }
    }

}
