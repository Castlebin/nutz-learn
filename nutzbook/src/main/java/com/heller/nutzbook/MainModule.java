package com.heller.nutzbook;

import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@ChainBy(args="mvc/nutzbook-mvc-chain.js")
@Localization(value="msg/", defaultLocalizationKey="zh-CN")
@Ok("json:full")
@Fail("jsp:jsp.500")
@SetupBy(MainSetup.class)
@IocBy(type = ComboIocProvider.class, args = {
        "*js", "ioc/",
        "*anno", "com.heller.nutzbook",
        "*tx",
        "*org.nutz.integration.quartz.QuartzIocLoader"
})
@Modules(scanPackage = true)
public class MainModule {
}
