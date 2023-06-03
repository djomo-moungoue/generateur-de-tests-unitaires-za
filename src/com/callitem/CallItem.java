/**
 * 
 */
package com.callitem;

import com.application.GFrame;
import com.util.GUtil;


/**
 * <p>Die Klasse CallItem umfasst Funktionalit&auml;ten um die Aufrufe
 *  des Benutzers in einer JUnit Testklasse umzuwandeln.</p>
 * @author SergeOliver
 *
 */
public abstract class CallItem{

	/**
	 * <p>Klasse der aufgerufenen Methode oder des aufgerufenen Kons-
	 * truktors.</p>
	 */
	private Class<?> clazz;

	/**<p>Liste von Argumenten, die entweder primitive Typen oder 
	 * CallItems sind.</p>
	 * 
	 */
	private Object[] inputs;
	
	/**
	 * <p>Name der aufgerufenen Methode oder Assertion.</p>
	 */
	private String name;
	
	/**
	 * <p>Benutzersicht des Aufrufs einer Methode, eines Konstructors
	 * oder einer Assertion in der Benutzeroberfl&auml;che.</p>
	 */
	private String callingView;
	
	/**
	 * <p>Wiederherstellung des Aufrufs einer Methode, eines Konstructors
	 * oder einer Assertion in der Testdatei.</p>
	 */
	private String restoredCall;
	
	/**
	 *<p>Erzeugte Instanz oder R&uuml;ckgabewert. Es is entweder ein pri-
	 *mitiver Typ oder ein CallItems</p> 
	 */
	private Object output;
	
	/**
	 * <p>Instanz auf die eine Methode aufgerufen wurde. Die ist null, wenn
	 * die Methode statisch ist und nicht null sonst.</p>
	 */
	private Object object;
	
	/**
	 * ID des ConstruktorCallItem
	 */
	private int constructorCallID = 0;
	
	/**
	 * ID des AssertCallItem
	 */
	private int assertCallID = 0;
	
	public CallItem(String callingView, Object...inputs) {
		this.setCallingView(callingView);
		this.setInputs(inputs);
	}
	
	/**
	 * @return deklarierende Klasse einer Methode oder eines Konstruktors
	 */
	public Class<?> getClazz() {
		return this.clazz;
	}

	/**
	 * @param clazz: deklarierende Klasse einer Methode oder eines Konstruktors
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return den Namen einer Methode oder einer Assertion
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name: Name einer Methode oder einer Assertion
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return den ToolTipTex dieses CallItem
	 */
	public String getCallingView() {
		return this.callingView;
	}

	/**
	 * @param callView: ToolTipTex dieses CallItem
	 */
	public void setCallingView(String callView) {
		this.callingView = callView;
	}

	public String getRestoredCall() {
		return this.restoredCall;
	}

