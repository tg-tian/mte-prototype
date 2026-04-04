package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("domain_device_model")
public class DomainDeviceModel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long domainId;
    private Long deviceModelId;
}

