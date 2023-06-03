package com.util;


import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;


import com.actiononmenu.GMenuListenerAdapter;
import com.application.GFrame;
import com.callitem.CallItem;
import com.callitem.ConstructorCallItem;
import com.callitem.MethodCallItem;
import com.callitem.NullCallItem;
import com.classmember.GConstructor;
import com.classmember.GMethod;


/**
 * <p>Die Klass <code>GUtil</code> enth&auml;lt ausschlie&szlig;lig 
 * statische Hilfsmethoden f&uuml;r andere Klassen.</p>
 * @author SergeOliver
 *
 */
public final class GUtil {

	/**
	 * <p>Hilft die Reihenfolge der Ausführung von Anweisungen nachvollzuziehen</p>
	 */
	private static int systemAusgabeCounter = 0;

	private static Object[] inputFields;

	private static JTextField textField = null;
	
	private static boolean isNullPointer = false;

	public static int getSystemAusgabeCounter() {
		return systemAusgabeCounter;
	}

	public static void setSystemAusgabeCounter(int systemAusgabeCounter) {
		GUtil.systemAusgabeCounter = systemAusgabeCounter;
	}

	/**
	 * <p>H&auml;angt ein Z&auml;hler vor <code>message</code> und gibt 
	 * die in der lokalen Systemkonsole aus.</p>
	 * @param message
	 */
	public static void message(String message){
		System.out.println((++systemAusgabeCounter)+". "+message);
	}
	
