package io.github.talelin.latticy.common;

import io.github.talelin.latticy.model.User.UserDO;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程安全的当前登录用户，如果用户为登录，则得到 null
 *
 * @author pedro@TaleLin
 */
public class LocalUser {

    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<>();


    /**
     * 设置登录用户
     *
     * @param user user
     */
    public static void setLocalUser(UserDO user) {
        Map<String, Object> map = new HashMap<>();
        map.put("cms_user", user);
        LocalUser.local.set(map);
    }


    public static UserDO getCmsUser() {
        Map<String, Object> map = LocalUser.local.get();
        return (UserDO) map.get("cms_user");
    }



    public static <T> T getLocalUser(Class<T> clazz) {
        return (T) local.get();
    }

    /**
     * 清理当前用户
     */
    public static void clearLocalUser() {
        LocalUser.local.remove();
    }
}
