     package com.api.Coau;  // Mude de "com.api.Coau" para "com.api.coau"

     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

    
    

@SpringBootApplication
public class CoauApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        // Força o Java a ignorar periféricos de vídeo/monitor
        System.setProperty("java.awt.headless", "true"); 
        SpringApplication.run(CoauApplication.class, args);
    }
}

     /* @SpringBootApplication
     public class CoauApplication extends SpringBootServletInitializer {
         public static void main(String[] args) {
             SpringApplication.run(CoauApplication.class, args);
         }
      }*/
         
    
     