package launch;

import java.io.File;

//import org.apache.catalina.startup.Tomcat;

import com.kagubuzz.utilities.JavaFileUtilities;

public class Main {/*
    
   public static void main(String[] args) throws Exception {

        System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());

        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory() / 1024);

        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        

        System.out.println("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));


        System.out.println("Total memory (bytes): " + Runtime.getRuntime().totalMemory() / 1024);

        String webappDirLocation = "src/main/webapp/";
        
        Tomcat tomcat = new Tomcat();

        // The port that we should run on can be set into an environment
        // variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        tomcat.setPort(Integer.valueOf(webPort));

        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
        tomcat.addWebapp("/images", JavaFileUtilities.getTempFilePath());
        tomcat.addWebapp("/KaguBuzzUserDirectories", JavaFileUtilities.getTempFilePath());
        tomcat.start();
        tomcat.getServer().await();
    }*/
}