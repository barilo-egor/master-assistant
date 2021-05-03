package by.moon.masterassistant;

import by.moon.masterassistant.service.InitializationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MasterAssistantApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MasterAssistantApplication.class, args);
        InitializationService initializationService = context.getBean(InitializationService.class);
        initializationService.initialize();
    }

}
