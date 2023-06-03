package com.callitem;

import java.util.Arrays;

import com.application.GFrame;

/**
 * <p>Die Klasse MethodCallItem entpricht den Aufruf einer Methode auf die
 * GUI und dient zur Wiederherstellung dieses Aufrufen w&auml;hrend
 * der Generierung der Testklasse.</p>
 * @author SergeOliver
 *
 */
public class MethodCallItem extends CallItem{

	private static final long serialVersionUID = 1L;
	private int order;
	
	/**
	 * @param name Name der Methode
	 * @param clazz deklarierende Klasse
	 * @param callingView Benutzersicht des Aufrufs
	 * @param returnValue R&uuml;ckgabewert des Aufrufs
	 * @param object worauf die Methode aufgerufen wurde
	 * @param arguments Liste der Argumente
	 */
	public MethodCallItem(Class<?> clazz, String name, String callingView, Object returnValue, 
			Object object, Object ... inputs) {
		super(callingView,inputs);
		GFrame.getInstance().incCallOrder();
		order = GFrame.getInstance().getCallOrder();
		setClazz(clazz);
		setName(name);
		setObject(object);
		setOutput(returnValue);
	}
	
	@Override
	public String toString() {
		return "MethodCallItem [order=" + order + ", clazz= " +getClazz()+ ", name="+getName()+", inputs="
				+ Arrays.toString(getInputs()) + ", callingView=" + getCallingView() + ", object="
				+ getObject() + ", output=" + getOutput() + "]";
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
