package io.github.talelin.latticy.common;



import io.github.talelin.latticy.model.User.ClientUserDO;

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
     * @param user
     */
    public static void setLocalUser(ClientUserDO user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        LocalUser.local.set(map);
    }



    public static ClientUserDO getCmsUser() {
        Map<String, Object> map = LocalUser.local.get();
        return (ClientUserDO) map.get("user");
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
