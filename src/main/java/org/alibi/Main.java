package org.alibi;

import org.alibi.config.DatabaseConfig;
import org.alibi.config.WebConfig;
import org.apache.catalina.LifecycleException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Main implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(DatabaseConfig.class, WebConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    public static void main(String[] args) throws LifecycleException {
        org.apache.catalina.startup.Tomcat tomcat = new org.apache.catalina.startup.Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();
        tomcat.addWebapp("/", new java.io.File("src/main/webapp").getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