	/**
	 * @return den Aufruf eines Konstruktors, einer Methode oder einer Assertion.
	 */
	public String restoreCall() {
		
		if(this instanceof ConstructorCallItem) {
			
			this.restoredCall = "new "+this.getClazz().getName()+"(";
			if(this.getInputs().length == 0) {
				this.restoredCall += ")";
			}else {
				for(int i=0; i<this.getInputs().length; i++) {
						
					if(this.getInputs()[i].getClass().isPrimitive() || GUtil.isWrappedPrimitive(this.getInputs()[i].getClass())) {
						
						if(i<this.getInputs().length-1)
							this.restoredCall +=this.getInputs()[i]+", ";
						else
							this.restoredCall +=this.getInputs()[i]+")";
						
					}else if(GUtil.checkType(this.getInputs()[i]) instanceof  ConstructorCallItem){
						
						if(i<this.getInputs().length-1)
							this.restoredCall +="obj"+((ConstructorCallItem)GUtil.checkType(this.getInputs()[i])).getConstructorCallID()+", ";
						else
							this.restoredCall +="obj"+((ConstructorCallItem)GUtil.checkType(this.getInputs()[i])).getConstructorCallID()+")";
						
					}else if(this.getInputs()[i] instanceof  NullCallItem){
						
						if(i<this.getInputs().length-1)
							this.restoredCall +="null, ";
						else
							this.restoredCall +="null)";
						
					}
				}
			}
			System.out.println("restoredCall leaving ConstructorCallItem: "+restoredCall);
		}else if(this instanceof NullCallItem){ 
			
			this.restoredCall = "null";
			
		}else if(this instanceof MethodCallItem) {
			
			if(this.getObject() != null) { 
				for(CallItem callItem : GFrame.getInstance().getCachedCallHistory()) {
					if(callItem instanceof ConstructorCallItem) {
						if(callItem.getOutput().equals(this.getObject())) {
							this.restoredCall = "obj"+callItem.getConstructorCallID()+"."+this.getName()+"(";
						}
					}
				}
			}else {
				
				this.restoredCall = this.getClazz().getName()+"."+this.getName()+"(";
			}
				
			if(this.getInputs().length == 0) {
				
				this.restoredCall += ")";
				
			}else {
				
				for(int i=0; i<this.getInputs().length; i++) {
					
					if(this.getInputs()[i].getClass().isPrimitive() || GUtil.isWrappedPrimitive(this.getInputs()[i].getClass())) {
						
						if(i<this.getInputs().length-1) 
							this.restoredCall +=this.getInputs()[i]+", ";
						else 
							this.restoredCall +=this.getInputs()[i]+")";
						
					}else if(GUtil.checkType(this.getInputs()[i]) instanceof  ConstructorCallItem){
						
						if(i<this.getInputs().length-1) 
							this.restoredCall += "obj"+((ConstructorCallItem)GUtil.checkType(this.getInputs()[i])).getConstructorCallID()+", ";
						else	
							this.restoredCall +="obj"+((ConstructorCallItem)GUtil.checkType(this.getInputs()[i])).getConstructorCallID()+")";
						
					}else if(GUtil.checkType(this.getInputs()[i]) instanceof  NullCallItem) {
						
						if(i<this.getInputs().length-1)
							this.restoredCall += "null"+", ";
						else	
							this.restoredCall +="null"+")";
						
					}
				}
			}
			
		}else if(this instanceof AssertCallItem){
			
			this.restoredCall = this.getName()+"(";
			
			if(this.getInputs().length == 0) {
				
				this.restoredCall += ")";
				
			}else {
				
				for(int i=0; i<this.getInputs().length; i++) {
					
					if(i==0 && this.getInputs()[i]  instanceof String)
							this.restoredCall +=this.getInputs()[i]+", ";
					else if(this.getInputs()[i].getClass().isPrimitive() || GUtil.isWrappedPrimitive(this.getInputs()[i].getClass())) {
						
						if(i<this.getInputs().length-1)
							this.restoredCall +=this.getInputs()[i]+", ";
						else
							this.restoredCall +=this.getInputs()[i]+")";
						
					}else if(GUtil.checkType(this.getInputs()[i]) instanceof ConstructorCallItem){
						
						if(i<this.getInputs().length-1) 
							this.restoredCall += "obj"+((ConstructorCallItem)GUtil.checkType(this.getInputs()[i])).getConstructorCallID()+", ";
						else 
							this.restoredCall +="obj"+((ConstructorCallItem)GUtil.checkType(this.getInputs()[i])).getConstructorCallID()+")";
						
					}else if(GUtil.checkType(this.getInputs()[i]) instanceof  NullCallItem) { 
						
						if(i<this.getInputs().length-1)
							this.restoredCall += "null, ";
						else
							this.restoredCall +="null)";
						
					}else if(GUtil.checkType(this.getInputs()[i]) instanceof MethodCallItem){
						
						if(i<this.getInputs().length-1) 
							this.restoredCall += ((MethodCallItem)GUtil.checkType(this.getInputs()[i])).restoreCall()+", ";
						else
							this.restoredCall +=((MethodCallItem)GUtil.checkType(this.getInputs()[i])).restoreCall()+")";
						
					}
				}
			}
		}
		return this.restoredCall;
	}

	/**
	 * @return Liste von Argumenten, die entweder primitive Typen 
	 * oder CallItems sind.
	 */
	public Object[] getInputs() {
		return this.inputs;
	}

	/**
	 * @param inputs: Liste von Argumenten, die entweder primitive Typen 
	 * oder CallItems sind.
	 */
	public void setInputs(Object[] inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return die Instanz eines ConstructorCallItem oder die Rückgabewert 
	 * eines MethodCallItem
	 */
	public Object getOutput() {
		return this.output;
	}

	/**
	 * @param output: die Instanz eines ConstructorCallItem oder die Rückgabewert 
	 * eines MethodCallItem
	 */
	public void setOutput(Object output) {
		this.output = output;
	}

	
	/**
	 * @return die Instanz auf die eine Methode aufgerufen wurde. Die ist null, 
	 * wenn die Methode statisch ist und nicht null sonst.
	 */
	public Object getObject() {
		return this.object;
	}

	
	/**
	 * @param object: die Instanz auf die eine Methode aufgerufen wurde. Die ist null, 
	 * wenn die Methode statisch ist und nicht null sonst.
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return ID des ConstructorCallItem
	 */
	public int getConstructorCallID() {
		return this.constructorCallID;
	}

	/**
	 * @param constructorCallID: ID des ConstructorCallItem
	 */
	public void setConstructorCallID(int constructorCallID) {
		this.constructorCallID = constructorCallID;
	}

	/**
	 * @return ID des AssertCallItem
	 */
	public int getAssertCallID() {
		return this.assertCallID;
	}

	/**
	 * @param assertCallID: ID des AssertCallItem
	 */
	public void setAssertCallID(int assertCallID) {
		this.assertCallID = assertCallID;
	}
	
}
