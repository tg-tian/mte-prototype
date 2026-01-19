package lowcode.device.component.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.device.DeviceDataType;
import demo.lowcode.common.device.DeviceProperty;
import demo.lowcode.common.device.ValidateParam;
import demo.lowcode.common.util.StringUtil;
import lowcode.device.component.dto.CommandDto;
import lowcode.device.component.dto.ParamDto;
import lowcode.device.component.entity.BrandService;
import lowcode.device.component.entity.DeviceEvent;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class DeviceComponentBusiness {

    public ParamDto loadOperationParam(String deviceName, String commandCode) throws IOException {
        ParamDto paramDto = new ParamDto();
        // TODO:改为向iot平台请求获取

        return paramDto;
    }

    public List<DeviceEvent> loadEvent(String deviceName) throws IOException {
        List<DeviceEvent> result = new ArrayList<>();
        // TODO:改为向iot平台请求获取
        return result;
    }

    @ApiOperation(value = "加载操作")
    public List<CommandDto> loadCommand(String deviceType) throws  IOException{
        // TODO:改为向iot平台请求获取
        List<CommandDto> commandList = new ArrayList<>();
        return commandList;
    }

    public List<DeviceProperty> loadProperties(String deviceType) throws IOException {
        // TODO:改为向iot平台请求获取
        List<DeviceProperty> propertyList = new ArrayList<>();
        return propertyList;
    }
}
