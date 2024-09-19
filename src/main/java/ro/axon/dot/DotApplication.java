package ro.axon.dot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude =
		{org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class DotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotApplication.class, args);
	}

}
