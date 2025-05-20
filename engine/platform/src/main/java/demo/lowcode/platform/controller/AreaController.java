package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.AreaBusiness;
import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.entity.Area;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Api(value = "区域接口", tags = {"区域实例管理"})
public class AreaController {
    @Resource
    private AreaBusiness areaBusiness;


    @GetMapping("/areas")
    @ApiOperation(value = "获取当前场景的区域列表")
    public ResponseEntity<?> getAreas(@RequestParam Long sceneId) {
        try {
            List<Area> areaList = areaBusiness.getAreaListByScene(sceneId);
            return new ResponseEntity<>(areaList, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("区域读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到区域列表", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/areas")
    @ApiOperation(value = "创建新的区域")
    public ResponseEntity<?> createArea(@RequestBody Area newArea) {
        try {
            Area area = areaBusiness.createArea(newArea);
            return new ResponseEntity<>(area, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/areas/{id}")
    @ApiOperation(value = "更新区域信息")
    public ResponseEntity<?> updateArea(@PathVariable("id") Long id, @RequestBody Area updatedArea) {
        try {
            Area area = areaBusiness.updateArea(id, updatedArea);
            return new ResponseEntity<>(area, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/areas/{id}")
    @ApiOperation(value = "删除区域")
    public ResponseEntity<?> deleteArea(@PathVariable("id") Long id) {
        try {
            areaBusiness.deleteAreaById(id);
            return new ResponseEntity<>("删除成功", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}
