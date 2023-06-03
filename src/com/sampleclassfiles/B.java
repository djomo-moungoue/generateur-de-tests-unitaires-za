package com.sampleclassfiles;

public class B {
	
	private A a;
	
	public B(){}
	
	public B(A a) {
		this.a = a;
	}
	
	public B(int i, A a, double d){
		this.a = a;
	}
	
	public int add(int i, int i2, int i3) {
		return (int)a.add(i, i2)+i3;
	}
	
	public double add(int i1, int i2) {
		return i1+i2;
	}
}
