package io.github.talelin.latticy.dto.user;

import io.github.talelin.latticy.model.User.UserDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * @author pedro@TaleLin
 */
@NoArgsConstructor
@Data
public class UpdateInfoDTO extends UserDO {

    @Email(message = "{email}")
    private String email;

    @Length(min = 2, max = 10, message = "{nickname.length}")
    private String nickname;

    @Length(min = 2, max = 10, message = "{username.length}")
    private String username;

    @Length(max = 500, message = "{avatar.length}")
    private String avatar;
}
