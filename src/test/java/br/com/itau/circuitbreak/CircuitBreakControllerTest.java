package br.com.itau.circuitbreak;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.netflix.hystrix.exception.HystrixRuntimeException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = CircuitBreakApplication.class)
public class CircuitBreakControllerTest {
	
	@Autowired
	private CircuitBreakController controller;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	
	@Test
	public void com() throws InterruptedException {
		
		exception.expect(HystrixRuntimeException.class);
		controller.comCircuitBreaker();
	}
	
	@Test
	public void sem() throws InterruptedException {
		assertThat(controller.semCircuitBreaker(), equalTo("Sucesso"));
	}
	

}
