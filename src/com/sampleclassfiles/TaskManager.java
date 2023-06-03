package com.sampleclassfiles;

public class TaskManager {
	
	private int defaultPriority = 2;
	
	public TaskManager(Task task) {
		task.setPriority(defaultPriority);
	}
	
	public TaskManager(Task task, int newPrio) {
		task.setPriority(newPrio);
	}

}
