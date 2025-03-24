package manager;

import manager.business_logic.config.WorkerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WorkerConfig.class)
public class ManagerApplication {

    public static void main(String[] args) {SpringApplication.run(ManagerApplication.class, args);}

}
