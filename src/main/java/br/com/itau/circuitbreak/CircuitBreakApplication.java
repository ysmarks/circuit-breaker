package br.com.itau.circuitbreak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@SpringBootApplication
public class CircuitBreakApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean<>(new HystrixMetricsStreamServlet(), "/hystrix.stream");
	}
}
