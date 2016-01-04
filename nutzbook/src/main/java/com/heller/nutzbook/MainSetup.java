package com.heller.nutzbook;

import com.heller.nutzbook.bean.User;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import java.util.Date;

public class MainSetup implements Setup {
    @Override
    public void init(NutConfig conf) {
        Ioc ioc = conf.getIoc();
        Dao dao = ioc.get(Dao.class);
        Daos.createTablesInPackage(dao, "com.heller.nutzbook", false);

        // 初始化，新建默认的根用户
        if (dao.count(User.class) == 0) {
            User user = new User();
            user.setName("admin");
            user.setPassword("123456");
            Date now = new Date();
            user.setCreateTime(now);
            user.setUpdateTime(now);

            dao.insert(user);
        }
    }

    @Override
    public void destroy(NutConfig conf) {

    }
}
