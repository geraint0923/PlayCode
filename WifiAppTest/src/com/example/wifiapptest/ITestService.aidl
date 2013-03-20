package com.example.wifiapptest;

interface ITestService {
	void tick();
	
	void startTriggerTester(int count, int para);
	void startBatchTester(int count, int para);
}