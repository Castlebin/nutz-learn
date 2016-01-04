package com.heller.nutzbook;

import com.heller.nutzbook.bean.User;
import org.apache.commons.mail.HtmlEmail;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.integration.quartz.NutQuartzCronJobFactory;
import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import java.util.Date;

public class MainSetup implements Setup {

    private static final Log log = Logs.get();

    private Ioc ioc;

    @Override
    public void init(NutConfig conf) {
        ioc = conf.getIoc();
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

        // 获取NutQuartzCronJobFactory, 从而触发计划任务的初始化与启动
        ioc.get(NutQuartzCronJobFactory.class);

        // 测试发送邮件
    //  testSendEmail();
    }

    @Override
    public void destroy(NutConfig conf) {

    }

    private void testSendEmail() {
        // 测试发送邮件
        try {
            HtmlEmail email = ioc.get(HtmlEmail.class);
            email.setSubject("测试NutzBook");
            email.setMsg("This is a test mail ... :-)" + System.currentTimeMillis());
            email.addTo("o2o_notify@126.com");//请务必改成您自己的邮箱啊!!!
            email.buildMimeMessage();
            email.sendMimeMessage();

            log.info("Send test mail success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
