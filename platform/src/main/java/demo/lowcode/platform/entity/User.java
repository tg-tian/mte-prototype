package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@TableName("sys_user_info")
@ApiModel(value = "用户类", description = "存储用户基本信息")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户ID", example = "1")
    private long user_id;
    @Column(name = "user_name", nullable = false)
    @ApiModelProperty(value = "用户名",example = "admin")
    private String user_name;
    @Column(name = "password", nullable = false)
    @ApiModelProperty(value = "密码",example = "ADSAJFGBHJG122324NJ")
    private String password;
    @Column(name = "role_id", nullable = false)
    @ApiModelProperty(value = "用户角色id",example = "1")
    private long role_id;
    @Column(name = "user_info")
    @ApiModelProperty(value = "用户信息",example = "管理员账号")
    private String user_info;
}
