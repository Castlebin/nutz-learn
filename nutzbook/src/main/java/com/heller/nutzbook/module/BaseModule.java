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

    // 将邮箱验证key临时保存在内存中，正式使用时，应该保存在数据库中
    protected byte[] emailKEY = R.sg(24).next().getBytes();

}
