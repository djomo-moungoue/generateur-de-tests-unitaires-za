package com.sampleclassfiles;

public class Task {
	
	private static int counter = 0;
	private int id = 0;
	private int priority = 0;
	
	public Task() {
		id = ++counter;
		priority = id;
	}
	
	public boolean equals(Task task) {
		return this.priority == task.priority;
	}
	
	@Override
	public String toString() {
		return "com.sampleclassfiles.Task@"+id;
	}

	public String info() {
		return "("+id+"|"+priority+")";
	}

	public static int getCounter() {
		return counter;
	}

	public static void incCounter() {
		++Task.counter ;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
