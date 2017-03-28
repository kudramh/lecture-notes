package org.kudram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class BootrestApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootrestApplication.class, args);
	}
}

@RestController
class GreetingController{
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    private Environment env;
    @Autowired
    GreetingController(Environment env){
        this.env = env;
        logger.info("--->Env: "+env);
    }

    @RequestMapping("/")
    Greet greet(){
        logger.info("bootrest.custom.property "+ env.getProperty("bootrest.custom.property"));
        return new Greet("Hello World! "+env.getProperty("bootrest.custom.property"));
    }
}
class Greet {
    private String message;
    public Greet() {
    }
    public Greet(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
