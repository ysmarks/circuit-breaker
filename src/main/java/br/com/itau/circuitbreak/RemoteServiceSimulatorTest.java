package br.com.itau.circuitbreak;

public class RemoteServiceSimulatorTest {

	private long espera;
	
	RemoteServiceSimulatorTest(long espera) throws InterruptedException {
		this.espera = espera;
	}
	
	public String executa() throws InterruptedException {
		Thread.sleep(espera);
		
		return "Sucesso";
	}
}
