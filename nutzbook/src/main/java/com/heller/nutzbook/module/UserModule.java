package com.heller.nutzbook.module;

import com.heller.nutzbook.bean.User;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

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

}
