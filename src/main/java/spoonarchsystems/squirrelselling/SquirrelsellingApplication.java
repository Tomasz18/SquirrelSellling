package spoonarchsystems.squirrelselling;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication(scanBasePackages = { "spoonarchsystems" })
public class SquirrelsellingApplication {

	@Bean
	public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
		return emf.unwrap(SessionFactory.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SquirrelsellingApplication.class, args);
	}
}
