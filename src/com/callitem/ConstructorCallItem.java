/**
 * 
 */
package com.callitem;

import java.util.Arrays;

import com.application.GFrame;



/**
 * <p>Die Klasse ConstructorCallItem entpricht den Aufruf eines Konstruktors
 * auf die GUI und dient zur Wiederherstellung dieses Aufrufen w&auml;hrend
 * der Generierung der Testklasse.</p>
 * @author SergeOliver
 *
 */
public class ConstructorCallItem extends CallItem{

	private static final long serialVersionUID = 1L;
	private int order;
	
	/**
	 * @param clazz deklarierende Klasse
	 * @param callingView Benutzersicht des Aufrufs
	 * @param instance erstellte Instanz
	 * @param arguments Liste von Argumenten
	 */
	public ConstructorCallItem(Class<?> clazz, String callingView, Object instance, 
			Object ... inputs) {
		super(callingView,inputs);
		setClazz(clazz);
		GFrame.getInstance().incCallOrder();
		order = GFrame.getInstance().getCallOrder();
		GFrame.getInstance().incConstructorCallOrder();
		setConstructorCallID(GFrame.getInstance().getConstructorCallOrder());
		setOutput(instance);
	}

	@Override
	public String toString() {
		return "ConstructorCallItem [order=" + order + ", constructID= " +getConstructorCallID() + ", clazz= " +getClazz() + ", inputs="
				+ Arrays.toString(getInputs()) + ", callingView=" + getCallingView() + ", output=" + getOutput() + "]";
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
