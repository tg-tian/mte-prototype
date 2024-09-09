package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import demo.lowcode.platform.entity.BrandService;
import demo.lowcode.platform.entity.Event;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceComponentBusiness {
    @Value("${definitionPath}")
    private String definitionPath;

    /**
     * 设备拓展功能实现
     * loadParam : 加载 输入 和 输出 参数
     * loadEvent : 加载 事件的基本信息
     */
    @ApiOperation(value = "加载参数", notes = "直接载入 设备信息类 的 参数 属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devicePath",value="设备名称json访问路径",required=true),
            @ApiImplicitParam(name = "command_code", value = "设备支持的操作代码",required = true),
    })
    private Param loadInputParam(String devicePath,  String command_code) throws IOException {
        // paramType : ["inputParam","outputParam"]
        // command_code : ["Start","MakeCoffee",......]
        File file = new File(devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * ObjectMapper 是 Jackson 库中用于处理 JSON 数据的核心类。以下是 ObjectMapper 的常见功能：
         * 1.将对象转换为 JSON 字符串：使用 writeValueAsString(Object value) 方法，可以将 Java 对象转换为 JSON 格式的字符串。
         * 2.将 JSON 字符串转换为对象：使用 readValue(String content, Class<T> valueType) 方法，可以将 JSON 字符串转换为指定类型的 Java 对象。
         * 3.将 JSON 字符串转换为集合或泛型对象：使用 readValue(String content, TypeReference<T> valueTypeRef) 方法，可以将 JSON 字符串转换为泛型对象或集合类型。
         * 4.从文件中读取 JSON 数据：使用 readValue(File src, Class<T> valueType) 方法，可以从文件中读取 JSON 数据并转换为 Java 对象。
         * 5.将对象写入文件为 JSON：使用 writeValue(File resultFile, Object value) 方法，可以将 Java 对象转换为 JSON 并写入文件中。
         * */
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode commands = rootNode.path("commands");
        Param param = new Param();

        if (commands.isArray() && commands.path(command_code).isArray()) {
            //获得对应的操作的json目录
            JsonNode command = commands.path(command_code);
            JsonNode Params = command.path("in").path(0); //获取inputParam的第一个节点

            if (Params.isArray()) {
                String code = Params.path("code").asText();
                String name = Params.path("name").asText();
                String type = Params.path("type").asText();
                /**
                List<String> options = new ArrayList<>();
                JsonNode option_s = Params.path("options");
                for (JsonNode opt : option_s) {
                    options.add(opt.asText());
                }*/
                param.Change(code, name, type);
            }
        }
        return param;
    }

    @ApiOperation(value = "加载事件", notes = "加载事件的具体关联信息,读取文件：“操作名称” + Event.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="eventPath",value="事件json访问路径",required=true),
    })
    private List<Event> loadEvent(String eventPath) throws IOException {
        File file = new File(eventPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode events = rootNode.path("eventList");
        List<Event> eventList = new ArrayList<>();
        if (events.isArray()) {
            for (JsonNode event : events) {
                String name = event.path("name").asText();
                String description = event.path("description").asText();
                String type = event.path("type").asText();
                Event new_event = new Event(name,description,type);
                eventList.add(new_event);
            }
        }
        return eventList;
    }

    @ApiOperation(value = "加载操作", notes = "加载操作基本信息,读取文件：“设备名称”.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devicePath",value="设备名称json访问路径",required=true),
    })
    private List<Command> loadCommand(String devicePath) throws  IOException{
        File file = new File(devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode commands = rootNode.path("commands");
        List<Command> commandList = new ArrayList<>();
        if(commands.isArray()){
            for(JsonNode command:commands){
                String code = command.path("code").asText();
                String name = command.path("name").asText();
                Command command1 = new Command(code,name);
                commandList.add(command1);
            }
        }
        return commandList;
    }

    @ApiOperation(value = "加载服务", notes = "加载操作绑定基本信息,读取文件：“服务名称”.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="servicePath",value="服务名称json访问路径",required=true),
    })
    private BrandService loadService(String servicePath) throws IOException{
        File file = new File(servicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        String code = rootNode.path("code").asText();
        String description = rootNode.path("description").asText();
        return new BrandService(code,description);
    }
}
