package io.github.talelin.latticy.service.cms.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.common.mybatis.Page;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.mapper.UserGroupMapper;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.model.User.GroupDO;
import io.github.talelin.latticy.model.User.PermissionDO;
import io.github.talelin.latticy.model.User.UserDO;
import io.github.talelin.latticy.model.User.UserGroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Transactional
    @Override
    public UserDO createUser(RegisterDTO dto) {
        boolean exist = this.checkUserExistByUsername(dto.getUsername());
        if (exist) {
            throw new ForbiddenException("username already exist, please choose a new one", 10071);
        }
        if (StrUtil.isNotBlank(dto.getEmail())) {
            exist = this.checkUserExistByEmail(dto.getEmail());
            if (exist) {
                throw new ForbiddenException("email already exist, please choose a new one", 10076);
            }
        } else {
            // bug 前端如果传入的email为 "" 时，由于数据库中存在""的email，会报duplication错误
            // 所以如果email为blank，必须显示设置为 null
            dto.setEmail(null);
        }
        UserDO user = new UserDO();
        BeanUtil.copyProperties(dto, user);
        this.baseMapper.insert(user);
        if (dto.getGroupIds() != null && !dto.getGroupIds().isEmpty()) {
            checkGroupsValid(dto.getGroupIds());
            checkGroupsExist(dto.getGroupIds());
            List<UserGroupDO> relations = dto.getGroupIds()
                    .stream()
                    .map(groupId -> new UserGroupDO(user.getId(), groupId))
                    .collect(Collectors.toList());
            userGroupMapper.insertBatch(relations);
        } else {
            // id为2的分组为游客分组
            Long guestGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);
            UserGroupDO relation = new UserGroupDO(user.getId(), guestGroupId);
            userGroupMapper.insert(relation);
        }
        userIdentityService.createUsernamePasswordIdentity(user.getId(), dto.getUsername(), dto.getPassword());
        return user;
    }

    @Transactional
    @Override
    public UserDO updateUserInfo(UpdateInfoDTO dto) {
        UserDO user = LocalUser.getCmsUser();
        if (StrUtil.isNotBlank(dto.getUsername())) {
            boolean exist = this.checkUserExistByUsername(dto.getUsername());
            if (exist) {
                throw new ForbiddenException("username already exist, please choose a new one", 10071);
            }
            user.setAvatar(dto.getAvatar());
            userIdentityService.changeUsername(user.getId(), dto.getUsername());
        }
        dto.setId(user.getId());
        BeanUtil.copyProperties(dto, user);
        this.baseMapper.updateById(user);
        return user;
    }

    @Override
    public UserDO changeUserPassword(ChangePasswordDTO dto) {
        UserDO user = LocalUser.getCmsUser();
        boolean valid = userIdentityService.verifyUsernamePassword(user.getId(), user.getUsername(), dto.getOldPassword());
        if (!valid) {
            throw new ParameterException("password invalid, please enter correct password", 10032);
        }
        valid = userIdentityService.changePassword(user.getId(), dto.getNewPassword());
        if (!valid) {
            throw new FailedException("password change failed", 10011);
        }
        return user;
    }

    @Override
    public List<GroupDO> getUserGroups(Long userId) {
        return groupService.getUserGroupsByUserId(userId);
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> getStructualUserPermissions(Long userId) {
        List<PermissionDO> permissions = getUserPermissions(userId);
        return permissionService.structuringPermissions(permissions);
    }

    @Override
    public List<PermissionDO> getUserPermissions(Long userId) {
        // 查找用户搜索分组，查找分组下的所有权限
        List<Long> groupIds = groupService.getUserGroupIdsByUserId(userId);
        if (groupIds == null || groupIds.size() == 0) {
            return new ArrayList<>();
        }
        return permissionService.getPermissionByGroupIds(groupIds);
    }

    @Override
    public List<PermissionDO> getUserPermissionsByModule(Long userId, String module) {
        List<Long> groupIds = groupService.getUserGroupIdsByUserId(userId);
        if (groupIds == null || groupIds.size() == 0) {
            return new ArrayList<>();
        }
        return permissionService.getPermissionByGroupIdsAndModule(groupIds, module);
    }

    @Override
    public UserDO getUserByUsername(String username) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserDO::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public boolean checkUserExistByUsername(String username) {
        int rows = this.baseMapper.selectCountByUsername(username);
        return rows > 0;
    }

    @Override
    public boolean checkUserExistByEmail(String email) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserDO::getEmail, email);
        int rows = this.baseMapper.selectCount(wrapper);
        return rows > 0;
    }

    @Override
    public boolean checkUserExistById(Long id) {
        int rows = this.baseMapper.selectCountById(id);
        return rows > 0;
    }

    @Override
    public IPage<UserDO> getUserPageByGroupId(Page<UserDO> pager, Long groupId) {
        Long rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        return this.baseMapper.selectPageByGroupId(pager, groupId, rootGroupId);
    }

    @Override
    public Long getRootUserId() {
        Long rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserGroupDO::getGroupId, rootGroupId);
        UserGroupDO userGroupDO = userGroupMapper.selectOne(wrapper);
        return userGroupDO == null ? 0 : userGroupDO.getUserId();
    }

    private void checkGroupsExist(List<Long> ids) {
        for (long id : ids) {
            if (!groupService.checkGroupExistById(id)) {
                throw new NotFoundException("group not found，can't create user", 10023);
            }
        }
    }

    private void checkGroupsValid(List<Long> ids) {
        Long rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        boolean anyMatch = ids.stream().anyMatch(it -> it.equals(rootGroupId));
        if (anyMatch) {
            throw new ForbiddenException("you can't add user to root group", 10073);
        }
    }
}
