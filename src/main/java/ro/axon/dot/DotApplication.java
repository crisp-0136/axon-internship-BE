package ro.axon.dot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(exclude =
		{org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class DotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotApplication.class, args);
	}

}
