package br.com.itau.circuitbreak;

import org.junit.Test;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CircuitBreakerComandosTest {

	@Test
    public void givenCircuitBreakerSetup__whenRemoteSvcCmdExecuted_thenReturnSuccess()
      throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));
        
        HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
        properties.withExecutionTimeoutInMilliseconds(10000);

        properties.withCircuitBreakerSleepWindowInMilliseconds(5000);
        properties.withExecutionIsolationStrategy(
          HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        properties.withCircuitBreakerEnabled(true);
        properties.withCircuitBreakerRequestVolumeThreshold(5);

        config.andCommandPropertiesDefaults(properties);

        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
          .withMaxQueueSize(5)
          .withCoreSize(5)
          .withQueueSizeRejectionThreshold(5));

        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
        Thread.sleep(5000);

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceSimulatorTest(500)).execute(),
          equalTo("Sucesso"));
        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceSimulatorTest(500)).execute(),
          equalTo("Sucesso"));
        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceSimulatorTest(500)).execute(),
          equalTo("Sucesso"));
    }
	public String invokeRemoteService(HystrixCommand.Setter config, int timeout)
		      throws InterruptedException {
		        String response = null;
		        try {
		            response = new RemoteServiceTestCommand(config,
		              new RemoteServiceSimulatorTest(timeout)).execute();
		        } catch (HystrixRuntimeException ex) {
		            System.out.println("ex = " + ex);
		        }
		        return response;
		    }
}
