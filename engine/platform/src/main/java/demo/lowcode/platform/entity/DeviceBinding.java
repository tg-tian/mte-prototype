package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 领域设备组件绑定类，存储设备组件的绑定情况
 */

@Entity
@Data
@TableName("deviceBinding")
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备组件绑定信息对象", description = "设备组件绑定详细信息")
public class DeviceBinding {
    @Id
    @Column(name = "deviceTypeId", nullable = false)
    @ApiModelProperty(value = "设备类型编号", example = "001")
    private long deviceTypeId;

    @ManyToOne  // 多个 Binding 可以对应一个 DeviceType
    @JoinColumn(name = "deviceTypeId", referencedColumnName = "deviceTypeId", insertable = false, updatable = false)
    private DeviceType deviceType;  // 引用到 DeviceType 实体

    @Id
    @Column(name = "domainId", nullable = false)
    @ApiModelProperty(value = "领域编号", example = "001")
    private long domainId;

    public DeviceBinding(long deviceTypeId, long domainId) {
        this.deviceTypeId = deviceTypeId;
        this.domainId = domainId;
    }
}
