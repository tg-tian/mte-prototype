package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上传服务类，执行数据上传数据库业务
 */
@Service
public class DeviceTypeBusiness {
    //服务层，不需要Swagger的注解描述。在Controller中使用Swagger注解。
    //使用构造函数注入 DeviceTypeMapper 确保依赖不可变

    private final DeviceTypeMapper deviceTypeMapper;

    // 使用构造函数注入，推荐的方式
    @Autowired
    public DeviceTypeBusiness(DeviceTypeMapper deviceTypeMapper) {
        this.deviceTypeMapper = deviceTypeMapper;
    }

    /**
     * 新增设备类型，上传数据库
     * @param deviceType
     */
    public void deviceTypeUpload(DeviceType deviceType){
        //deviceTypeMapper.insert(deviceType);
        int count = deviceTypeMapper.countByDeviceTypeCode(deviceType.getDeviceTypeCode());
        if(count == 0){
            deviceTypeMapper.insertDeviceType(deviceType.getDeviceTypeCode(),deviceType.getDeviceTypeName(),deviceType.getImgPath());
        }else{
            throw new RuntimeException("deviceTypeCode 已存在，不能插入重复的记录");
        }
    }

    /**
     * 查询设备表的所有记录
     * @return
     */
    public List<DeviceType> getDeviceTypeData(){
        List<DeviceType> result = deviceTypeMapper.selectList(null);
        return  result;
    }

    /**
     * 根据设备名称查询设备id
     * @param deviceTypeCode
     * @return
     */
    public long loadDeiceTypeId(String deviceTypeCode){
        return deviceTypeMapper.loadDeviceId(deviceTypeCode);
    }
    public  List<DeviceType> loadDeviceData(List<Long> deviceTypeIdList){
        return  deviceTypeMapper.selectBatchIds(deviceTypeIdList);
    }

    /**
     * 根据设备类型id查询设备类型名称
     * @param deviceTypeId
     * @return
     */
    public String loadDeviceType(long deviceTypeId){
        return deviceTypeMapper.getDeviceTypeCode(deviceTypeId);
    }

    /**
     * 查询某一设备类型的数据
     * @param deviceTypeCode
     * @return
     */
    public DeviceType loadDeviceTypeInfo(String deviceTypeCode) {return deviceTypeMapper.selectByCode(deviceTypeCode);}

    /**
     * 更新发布状态
     * @param deviceTypeCode
     */
    public void updatePublish(String deviceTypeCode) {deviceTypeMapper.updatePublish(deviceTypeCode);}
}
