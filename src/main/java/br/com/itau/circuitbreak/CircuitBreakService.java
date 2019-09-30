package br.com.itau.circuitbreak;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CircuitBreakService {

	@Bean
	public RestTemplate template() {
		
		return new RestTemplate();
	}
}
