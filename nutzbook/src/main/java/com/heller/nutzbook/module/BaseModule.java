package com.heller.nutzbook.module;

import com.heller.nutzbook.service.EmailService;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;

import java.util.List;

public abstract class BaseModule {

    @Inject
    protected Dao dao;

    @Inject
    protected EmailService emailService;

    protected byte[] emailKEY = R.sg(24).next().getBytes();

    protected QueryResult query(Class<?> klass, Condition cnd, Pager pager) {
        if (pager != null && pager.getPageNumber() < 1) {
            pager.setPageNumber(1);
        }
        List<?> objects = dao.query(klass, cnd, pager);
        dao.fetchLinks(objects, null);
        pager.setRecordCount(dao.count(klass, cnd));
        
        return new QueryResult(objects, pager);
    }

    protected NutMap ajaxOk(Object data) {
        return new NutMap().setv("ok", true).setv("data", data);
    }

    protected NutMap ajaxFail(String msg) {
        return new NutMap().setv("ok", false).setv("msg", msg);
    }

}
