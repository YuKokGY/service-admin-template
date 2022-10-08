package io.github.talelin.latticy.controller;


import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.core.annotation.AdminRequired;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.LoginDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.model.User.GroupDO;
import io.github.talelin.latticy.model.User.UserDO;
import io.github.talelin.latticy.service.cms.user.GroupService;
import io.github.talelin.latticy.service.cms.user.UserIdentityService;
import io.github.talelin.latticy.service.cms.user.UserService;
import io.github.talelin.latticy.vo.UnifyResponseVO;
import io.github.talelin.latticy.vo.UpdatedVO;
import io.github.talelin.latticy.vo.UserInfoVO;
import io.github.talelin.latticy.vo.UserPermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cms/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserIdentityService userIdentityService;

    @Autowired
    GroupService groupService;
    @Autowired
    DoubleJWT jwt;


    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Tokens login(@RequestBody @Validated LoginDTO validator) {
        UserDO user = userService.getUserByUsername(validator.getUsername());
        if (user == null) {
            throw new NotFoundException("user not found", 10021);
        }
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(),
                user.getUsername(),
                validator.getPassword());
        if (!valid) {
            throw new ParameterException("username or password is fault", 10031);
        }
        return jwt.generateTokens(user.getId());
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    @LoginRequired
    public UpdatedVO update(@RequestBody @Validated UpdateInfoDTO validator) {
        userService.updateUserInfo(validator);
        return new UpdatedVO(6);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @AdminRequired
    public UnifyResponseVO<?> register(@RequestBody @Validated RegisterDTO validator) {
        userService.createUser(validator);
        return new UnifyResponseVO<>(0);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @AdminRequired
    public UpdatedVO<?> updateUser(@RequestBody @Validated UpdateInfoDTO dto) {
        userService.updateUserInfo(dto);
        return new UpdatedVO<>(3);
    }


    /**
     * 修改用户密码
     */
    @PutMapping("/change_password")
    @LoginRequired
    public UnifyResponseVO<?> updatePassword(@RequestBody @Validated ChangePasswordDTO dto) {
        userService.changeUserPassword(dto);
        return new UnifyResponseVO<>(0);
    }

    /**
     * 查询拥有权限
     */
    @GetMapping("/permissions")
    @LoginRequired
    public UserPermissionVO getPermissions() {
        UserDO user = LocalUser.getCmsUser();
        boolean admin = groupService.checkIsRootByUserId(user.getId());
        List<Map<String, List<Map<String, String>>>> permissions = userService.getStructualUserPermissions(user.getId());
        UserPermissionVO userPermissions = new UserPermissionVO(user, permissions);
        userPermissions.setAdmin(admin);
        return userPermissions;
    }

    /**
     * 查询自己信息
     */
    @LoginRequired
    @GetMapping("/information")
    public UserInfoVO getInformation() {
        UserDO user = LocalUser.getCmsUser();
        List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
        return new UserInfoVO(user, groups);
    }
}
