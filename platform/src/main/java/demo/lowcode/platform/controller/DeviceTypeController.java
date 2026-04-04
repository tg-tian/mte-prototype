package demo.lowcode.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.lowcode.platform.business.DeviceModelBusiness;
import demo.lowcode.platform.dto.BindInfo;
import demo.lowcode.platform.entity.DeviceModel;
import demo.lowcode.platform.entity.DomainComponent;
import demo.lowcode.platform.mapper.DeviceModelMapper;
import demo.lowcode.platform.mapper.DomainComponentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class DeviceTypeController {

    private final DeviceModelBusiness deviceModelBusiness;
    private final DeviceModelMapper deviceModelMapper;
    private final DomainComponentMapper domainComponentMapper;

    @Autowired
    public DeviceTypeController(
            DeviceModelBusiness deviceModelBusiness,
            DeviceModelMapper deviceModelMapper,
            DomainComponentMapper domainComponentMapper
    ) {
        this.deviceModelBusiness = deviceModelBusiness;
        this.deviceModelMapper = deviceModelMapper;
        this.domainComponentMapper = domainComponentMapper;
    }

    @GetMapping("/api/v1/device-types")
    public ResponseEntity<?> list(@RequestParam(required = false) Long domainId) {
        try {
            if (domainId != null) {
                return new ResponseEntity<>(deviceModelMapper.selectByDomainId(domainId), HttpStatus.OK);
            }
            return new ResponseEntity<>(deviceModelBusiness.list(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch device types: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/api/v1/device-types/page")
    public ResponseEntity<?> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long domainId
    ) {
        try {
            if (domainId != null) {
                List<DeviceModel> all = deviceModelMapper.selectByDomainId(domainId);
                int fromIndex = Math.max((page - 1) * size, 0);
                if (fromIndex >= all.size()) {
                    return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
                }
                int toIndex = Math.min(fromIndex + size, all.size());
                return new ResponseEntity<>(all.subList(fromIndex, toIndex), HttpStatus.OK);
            }

            QueryWrapper<DeviceModel> queryWrapper = new QueryWrapper<>();
            if (modelName != null && !modelName.trim().isEmpty()) {
                queryWrapper.like("modelName", modelName.trim());
            }
            if (category != null && !category.trim().isEmpty()) {
                queryWrapper.like("category", category.trim());
            }

            Page<DeviceModel> pageParam = new Page<>(page, size);
            IPage<DeviceModel> result = deviceModelBusiness.page(pageParam, queryWrapper);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to page device types: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/domain/devicetype/binding")
    public ResponseEntity<?> bindDeviceType(@RequestBody BindInfo bindInfo) {
        try {
            DomainComponent existed = domainComponentMapper.selectByDomainAndComponentWithType(
                    bindInfo.getDomainId(),
                    bindInfo.getDeviceTypeId(),
                    "deviceType"
            );
            if (existed != null) {
                throw new RuntimeException("绑定失败：该设备类型已绑定");
            }
            DomainComponent relation = new DomainComponent();
            relation.setDomainId(bindInfo.getDomainId());
            relation.setComponentId(bindInfo.getDeviceTypeId());
            relation.setComponentType("deviceType");
            domainComponentMapper.insert(relation);
            return new ResponseEntity<>("绑定成功", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/domain/devicetype/unbinding")
    public ResponseEntity<?> unbindDeviceType(@RequestBody BindInfo bindInfo) {
        try {
            DomainComponent existed = domainComponentMapper.selectByDomainAndComponentWithType(
                    bindInfo.getDomainId(),
                    bindInfo.getDeviceTypeId(),
                    "deviceType"
            );
            if (existed == null) {
                throw new RuntimeException("两者未绑定");
            }
            domainComponentMapper.deleteById(existed.getId());
            return new ResponseEntity<>("取消绑定成功", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
