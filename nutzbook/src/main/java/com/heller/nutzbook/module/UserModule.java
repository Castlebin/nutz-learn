package com.heller.nutzbook.module;

import com.heller.nutzbook.bean.User;
import com.heller.nutzbook.bean.UserProfile;
import com.heller.nutzbook.service.UserService;
import com.heller.nutzbook.util.Toolkit;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Scope;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpSession;

@Filters(@By(type = CheckSession.class, args = {"me", "/"})) // 如果session中没有me这个属性，就跳转到/
@IocBean
@At("/user")
@Ok("json:{locked:'password|salt', ignoreNull:true}") // 密码和salt也不可以发送到浏览器去
@Fail("http:500")
public class UserModule extends BaseModule {

    @Inject
    protected UserService userService;

    @At
    public int count() {
        return dao.count(User.class);
    }

    @Filters //为login方法设置为空的过滤器，否则无法登录（类上有一个CheckSession的过滤器）
    @At
    public Object login(@Param("username") String username,
                        @Param("password") String password,
                        @Param("captcha") String captcha,
                        @Attr(scope=Scope.SESSION, value="nutz_captcha") String _captcha,
                        HttpSession session) {
        NutMap re = new NutMap();
        if (!Toolkit.checkCaptcha(_captcha, captcha)) {
            return re.setv("ok", false).setv("msg", "验证码错误");
        }
        int userId = userService.fetch(username, password);
        if (userId < 0) {
            return re.setv("ok", false).setv("msg", "用户名或密码错误");
        } else {
            session.setAttribute("me", userId);
            return re.setv("ok", true);
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
    public Object add(@Param("..")User user) { // 两个点号是按对象属性一一设置
        NutMap re = new NutMap();
        String msg = checkUser(user, true);
        if (msg != null){
            return re.setv("ok", false).setv("msg", msg);
        }
        user = userService.add(user.getName(), user.getPassword());

        return re.setv("ok", true)
                 .setv("data", user);
    }

    @At
    public Object update(@Param("id") int id,
                         @Param("password")String password,
                         @Attr("me")int me) {
        if (id != me && me != 1) {// id=1 is admin
            return new NutMap().setv("ok", false).setv("msg", "你没有权限修改其他人的密码");
        }
        if (Strings.isBlank(password) || password.length() < 6) {
            return new NutMap().setv("ok", false).setv("msg", "密码不符合要求");
        }

        userService.updatePassword(id, password);

        return new NutMap().setv("ok", true);
    }

    @At
    @Aop(TransAop.READ_COMMITTED)
    public Object delete(@Param("id") int id,
                         @Attr("me") int me) {
        if (me == id) {
            return new NutMap().setv("ok", false)
                    .setv("msg", "不能删除当前用户！");
        }

        dao.delete(User.class, id); // 再严谨一些的话,需要判断是否为>0
        // 删除User的时候也需要删除对应的UserProfile
        dao.clear(UserProfile.class, Cnd.where("userId", "=", me));

        return new NutMap().setv("ok", true);
    }

    /**
     * 查询，带分页：
     * eg: http://127.0.0.1:8080/nutzbook/user/query?pageNumber=1&pageSize=2
     */
    @At
    public Object query(@Param("name") String name,
                        @Param("..") Pager pager) {
        Cnd cnd = Strings.isBlank(name)? null : Cnd.where("name", "like", "%"+name+"%");
        QueryResult qr = new QueryResult();
        qr.setList(dao.query(User.class, cnd, pager));
        pager.setRecordCount(dao.count(User.class, cnd));
        qr.setPager(pager);  // 分页还不够智能啊。不过挺清晰的，这样也不错

        return qr;
    }

    @GET
    @At("/login") // /user/login
    @Filters
    @Ok("jsp:jsp.user.login") // 访问这个/user/login路径的GET请求, 将会转发到 /WEB-INF/jsp/user/login.jsp
    public void loginPage() {
    }

    @At("/")
    @Ok("jsp:jsp.user.list") // 真实路径是 /WEB-INF/jsp/user/list.jsp
    public void index() {
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
