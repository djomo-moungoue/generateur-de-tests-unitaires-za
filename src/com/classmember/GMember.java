package com.classmember;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>Die abstrakte Klasse GMember enth&auml;hlt Funktionalit&auml;ten
 * um Merkmale von Konstruktoren und Methoden zu extrahieren.</p>
 * @author SergeOliver
 *
 */
/**
 * @author SergeOliver
 *
 */
public abstract class GMember {

	/**
	 * <p>Deklarierende Klasse der Methode oder des Konstruktors.</p>
	 */
	private Class<?> clazz;

	private Constructor<?> constructor;
	private Method method;
	private String returnType;
	private String label;
	private int numberOfParameter;
	private int numberOfException;
	private String modifier;
	private String[] parameters;
	private String[] exceptions;
	protected String name;
	
	/**
	 * <p>ToolTipTex der Methode oder des Konstruktors</p>
	 */
	protected String view;
	
	/**
	 * <p>erzeugte Instanz durch den Aufruf von newInstance().</p>
	 */
	private Object instance;
	
	/**
	 * <p>R&uuml;ckgabewert des Methodenaufrufs.</p>
	 */
	private Object returnValue;
	
	
	public GMember(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * <p>Soll implementiert werden bei einer Unterklasse, die neue Ojekte
	 * per Reflektion erstelle.</p>
	 * @param arguments ist die Liste von Argumenten.
	 * @return
	 */
	public abstract boolean createInstance(Object...arguments);
	
	/**
	 * <p>Soll implementiert werden bei einer Unterklasse, die Methoden per
	 * Reflektion aufrufen.</p>
	 * @param obj, eine Instanz der deklarierenden Klassen für Instanzmethoden oder
	 *  null für statische Methoden.
	 * @param arguments ist die Liste von Argumenten.
	 */
	public abstract void invoke(Object obj, Object...arguments);
	
	public String getModifier() {
		return this.modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	public void setModifier(int modifier) {
		this.modifier = Modifier.toString(modifier);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		int index = name.lastIndexOf(".");
		this.name = name.substring(index+1);
	}
	
	public String[] getParameters() {
		return this.parameters;
	}
	
	public void setParameters(Type[] genericParam, Class<?>[] param) {
		int index, length = 0;
		length = genericParam.length;
		this.parameters = new String[length];
		for(int i = 0; i < length; i++) {
			if(genericParam[i] instanceof ParameterizedType){
				ParameterizedType argType = (ParameterizedType)genericParam[i];
				Type[] parameterArgTypes = argType.getActualTypeArguments();
				this.parameters[i] = param[i].getSimpleName()+"<";
				if(parameterArgTypes.length == 1) {
					index = String.valueOf(parameterArgTypes[0]).lastIndexOf(".");
					this.parameters[i] += String.valueOf(parameterArgTypes[0]).substring(index+1)+">";
					System.out.println("One parameter");
				}else {
					for(int j = 0; j < parameterArgTypes.length; j++) {
						if(j < (parameterArgTypes.length-1)) {
							index = String.valueOf(parameterArgTypes[j]).lastIndexOf(".");
							this.parameters[i] += String.valueOf(parameterArgTypes[j]).substring(index+1)+", ";
						}else {
							index = String.valueOf(parameterArgTypes[j]).lastIndexOf(".");
							this.parameters[i] += String.valueOf(parameterArgTypes[j]).substring(index+1)+">";
						}
					}
				}
			}else if (param[i].isArray()) {
				this.parameters[i] = param[i].getComponentType().getSimpleName()+"[]";
			}else {
				this.parameters[i] = param[i].getSimpleName();
			}
		}
	}
	
	public String[] getExceptions() {
		return this.exceptions;
	}
	
	public void setExceptions(Class<?>[] exceptions) {
		int index;
		this.exceptions = new String[exceptions.length];
		for(int i = 0; i < exceptions.length; i++) {
			index = String.valueOf(exceptions[i]).lastIndexOf(".");
			this.exceptions[i] = " throws "+String.valueOf(exceptions[i]).substring(index+1);
		}
	}
	
	public int getNumberOfParameter() {
		return this.numberOfParameter;
	}
	public void setNumberOfParameter(int numberOfParameter) {
		this.numberOfParameter = numberOfParameter;
	}
	public int getNumberOfException() {
		return this.numberOfException;
	}
	public void setNumberOfException(int numberOfException) {
		this.numberOfException = numberOfException;
	}
	public String getReturnType() {
		return this.returnType;
	}

	public void setReturnType(Type genericReturnType, Class<?> returnType) {
		int index;
		if(genericReturnType instanceof ParameterizedType){
			ParameterizedType argType = (ParameterizedType)genericReturnType;
			Type[] parameterArgTypes = argType.getActualTypeArguments();
			this.returnType = returnType.getSimpleName()+"<";
			if(parameterArgTypes.length == 1) {
				index = String.valueOf(parameterArgTypes[0]).lastIndexOf(".");
				this.returnType += String.valueOf(parameterArgTypes[0]).substring(index+1)+">";
			}else {
				for(int j = 0; j < parameterArgTypes.length; j++) {
					if(j < (parameterArgTypes.length-1)) {
						index = String.valueOf(parameterArgTypes[j]).lastIndexOf(".");
						this.returnType += String.valueOf(parameterArgTypes[j]).substring(index+1)+", ";
					}else {
						index = String.valueOf(parameterArgTypes[j]).lastIndexOf(".");
						this.returnType += String.valueOf(parameterArgTypes[j]).substring(index+1)+">";
					}
				}
			}
		}else if (returnType.isArray()) {
			this.returnType = returnType.getComponentType().getSimpleName()+"[]";
		}else {
			this.returnType = returnType.getSimpleName();
		}
	}

	public Class<?> getClazz() {
		return this.clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Method getMethod() {
		return this.method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	public Constructor<?> getConstructor() {
		return this.constructor;
	}

	public void setConstructor(Constructor<?> constructor) {
		this.constructor = constructor;
	}

	/**
	 * @return die Instanz erzeugt durch den Aufruf von newInstance().

	 */
	public Object getInstance() {
		return this.instance;
	}

	/**
	 * @return ToolTipTex der Methode oder des Konstruktors
	 */
	public String getView() {
		return this.view;
	}

	/**
	 * @param view: ToolTipTex der Methode oder des Konstruktors
	 */
	public void setView(String view) {
		this.view = view;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getReturnValue() {
		return this.returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

	public void setExceptions(String[] exceptions) {
		this.exceptions = exceptions;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}
}
