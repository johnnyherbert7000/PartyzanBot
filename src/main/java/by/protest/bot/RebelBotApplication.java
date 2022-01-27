package by.protest.bot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RebelBotApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RebelBotApplication.class, args);
	}

}