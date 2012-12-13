package com.msra.elechecker;



public interface ICamera {
	void takePhoto(String path);
	void setCallback(Checker cr);
}