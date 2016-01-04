package com.heller.nutzbook.service;

import org.apache.commons.mail.HtmlEmail;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean(name = "emailService")
public class EmailServiceImpl implements EmailService {

    private static final Log log = Logs.get();

    @Inject("refer:$ioc")
    protected Ioc ioc;

    @Override
    public boolean send(String to, String subject, String html) {
        try {
            HtmlEmail email = ioc.get(HtmlEmail.class);
            email.setSubject(subject);
            email.setHtmlMsg(html);
            email.addTo(to);
            email.buildMimeMessage();

            email.sendMimeMessage();

            return true;
        } catch (Throwable e) {
            log.error("send email fail", e);
            return false;
        }
    }

}
