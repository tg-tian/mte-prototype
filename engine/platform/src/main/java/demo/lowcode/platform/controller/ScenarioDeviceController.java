package demo.lowcode.platform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/scenario" )
@CrossOrigin
@Api(value = "场景注册接口",tags={"场景注册管理"})
public class ScenarioDeviceController {
    /**
     * TODO：上传场景绑定设备
     */
    @PostMapping(value = "/device")
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
     * TODO：获取场景绑定设备
     * @param scenarioCode
     * @return
     */
    @GetMapping(value = "/device")
    @ApiOperation(value = "获取场景注册数据",notes = "用于获取该场景下已经注册的设备信息")
    public ResponseEntity<?> loadDeviceRegister(String scenarioCode){
        List<Map<String,String>> resultList = new ArrayList<>(); //构建map存储所需数据

        return new ResponseEntity<>(resultList,HttpStatus.OK);
    }

}
