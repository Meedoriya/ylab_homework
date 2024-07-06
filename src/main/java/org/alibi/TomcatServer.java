package org.alibi;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class TomcatServer {
    public static void main(String[] args) throws LifecycleException {
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

        System.out.println("Configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
