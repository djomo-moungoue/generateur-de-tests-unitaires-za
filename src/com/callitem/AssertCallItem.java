package com.callitem;

import java.util.Arrays;

import com.application.GFrame;

/**
 * <p>Die Klasse AssertCallItem entpricht den Aufruf einer assert-Methode
 * auf die GUI und dient zur Wiederherstellung dieses Aufrufen w&auml;hrend
 * der Generierung der Testklasse.</p>
 * @author SergeOliver
 *
 */
public class AssertCallItem extends CallItem{
	
	private static final long serialVersionUID = 1L;
	private int order;
	
	/**
	 * @param name Name der Assertion
	 * @param callingView Benutzersicht des Aufrufs
	 * @param arguments Liste der Argumenten
	 */
	public AssertCallItem(String name, String callingView, Object ... inputs) {
		super(callingView,inputs);
		GFrame.getInstance().incCallOrder();
		order = GFrame.getInstance().getCallOrder();
		GFrame.getInstance().incAssertCallOrder();
		setAssertCallID(GFrame.getInstance().getAssertCallOrder());
		setName(name);
	}
	
	@Override
	public String toString() {
		return "AssertCallItem [order=" + order + ", assertID = " +getAssertCallID()+  ", name="+getName()+", inputs="
				+ Arrays.toString(getInputs()) + ", callingView=" + getCallingView() + "]";
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
