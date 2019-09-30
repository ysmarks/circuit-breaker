package br.com.itau.circuitbreak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("circuitBreakerClient")
public class CircuitBreakerClientSpringService {

	@Value("${circuitbreak.service.timout}")
	private int remoteServiceDelay;
	
	@HystrixCircuitBreaker
	public String invocarServicoComHystrix() throws InterruptedException {
		 return new RemoteServiceSimulatorTest(remoteServiceDelay).executa();
	}
	
	public String invocarServicoSemHystrix() throws InterruptedException {
		 return new RemoteServiceSimulatorTest(remoteServiceDelay).executa();
	}
}
