package com.sampleclassfiles;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;

public class A{
	
	int i1;
	int i2;
	
	public A(){}
	
	public A(int i1, int i2){
		this.i1 = i1;
		this.i2 = i2;
	}
	
	public double add(double d, double d2){
		return d+d2;
	}
	
	public static int substract(int i, int i2){
		return i-i2;
	}
	
	public int substract(){
		return i1-i2;
	}
}
