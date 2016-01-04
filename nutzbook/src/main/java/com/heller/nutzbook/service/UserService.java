package com.heller.nutzbook.service;

import com.heller.nutzbook.bean.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.random.R;
import org.nutz.service.IdNameEntityService;

import java.util.Date;

@IocBean(fields = "dao")
public class UserService extends IdNameEntityService<User> {

    public User add(String name, String password) {
        Date now = new Date();

        User user = new User();
        user.setName(name);
        user.setSalt(R.UU16());
        user.setPassword(new Sha256Hash(password, user.getSalt()).toHex());
        user.setCreateTime(now);
        user.setUpdateTime(now);

        return dao().insert(user);
    }

    public int fetch(String username, String password) {
        User user = fetch(username);
        if (user == null) {
            return -1;
        }
        String _pass = new Sha256Hash(password, user.getSalt()).toHex();
        if(_pass.equalsIgnoreCase(user.getPassword())) {
            return user.getId();
        }
        return -1;
    }

    public void updatePassword(int userId, String password) {
        User user = fetch(userId);
        if (user == null) {
            return;
        }
        user.setSalt(R.UU16());
        user.setPassword(new Sha256Hash(password, user.getSalt()).toHex());
        user.setUpdateTime(new Date());
        dao().update(user, "^(password|salt|updateTime)$");
    }

}
