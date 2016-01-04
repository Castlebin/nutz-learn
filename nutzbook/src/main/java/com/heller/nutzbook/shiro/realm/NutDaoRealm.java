package com.heller.nutzbook.shiro.realm;

import com.heller.nutzbook.bean.Permission;
import com.heller.nutzbook.bean.Role;
import com.heller.nutzbook.bean.User;
import com.heller.nutzbook.util.Toolkit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.shiro.CaptchaUsernamePasswordToken;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

/**
 * 用NutDao来实现Shiro的Realm
 * <p/> 在Web环境中通过Ioc来获取NutDao
 *
 * 因为shiro先于NutFilter初始化,所以nutz ioc容器没法直接提供支持
 * 所以在Shiro中无法使用nutz ioc注解
 */
public class NutDaoRealm extends AuthorizingRealm {

    protected Dao dao;

    public NutDaoRealm() {
        super(null, null);
    }

    public NutDaoRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
        setAuthenticationTokenClass(CaptchaUsernamePasswordToken.class);
    }

    public NutDaoRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public NutDaoRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        int userId = (Integer) principals.getPrimaryPrincipal();
        User user = dao().fetch(User.class, userId);
        if (user == null) {
            return null;
        }
        if (user.isLocked()) {
            throw new LockedAccountException("Account [" + user.getName() + "] is locked.");
        }

        SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
        user = dao().fetchLinks(user, null);
        if (user.getRoles() != null) {
            dao().fetchLinks(user.getRoles(), null);
            for (Role role : user.getRoles()) {
                auth.addRole(role.getName());
                if (role.getPermissions() != null) {
                    for (Permission p : role.getPermissions()) {
                        auth.addStringPermission(p.getName());
                    }
                }
            }
        }

        if (user.getPermissions() != null) {
            for (Permission p : user.getPermissions()) {
                auth.addStringPermission(p.getName());
            }
        }

        return auth;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        CaptchaUsernamePasswordToken upToken = (CaptchaUsernamePasswordToken) token;

        if (Strings.isBlank(upToken.getCaptcha())) {
            throw new AuthenticationException("验证码不能为空");
        }

        String _captcha = Strings.sBlank(SecurityUtils.getSubject()
                .getSession(true).getAttribute(Toolkit.captcha_attr));
        if (!upToken.getCaptcha().equalsIgnoreCase(_captcha)) {
            throw new AuthenticationException("验证码错误");
        }

        User user = dao().fetch(User.class, Cnd.where("name", "=", upToken.getUsername()));
        if (user == null) {
            return null;
        }
        if (user.isLocked()) {
            throw new LockedAccountException("Account [" + upToken.getUsername() + "] is locked.");
        }

        SimpleAccount account = new SimpleAccount(user.getId(), user.getPassword(), getName());
        account.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));

        return account;
    }

    public Dao dao() {
        if (dao == null) {
            dao = Mvcs.ctx().getDefaultIoc().get(Dao.class, "dao");
            return dao;
        }

        return dao;
    }

}
