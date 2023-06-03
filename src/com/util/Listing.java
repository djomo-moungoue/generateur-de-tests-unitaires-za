package com.util;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Listing {

	/**
	 * @param args
	 * @throws NoSuchFieldException 
	 */
		
	public static void main(String[] args) throws NoSuchFieldException {

		Class<? extends Object> clazz = Task.class;
		Object task = null;
		
		try {
			Constructor<? extends Object> constructor = clazz.getConstructor(new Class[] {int.class, long.class});
			task = constructor.newInstance(new Object[] {1,2});
			System.out.println(task);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {//
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		try {
			Field field = clazz.getDeclaredField("priority");
			field.setAccessible(true);
			System.out.println(field.get(task));
			field.set(task, new Long(10));
			System.out.println(field.get(task));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	
		try {
			Method method = clazz.getMethod("setPriority", new Class[] {long.class});//(2)1,2
			method.setAccessible(true);
			method.invoke(task, new Object[] {15});
			Field field = clazz.getDeclaredField("priority");
			field.setAccessible(true);
			System.out.println(field.get(task));
			
		} catch (NoSuchMethodException e) {//(4)
			e.printStackTrace();
		} catch (SecurityException e) {//(5)
			e.printStackTrace();
		} catch (IllegalAccessException e) {//(6)
			e.printStackTrace();
		} catch (IllegalArgumentException e) {//(7)
			e.printStackTrace();
		} catch (InvocationTargetException e) {//(8)
			e.printStackTrace();
		}
	}
}

class Task{
	
	private int id;
	private long priority;
	
	public Task(int id, long priority) {
		this.id = id;
		this.priority = priority;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPriority(long priority) {
		this.priority = priority;
	}

	public int getId() {
		return this.id;
	}

	public long getPriority() {
		return this.priority;
	}
}

