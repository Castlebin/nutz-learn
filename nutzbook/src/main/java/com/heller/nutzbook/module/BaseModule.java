package com.heller.nutzbook.module;

import com.heller.nutzbook.service.EmailService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.random.R;

public abstract class BaseModule {

    @Inject
    protected Dao dao;

    @Inject
    protected EmailService emailService;

    protected byte[] emailKEY = R.sg(24).next().getBytes();

}
