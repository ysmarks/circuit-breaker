package br.com.itau.circuitbreak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
//@EnableHystrix
public class CircuitBreakController {

	@Autowired
	private CircuitBreakService template;
	
	@Autowired
	private CircuitBreakerClientSpringService cliente;
	
	@HystrixCommand(fallbackMethod = "mostraPadariaFarmaciaFallback")
	@GetMapping("circuitBreak")
	public String circuitBreakEndpoints() {
		String padariaService = template.template().getForObject("http://localhost:8181/padaria/pao", String.class);
		String farmaciaService = template.template().getForObject("http://localhost:8282/farmacia/medicamento", String.class);
		return padariaService;
	}
	@GetMapping("com")
	public String comCircuitBreaker() throws InterruptedException {
		return cliente.invocarServicoComHystrix();
	}
	@GetMapping("sem")
	public String semCircuitBreaker() throws InterruptedException {
		return cliente.invocarServicoSemHystrix();
	}
	public String mostraPadariaFarmaciaFallback() {
		return "Conexão falhou...";
	}
}
