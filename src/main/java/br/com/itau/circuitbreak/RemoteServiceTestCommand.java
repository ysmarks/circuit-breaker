package br.com.itau.circuitbreak;

import com.netflix.hystrix.HystrixCommand;

public class RemoteServiceTestCommand extends HystrixCommand<String>{

	private RemoteServiceSimulatorTest remoteService;
	
	RemoteServiceTestCommand(Setter config, RemoteServiceSimulatorTest remoteService) {
		super(config);
		this.remoteService = remoteService;
	}
	@Override
	protected String run() throws Exception {
		
		return remoteService.executa();
	}

}
