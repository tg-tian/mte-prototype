package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册类，存储设备和场景的关联信息
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Resource
@TableName("register")
@ApiModel(value = "设备实例类",description = "存储设备与场景关联的详细信息")
@IdClass(DeviceRegisterId.class) // 使用联合主键类
public class DeviceRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceId", nullable = false)
    @ApiModelProperty(value = "设备ID", example = "101")
    private long deviceId;

    @ManyToOne
    @JoinColumn(name = "deviceId", referencedColumnName = "deviceId", nullable = false, insertable = false, updatable = false)
    private Device device;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sceneId", nullable = false)
    @ApiModelProperty(value = "场景ID", example = "202")
    private long sceneId;

    @ManyToOne
    @JoinColumn(name = "sceneId", referencedColumnName = "sceneId", nullable = false, insertable = false, updatable = false)
    private Scene scene;

    @Column(name = "protocol")
    @ApiModelProperty(value = "协议类型", example = "TCP")
    private String protocol;

    @Column(name = "IPAddress")
    @ApiModelProperty(value = "IP地址", example = "192.168.1.1")
    private String iPAddress;

    @Column(name = "port")
    @ApiModelProperty(value = "端口号", example = "8080")
    private int port;
}

