package com.sampleclassfiles;

public class Add{
	
	int result = 0;
	
	public int getResult() {
		return this.result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Add() {}
	
	public Add(A a, B b) {}
	
	public int add(int a, int b){ 
		result = a+b;
		return result;
	}
	
	public static int add2(int a, int b){ 
		return a+b;
	}
	
	
}
