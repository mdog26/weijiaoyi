package springboot;

import org.springframework.context.ApplicationContext;

/**
 * Created by Transn on 2017-1-11.
 */
public class Beans {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    //通过class获取Bean.  
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean  
    public static <T> T getBean(String name,Class<T> clazz){
        return applicationContext.getBean(name, clazz);
    }
}
