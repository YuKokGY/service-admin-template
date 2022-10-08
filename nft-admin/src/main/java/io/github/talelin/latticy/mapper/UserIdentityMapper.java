package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.User.UserIdentityDO;
import org.springframework.stereotype.Repository;

/**
 * @author pedro@TaleLin
 */
@Repository
public interface UserIdentityMapper extends BaseMapper<UserIdentityDO> {

}
