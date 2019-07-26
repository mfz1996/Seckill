package test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-web.xml");
        ComboPooledDataSource dataSource = (ComboPooledDataSource) context.getBean("dataSource");
        System.out.println(dataSource.getJdbcUrl());
    }
}
