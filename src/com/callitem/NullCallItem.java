/**
 * 
 */
package com.callitem;

import java.util.Arrays;

/**
 * <p>Die Klasse NullCallItem entspricht die Eingabe eines Null-Pointer als
 * Argument</p>
 * @author SergeOliver
 *
 */
public class NullCallItem extends CallItem{
	
	private int order;
	private static boolean isDuplicate = false;
	private static final NullCallItem instance = new NullCallItem();
	
	/**
	 * @return new NullCallItem()
	 */
	public static NullCallItem getInstance() {
		return instance;
	}
	
	private NullCallItem() {
		super("null",new Object[]{});
		order = 0;
		setClazz(Null.class);
		setOutput(new Null());
	}
	
	@Override
	public String toString() {
		return "NullCallItem [order=" + order + ", clazz= " +getClazz() + ", inputs="
				+ Arrays.toString(getInputs()) + ", callingView=" + getCallingView() + ", output=" + getOutput() + "]";
	}

	public static boolean isDuplicate() {
		return isDuplicate;
	}

	public static void setDuplicate(boolean isDuplicate) {
		NullCallItem.isDuplicate = isDuplicate;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	class Null{
		Null(){}
		
		@Override
		public String toString() {
			return "null";
		}
	}
}

