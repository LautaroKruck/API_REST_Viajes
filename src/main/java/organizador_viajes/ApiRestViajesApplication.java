package organizador_viajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import organizador_viajes.security.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ApiRestViajesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestViajesApplication.class, args);
	}

}
