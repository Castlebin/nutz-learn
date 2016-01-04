package com.heller.nutzbook.module;

import com.heller.nutzbook.bean.User;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpSession;
import java.util.Date;

@IocBean
@At("user")
@Ok("json")
@Fail("http:500")
public class UserModule {

    @Inject
    private Dao dao;

    @At
    public int count() {
        return dao.count(User.class);
    }

    @At
    public Object login(@Param("username") String name, @Param("password") String password, HttpSession session) {
        User user = dao.fetch(User.class, Cnd.where("name", "=", name).and("password", "=", password));
        if (user == null) {
            return false;
        } else {
            session.setAttribute("me", user.getId());
            return true;
        }
    }

    @At
    @Ok(">>:/")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    /**
     * 增加用户
     * eg: http://127.0.0.1:8080/nutzbook/user/add?name=wendal&password=123456
     */
    @At
    public Object add(@Param("..") User user) {
        NutMap re = new NutMap();
        String msg = checkUser(user, true);
        if (msg != null) {
            return re.setv("ok", false)
                    .setv("msg", msg);
        }

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        user = dao.insert(user);

        return re.setv("ok", true)
                .setv("data", user);
    }

    /**
     * 对user进行校验
     */
    private String checkUser(User user, boolean create) {
        if (user == null) {
            return "空对象";
        }
        if (create) {
            if (Strings.isBlank(user.getName()) || Strings.isBlank(user.getPassword())) {
                return "用户名/密码 不能为空";
            }
        } else {
            if (Strings.isBlank(user.getPassword())) {
                return "密码 不能为空";
            }
        }

        String password = user.getPassword();
        if (password.length()<6 || password.length()>12) {
            return "密码长度仅支持6～12位之间！";
        }
        if (create) {
            int count = dao.count(User.class, Cnd.where("name", "=", user.getName()));
            if (count != 0) {
                return "用户名已存在";
            }
        } else {
            if (user.getId() < 1) {
                return "用户Id非法";
            }
        }

        if (user.getName() != null) {
            user.setName(user.getName().trim());
        }

        return null;
    }

}
