package org.viacode.library.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * VIAcode
 * Created by IVolkov on 8/7/2014.
 */

public final class ContextUtil {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        if (applicationContext != null) return applicationContext;
        applicationContext = new ClassPathXmlApplicationContext(new String[] {"dbBeans.xml"});
        return applicationContext;
    }
}
