package com.sampleclassfiles;

public class Operation{

	private double operand1 = 0;
	private double operand2 = 0;
	
	public Operation(){}

	public Operation(double p1){
		operand1 = p1;
	}
	
	public Operation(Operation operation, double p){
		operation.operand1 = p;
	}
	
	public double square(double d){
		return d*d;
	}
	
	public static double add(double p1, double p2){
		return p1 + p2;
	}
	
	public double substract(int a, int b){
		return a-b;
	}
	
	public static double substract(double p1, double p2){
		return p1 - p2;
	}
	
	public double multiply(double op1, double op2){
		return op1 * op2;
	}
	
	public String toString(){
		return "Operation: ("+operand1+" , "+operand2+")";
	}

	public double getOperand1() {
		return this.operand1;
	}

	public void setOperand1(double operand1) {
		this.operand1 = operand1;
	}

	public double getOperand2() {
		return this.operand2;
	}

	public void setOperand2(double operand2) {
		this.operand2 = operand2;
	}
}