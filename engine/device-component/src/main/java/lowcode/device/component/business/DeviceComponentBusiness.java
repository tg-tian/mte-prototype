package lowcode.device.component.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.CommonConfig;
import lowcode.device.component.dto.CommandDto;
import lowcode.device.component.entity.BrandService;
import lowcode.device.component.entity.Event;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DeviceComponentBusiness {

    public void loadDevice(String devicePath) throws IOException {
        System.out.println("开始加载设备类型");
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(input);
        File file = new File(CommonConfig.getDefinitionPath() + devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        String deviceType = rootNode.path("type").asText();

        // 读取commands数组
        JsonNode commandsNode = rootNode.path("commands");
        List<Command> commands = new ArrayList<>();
        if (commandsNode.isArray()) {
            for (JsonNode command : commandsNode) {
                String commandName = command.path("name").asText();
                String outputParam = command.path("outputParam").asText();
                List<Param> inputParam = new ArrayList<>();
                JsonNode inputNodes = command.path("inputParam");
                if (inputNodes.isArray()) {
                    for (JsonNode inputNode : inputNodes) {
                        Param param = new Param(inputNode.path("code").asText(), inputNode.path("name").asText(), inputNode.path("type").asText());
                        inputParam.add(param);
                    }
                }
                Command command1 = new Command();//commandName, inputParam, outputParam);
                commands.add(command1);
            }
        }

        // 读取events数组
        JsonNode eventsNode = rootNode.path("events");
        List<String> events = new ArrayList<>();
        if (eventsNode.isArray()) {
            for (JsonNode event : eventsNode) {
                events.add(event.asText());
            }
        }
        System.out.println("读取json文件成功");
    }


    /**
     * 设备拓展功能实现
     * loadParam : 加载 输入 和 输出 参数
     * loadEvent : 加载 事件的基本信息
     */
    @ApiOperation(value = "加载参数", notes = "直接载入 设备信息类 的 参数 属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name="deviceName",value="设备名称",required=true),
            @ApiImplicitParam(name = "commandCode", value = "设备支持的操作代码",required = true),
    })
    public List<Param> loadInputParam(String deviceName,  String commandCode) throws IOException {
        File file = new File("definition/"+deviceName+"/"+deviceName+".json");
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
        List<Param> paramList = new ArrayList<>();
        for(JsonNode command:commands) {
            if(Objects.equals(command.path("code").asText(), commandCode)) {
                JsonNode Params = command.path("inputParam"); //获取inputParam的第一个节点
                for (JsonNode param : Params){
                    Param param1 = new Param();
                    String code = param.path("code").asText();
                    String name = param.path("name").asText();
                    String type = param.path("type").asText();
                    param1.Change(code,name,type);
                    paramList.add(param1);
                }
            }
        }
        return paramList;
    }

    @ApiOperation(value = "加载事件", notes = "加载事件的具体关联信息,读取文件：“操作名称” + Event.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="deviceName",value="设备名称",required=true),
            @ApiImplicitParam(name="operationCode",value="事件操作码",required=true),
    })
    public List<Event> loadEvent(String deviceName, String operationCode) throws IOException {
        File file = new File("definition/"+deviceName+"/events/"+operationCode+"Event.json");
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

    //TODO 事件读取存在一些逻辑问题需要修改
    @ApiOperation(value = "加载操作", notes = "加载操作基本信息,读取文件：“设备名称”.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devicePath",value="设备名称json访问路径",required=true),
    })
    public List<CommandDto> loadCommand(String deviceType) throws  IOException{
        File file = new File(CommonConfig.getDefinitionPath()+deviceType+"/definitions/"+deviceType+".json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode commands = rootNode.path("commands");
        List<CommandDto> commandList = new ArrayList<>();
        if(commands.isArray()){
            for(JsonNode command:commands){
                String code = command.path("code").asText();
                String name = command.path("name").asText();

                JsonNode eventNode = command.path("events");
                List<String> events = new ArrayList<>();
                if (eventNode.isArray()) {
                    for (JsonNode event: eventNode){
                        events.add(event.asText());
                    }
                }

                JsonNode serviceNode = command.path("services");
                List<String> services = new ArrayList<>();
                if (serviceNode.isArray()) {
                    for (JsonNode service: serviceNode){
                        services.add(service.asText());
                    }
                }

                CommandDto command1 = new CommandDto();
                command1.setCommandCode(code);
                command1.setCommandName(name);
                command1.setEvents(events);
                command1.setServices(services);
                commandList.add(command1);
            }
        }
        return commandList;
    }

    @ApiOperation(value = "加载服务", notes = "加载操作绑定基本信息,读取文件：“服务名称”.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="servicePath",value="服务名称json访问路径",required=true),
    })
    public BrandService loadService(String deviceName , String serviceName) throws IOException{
        File file = new File("definition/"+deviceName+"/services/"+serviceName+".json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        String code = rootNode.path("code").asText();
        String description = rootNode.path("description").asText();
        return new BrandService(code,description,serviceName+".json");
    }
}
