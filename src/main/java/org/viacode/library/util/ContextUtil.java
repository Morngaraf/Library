package org.viacode.library.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * VIAcode
 * Created by IVolkov on 8/7/2014.
 */

@Deprecated
public final class ContextUtil {

    private static ApplicationContext applicationContext, i18nContext;

    public static ApplicationContext getApplicationContext() throws BeansException {
        if (applicationContext != null) return applicationContext;
        applicationContext = new ClassPathXmlApplicationContext(new String[] {"dbContext.xml"});
        return applicationContext;
    }

    public static ApplicationContext getI18nContext() throws BeansException {
        if (i18nContext != null) return i18nContext;
        i18nContext = new ClassPathXmlApplicationContext(new String[] {"src/main/webapp/WEB-INF/i18nContext.xml"});

        return i18nContext;
    }
}