	/**
	 * <p>H&auml;angt ein Z&auml;hler vor <code>message</code> und gibt 
	 * die in der lokalen Systemkonsole aus.</p>
	 * @param message
	 */
	public static void exceptionMessage(final String name, final String message){
		System.err.println((++systemAusgabeCounter)+". "+name+" caught: "+message);
		final String title = name;
		final String formatException = message+"\n\nEach input must have the right type.\n\nPleace check the type of parameters and try again!";
		final String classNotException = message+"\n\nEach input must have the right type.\n\nPleace check the type of parameters and try again!";
		new Thread() {
			@Override
			public void run() {
				if(name.endsWith("FormatException")) {
					JOptionPane.showMessageDialog(GFrame.getInstance(), formatException, title, JOptionPane.ERROR_MESSAGE);
				}if(name.endsWith("ClassNotException")) {
					JOptionPane.showMessageDialog(GFrame.getInstance(), classNotException, title, JOptionPane.ERROR_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(GFrame.getInstance(), message, title, JOptionPane.ERROR_MESSAGE);
				}
			}
		}.start();
	}
	
	/**
	 * <p>Gibt die Elemente eines Arrays von <code>Object</code> mit deren jeweiligen
	 * Index aus.</p>
	 * @param objects
	 */
	public static void display(String description, Object ... objects) {
		
		int count = 0;
		System.out.println("\n"+description);
		for(Object o : objects) {
			System.out.println((count++)+". "+o);
		}
		System.out.println();
	}
	
	/**
	 * <p>Gibt die Elemente eines ArrayList von <code>Class<?></code>mit deren jeweiligen
	 * Index aus.</p>
	 * @param classes
	 */
	public static void displayClasses(String description, ArrayList<Class<?>> classes) {
		
		int count = 0;
		System.out.println("\n"+description);
		for(Class<?> c : classes) {
			System.out.println((count++)+". "+c);
		}
		System.out.println();
	}
	
	/**
	 * <p>Gibt die Elemente eines ArrayList von <code>CallHistory</code>mit deren jeweiligen
	 * Index aus.</p>
	 * @param callhistory
	 */
	public static void displayHistory(String description, ArrayList<CallItem> callhistory) {
		
		int count = 0;
		System.out.println("\n"+description);
		for(CallItem ch : callhistory) {
			System.out.println((count++)+". "+ch);
		}
		System.out.println();
	}
	
	public static Object[] createConstructorInputFields(int nbr, Object o) {
		
		inputFields = new Object[nbr];
		Class<?>[] parameterTypes = ((GConstructor) o).getConstructor().getParameterTypes();
		
		for(int i=0; i<nbr; i++) {
			
			if(parameterTypes[i].isPrimitive()) {
				
				textField = new JTextField();
				textField.setToolTipText(((GConstructor) o).getParameters()[i]);
				textField.setName(((GConstructor) o).getParameters()[i]);
				textField.setInputVerifier(new GInputVerifier());
				inputFields[i] = textField;
			}else{
				
				JComboBox<Object> instancesBox = null;
				ArrayList<Object>instancesView = new ArrayList<>();
				boolean found = false;//Ist eine Instanz dieses Typs schon aufgelistet?
				
				if(GFrame.getInstance().getCachedCallHistory().size() > 0) {
					
					for(int k=0; k<GFrame.getInstance().getCachedCallHistory().size(); k++) {
						
						if(GFrame.getInstance().getCachedCallHistory().get(k) instanceof ConstructorCallItem) {
							
							if(parameterTypes[i].isInstance(GFrame.getInstance().getCachedCallHistory().get(k).getOutput())) {
								
								found = true;
								break;
							}
						}
					}
					if(!found){
						if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[0])) {
							
							createParameterlessInstance(parameterTypes[i], false);
				
						}else if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[1])) {
							
							createNullPointer(parameterTypes[i], true);
						}
					}
					for(int j=0; j<GFrame.getInstance().getCachedCallHistory().size(); j++) {
						
						if(GFrame.getInstance().getCachedCallHistory().get(j) instanceof ConstructorCallItem)
							instancesView.add(GFrame.getInstance().getCachedCallHistory().get(j).getOutput());
						else if(GFrame.getInstance().getCachedCallHistory().get(j) instanceof NullCallItem)
							instancesView.add(GFrame.getInstance().getCachedCallHistory().get(j).getOutput());
					}
					instancesBox = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
				}else {
					if(!found){
						if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[0])) {
							
							createParameterlessInstance(parameterTypes[i], false);

						}else if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[1])) {
							
							createNullPointer(parameterTypes[i], true);
						}
					}
					instancesBox = new JComboBox<Object>(new Object[] {GFrame.getInstance().getCachedCallHistory().get(0).getOutput()});
				}
				inputFields[i] = instancesBox;
			}
		}
		return inputFields;
	}
	
	public static Object[] createMethodInputFields(int nbr, Object o){
		
		inputFields = new Object[nbr];
		Class<?>[] parameterTypes = ((GMethod) o).getMethod().getParameterTypes();
		
		for(int i=0; i<nbr; i++) {
			
			if(parameterTypes[i].isPrimitive()) {
				
				textField = new JTextField();
				textField.setToolTipText(((GMethod) o).getParameters()[i]);
				textField.setName(((GMethod) o).getParameters()[i]);
				textField.setInputVerifier(new GInputVerifier());
				inputFields[i] = textField;
			}else{
				
				JComboBox<Object> instancesBox = null;
				ArrayList<Object>instancesView = new ArrayList<>();
				boolean found = false;//Ist eine Instanz dieses Typs schon aufgelistet?
				
				if(GFrame.getInstance().getCachedCallHistory().size() > 0) {
					
					for(int k=0; k<GFrame.getInstance().getCachedCallHistory().size(); k++) {
						
						if(GFrame.getInstance().getCachedCallHistory().get(k) instanceof ConstructorCallItem) {
							
							if(parameterTypes[i].isInstance(GFrame.getInstance().getCachedCallHistory().get(k).getOutput())) {
								
								found = true;
								break;
							}
						}
					}
					if(!found){
						if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[0])) {
							
							createParameterlessInstance(parameterTypes[i], false);
						}else if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[1])) {
							
							createNullPointer(parameterTypes[i], true);
						}
					}
					for(int j=0; j<GFrame.getInstance().getCachedCallHistory().size(); j++) {
						
						if(GFrame.getInstance().getCachedCallHistory().get(j) instanceof ConstructorCallItem)
							instancesView.add(GFrame.getInstance().getCachedCallHistory().get(j).getOutput());
						else if(GFrame.getInstance().getCachedCallHistory().get(j) instanceof NullCallItem)
							instancesView.add(GFrame.getInstance().getCachedCallHistory().get(j).getOutput());
					}
					instancesBox = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
				}else {
					if(!found){
						if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[0])) {
							
							createParameterlessInstance(parameterTypes[i], false);
						}else if(GFrame.getInstance().getComboBox2().getSelectedItem().equals(GFrame.getInstance().getComboBoxItems2()[1])) {
							
							createNullPointer(parameterTypes[i], true);
						}
					}
					instancesBox = new JComboBox<Object>(new Object[] {GFrame.getInstance().getCachedCallHistory().get(0).getOutput()});
				}
				inputFields[i] = instancesBox;
			}
		}
		return inputFields;
	}
	
	public static Object[] createAssertionInputFields(int nbr, String name) {
		
		inputFields = new Object[nbr];
		ArrayList<Object> instancesView = new ArrayList<>();
		JTextField field = new JTextField();
		inputFields[0] = field;
		
		if(name.equals("assertEquals")) {
			
			for(int i=0; i<GFrame.getInstance().getCachedCallHistory().size(); i++) {
				
				if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof MethodCallItem) {	
					
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getCallingView());
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
					
				}else if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof ConstructorCallItem) {
					
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
					
				}else if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof NullCallItem) {
					
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
					
				}
			}
			JComboBox<Object> box = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
			JComboBox<Object> box2 = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
			JTextField delta = new JTextField("0.0");
			box.setEditable(true);
			box2.setEditable(true);
			box.setToolTipText("Select an item or edit please!");
			box2.setToolTipText("Select an item or edit please!");
			delta.setToolTipText("Delta value to compare floating-point numbers");
			inputFields[1] = box;
			inputFields[2] = box2;
			inputFields[3] = delta;
		}else if(name.endsWith("Same")){
			
			for(int i=0; i<GFrame.getInstance().getCachedCallHistory().size(); i++) {
				
				if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof ConstructorCallItem) {
					
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
					
				}else if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof NullCallItem) {
				
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
					
				}
			}
			JComboBox<Object> box = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
			JComboBox<Object> box2 = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
			box.setToolTipText("Select an object please!");
			box2.setToolTipText("Select an object please!");
			inputFields[1] = box;
			inputFields[2] = box2;
			
		}else if(name.endsWith("Null")) {
			
			for(int i=0; i<GFrame.getInstance().getCachedCallHistory().size(); i++) {
				
				if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof ConstructorCallItem) {
				
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
					
				}else if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof NullCallItem) {
				
					instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getOutput());
				}
			}
			JComboBox<Object> box = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
			box.setToolTipText("Select an object please!");
			inputFields[1] = box;
		}else {
			for(int i=0; i<GFrame.getInstance().getCachedCallHistory().size(); i++) {
				
				if(GFrame.getInstance().getCachedCallHistory().get(i) instanceof MethodCallItem) {
					if(GFrame.getInstance().getCachedCallHistory().get(i).getOutput() instanceof Boolean)
						instancesView.add(GFrame.getInstance().getCachedCallHistory().get(i).getCallingView());
				}
			}
			JComboBox<Object> box = new JComboBox<Object>(instancesView.toArray(new Object[instancesView.size()]));
			box.setEditable(true);
			if(instancesView.size() > 0)
				box.setToolTipText("Choose/Edit a condition please!");
			else
				box.setToolTipText("Edit a condition please!");
			inputFields[1] = box;
		}

		return inputFields;
	}

	/**
	 * <p>Erstellt einen Null Pointer. Die ist aufgerufen, wenn 
	 * der Benutzer ein Objekt, das noch nicht aufgelistet ist, als Argument 
	 * eines Konstruktors oder einer Methode angeben will.</p>
	 * @param parameterType
	 */
	private static void createNullPointer(Class<?> parameterType, boolean isNullPointer) {
		
		try {
			GUtil.isNullPointer = isNullPointer;
			Class<?> clazz = parameterType;
			GConstructor gConstructor = new GConstructor(clazz, clazz.getConstructor());
			gConstructor.createInstance();
				
			if(!NullCallItem.isDuplicate()) {
				JMenuItem subObjectsmenu = new JMenuItem("null");
				subObjectsmenu.setToolTipText("<-- Null Pointer");
				GFrame.getInstance().getObjectsMenu().add(subObjectsmenu);
				NullCallItem.setDuplicate(true);
			}
		} catch (NoSuchMethodException | SecurityException e) {
			GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
		}
	}
	/**
	 * <p>Erstellt die default Instanz einer Klasse. Die ist aufgerufen, wenn 
	 * der Benutzer ein Objekt, das noch nicht aufgelistet ist, als Argument 
	 * eines Konstruktors oder einer Methode angeben will.</p>
	 * @param parameterType
	 */
	private static void createParameterlessInstance(Class<?> parameterType, boolean isNullPointer) {
		
		try {
			GUtil.isNullPointer = isNullPointer;
			final Class<?> clazz = parameterType;
			final GConstructor gConstructor = new GConstructor(clazz, clazz.getConstructor());
			message("C: "+gConstructor.toString());
			final Method[] declaredMethods = clazz.getDeclaredMethods();
			gConstructor.createInstance();
			
			final JMenu subObjectsmenu = new JMenu(String.valueOf(gConstructor.getInstance()));
			
			subObjectsmenu.addMenuListener(new GMenuListenerAdapter() {
				
				@Override
				public void menuSelected(MenuEvent e) {
					
					for(Method method : declaredMethods) {
						
						final GMethod gMethod = new GMethod(clazz, method);
						
						if(!gMethod.getModifier().contains("static")&& !gMethod.getReturnType().equals("void")) {
							
							JMenuItem methodMenuItem = new JMenuItem(new AbstractAction(gMethod.toString()){
	
								private static final long serialVersionUID = 1L;

								@Override
								public void actionPerformed(ActionEvent e) {
									
									new Thread(){
										
										@Override
										public void run() {
											
											if(gMethod.getNumberOfParameter() > 0) {
												
												int option = JOptionPane.showConfirmDialog(GFrame.getInstance(), GUtil.createMethodInputFields(gMethod.getNumberOfParameter(), gMethod), 
														GUtil.createLabel(gMethod)+"\n", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, GFrame.getInstance().getIcon());
												
												if(option == JOptionPane.OK_OPTION) {
													try {
														gMethod.invoke(gConstructor.getInstance(), GUtil.inputFields);
													}catch(NumberFormatException e) {
														GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
													}
												}
											}else {
												try {
													gMethod.invoke(gConstructor.getInstance());
												}catch(NumberFormatException e) {
													GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
												}
											}
										};
									}.start();
								}
							});
							subObjectsmenu.add(methodMenuItem);
						}
					}
				}
				
				@Override
				public void menuDeselected(MenuEvent e) {
					subObjectsmenu.removeAll();
				}			
			});
			subObjectsmenu.setToolTipText("<-- "+gConstructor.getView());
			GFrame.getInstance().getObjectsMenu().add(subObjectsmenu);
		} catch (NoSuchMethodException | SecurityException e) {
			GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
		}
	}
	
	/**
	 * @param o ein Objekt
	 * @return Oject, ConstructorCallItem, MethodCallItem
	 */
	public static Object checkType(Object o) {
		
		for(int l=0; l<GFrame.getInstance().getCachedCallHistory().size(); l++) {

			if(!o.getClass().isPrimitive() && !GUtil.isWrappedPrimitive(o.getClass())) {
				
				if(GFrame.getInstance().getCachedCallHistory().get(l) instanceof ConstructorCallItem) {
					
					if((GFrame.getInstance().getCachedCallHistory().get(l).getOutput().equals(o)))
							o = GFrame.getInstance().getCachedCallHistory().get(l);
					
				}else if((GFrame.getInstance().getCachedCallHistory().get(l) instanceof MethodCallItem)) {
					
					if((GFrame.getInstance().getCachedCallHistory().get(l).getCallingView().equals(o)))
						o = GFrame.getInstance().getCachedCallHistory().get(l);
					
				}else if((GFrame.getInstance().getCachedCallHistory().get(l) instanceof NullCallItem)){
					
					if((GFrame.getInstance().getCachedCallHistory().get(l).getOutput().equals(o)))
						o = GFrame.getInstance().getCachedCallHistory().get(l);
						
				}
			}
		}
		return o;
	}
	
	/**<p>Gibt true zur&uuml;ck, wenn das Klassenobjekt clazz eins der 8  
	 * wrapped Primitive Typen (Byte.class, Boolean.class, Short.class,
	 * Integer.class, Long.class, Float.class, Double.class) und Void.class.
	 * Ansonsten false.</p>
	 * @param clazz Ein Klassenobjekt
	 * @return
	 */
	public static boolean isWrappedPrimitive(Class<?> clazz) {
		if(clazz == Integer.class || clazz == Double.class ||  clazz == Boolean.class || 
				clazz == Long.class || clazz == Byte.class || clazz == Float.class || 
				clazz == Short.class || clazz == Character.class || clazz == Void.class)
			return true;
		else
			return false;
	}
	
	public static String createLabel(GConstructor c) {
		int index = c.getConstructor().getName().lastIndexOf(".");
		String label = c.getConstructor().getName().substring(index+1)+"(";
		int param = c.getNumberOfParameter();
		if(param == 1) return label + "arg)";
		for(int i=0; i<param; i++) {
			if(i < param-1)
				label +="arg"+(i+1)+", ";
			else
				label +="arg"+(i+1)+")";
		}
		
		return label;
	}
	
	public static String createLabel(GMethod m) {
		String label = m.getMethod().getName()+"(";
		int param = m.getNumberOfParameter();
		if(param == 1) return label + "arg)";
		for(int i=0; i<param; i++) {
			if(i < param-1)
				label +="arg"+(i+1)+", ";
			else
				label +="arg"+(i+1)+")";
		}
		return label;
	}

	public static boolean isNullPointer() {
		return isNullPointer;
	}

	public static void setNullPointer(boolean isNullPointer) {
		GUtil.isNullPointer = isNullPointer;
	}

	public static Object[] getInputFields() {
		return inputFields;
	}

	public static void setInputFields(Object[] inputFields) {
		GUtil.inputFields = inputFields;
	}

	public static JTextField getTextField() {
		return textField;
	}

	public static void setTextField(JTextField textField) {
		GUtil.textField = textField;
	}
}
