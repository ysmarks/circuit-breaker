package br.com.itau.circuitbreak;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

@Component
@Aspect
public class CircuitBreakAdapter {

	private HystrixCommand.Setter config;
	private HystrixCommandProperties.Setter commandProperties;
	private HystrixThreadPoolProperties.Setter threadPoolProperties;
	
	@Value("${circuitbreak.service.execution.timeout}")
    private int executionTimeout;

    @Value("${circuitbreak.service.sleepwindow}")
    private int sleepWindow;

    @Value("${circuitbreak.service.threadpool.maxsize}")
    private int maxThreadCount;

    @Value("${circuitbreak.service.threadpool.coresize}")
    private int coreThreadCount;

    @Value("${circuitbreak.service.task.queue.size}")
    private int queueCount;

    @Value("${circuitbreak.service.group.key}")
    private String groupKey;

    @Value("${circuitbreak.service.key}")
    private String key;
    
    @Around("@annotation(br.com.itau.circuitbreak.HystrixCircuitBreaker)")
    public Object circuitBreakerAround(final ProceedingJoinPoint joinPoint) {
    	
    	return new RemoteServiceCommand(config, joinPoint).execute();
    }
    @PostConstruct
    public void setup() {
    	
    	this.config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey));
    	this.config = config.andCommandKey(HystrixCommandKey.Factory.asKey(key));

        this.commandProperties = HystrixCommandProperties.Setter();
        this.commandProperties.withExecutionTimeoutInMilliseconds(executionTimeout);
        this.commandProperties.withCircuitBreakerSleepWindowInMilliseconds(sleepWindow);

        this.threadPoolProperties = HystrixThreadPoolProperties.Setter();
        this.threadPoolProperties.withMaxQueueSize(maxThreadCount).withCoreSize(coreThreadCount).withMaxQueueSize(queueCount);

        this.config.andCommandPropertiesDefaults(commandProperties);
        this.config.andThreadPoolPropertiesDefaults(threadPoolProperties);
    	
    }
    private static class RemoteServiceCommand extends HystrixCommand<String> {

        private final ProceedingJoinPoint joinPoint;

        RemoteServiceCommand(final Setter config, final ProceedingJoinPoint joinPoint) {
            super(config);
            this.joinPoint = joinPoint;
        }

        @Override
        protected String run() throws Exception {
            try {
                return (String) joinPoint.proceed();
            } catch (final Throwable th) {
                throw new Exception(th);
            }

        }
    }
}
