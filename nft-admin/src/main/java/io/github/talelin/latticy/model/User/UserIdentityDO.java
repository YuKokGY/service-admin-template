package io.github.talelin.latticy.model.User;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import lombok.*;

import java.io.Serializable;

/**
 * @author pedro@TaleLin
 */
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_identity")
public class UserIdentityDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 456555840105356178L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 认证类型，例如 username_password，用户名-密码认证
     */
    private String identityType;

    /**
     * 认证，例如 用户名
     */
    private String identifier;

    /**
     * 凭证，例如 密码
     */
    private String credential;
}
