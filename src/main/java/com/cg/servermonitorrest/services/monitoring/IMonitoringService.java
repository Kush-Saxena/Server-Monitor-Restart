package com.cg.servermonitorrest.services.monitoring;

public interface IMonitoringService {

	void initialize();
	void analyze();
	void restart();
	void report();
	void cleanup();
	
}
