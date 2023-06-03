package com.classmember;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.application.GFrame;
import com.callitem.ConstructorCallItem;
import com.callitem.NullCallItem;
import com.util.GUtil;

/**
 * <p>Die Klasse GConstructor ist eine Variante des java.lang.reflect.Constructor,
 * die Funktionalit&auml;ten zur besseren Darstellung eines Konstruktors als
 * String im Pull-Down-Men&uuml; der Anwendung definiert. </p>
 * @author SergeOliver
 *
 */
public class GConstructor extends GMember{

	/**
	 * modifier name(parameters) exceptions
	 * @param constructor
	 */
	public GConstructor(Class<?> clazz, Constructor<?> constructor) {
		super(clazz);
		setConstructor(constructor);
		setModifier(constructor.getModifiers());
		setName(constructor.getName());
		setParameters(constructor.getGenericParameterTypes(), constructor.getParameterTypes());
		setExceptions(constructor.getExceptionTypes());
		setNumberOfParameter(getParameters().length);
		setNumberOfException(getExceptions().length);
		if(getNumberOfParameter() < 1) 
			name = getModifier() +" "+name+ " () ";
		if(getNumberOfParameter() == 1) 
			name += " ("+getParameters()[0]+") ";
		if(getNumberOfParameter() > 1){
			name += " (";
			for(int i = 0; i<getParameters().length; i++){
				if(i < getParameters().length-1)
					name += getParameters()[i]+", ";
				else
					name += getParameters()[i]+") ";
			}
		}
		if(getNumberOfException() >= 1) {
			for(int i = 0; i < getNumberOfException(); i++) {
				if(i < getNumberOfException()-1)
					name +=getExceptions()[i]+", ";
				else
					name +=getExceptions()[i];
			}
		}
	}

	public boolean createInstance(Object...arguments) {
		try {

			Object[] inputs = new Object[arguments.length];
			Object[] modifiedInputs = new Object[arguments.length];
			if(arguments.length != 0) {
					Object input = null;
					view = "new "+getConstructor().getName().substring(getConstructor().getName().lastIndexOf(".")+1)+"(";
					for(int i=0; i<arguments.length; i++) {
						if(arguments[i] instanceof JTextField) {
							input = ((JTextField)arguments[i]).getText();
						}else if(arguments[i] instanceof JComboBox<?>) {
							input = ((JComboBox<?>) arguments[i]).getSelectedItem();
						}
						Class<?> currentArgumentType = getConstructor().getParameterTypes()[i];
						if(!currentArgumentType.isArray()) {
							
							if(currentArgumentType == byte.class) {
								inputs[i] = Byte.parseByte(input.toString());
								modifiedInputs[i] = Byte.parseByte(input.toString());
							}else if(currentArgumentType == char.class) {
								inputs[i] = input.toString().charAt(0);
								modifiedInputs[i] = input.toString().charAt(0);
							}else if(currentArgumentType == boolean.class) {
								inputs[i] = Boolean.parseBoolean(input.toString());
								modifiedInputs[i] = Boolean.parseBoolean(input.toString());
							}else if(currentArgumentType == short.class) {
								inputs[i] = Short.parseShort(input.toString());
								modifiedInputs[i] = Short.parseShort(input.toString());
							}else if(currentArgumentType == int.class) {
								inputs[i] = Integer.parseInt(input.toString());
								modifiedInputs[i] = Integer.parseInt(input.toString());
							}else if(currentArgumentType == double.class) {
								inputs[i] = Double.parseDouble(input.toString());
								modifiedInputs[i] = Double.parseDouble(input.toString());
							}else if(currentArgumentType == float.class) {
								inputs[i] = Float.parseFloat(input.toString());
								modifiedInputs[i] = Float.parseFloat(input.toString());
							}else if(currentArgumentType == long.class) {
								inputs[i] = Long.parseLong(input.toString());
								modifiedInputs[i] = Long.parseLong(input.toString());
							}else {
								for(int j = 0; j < GFrame.getInstance().getCachedClasses().size(); j++) {
									System.out.println("formal param:"+currentArgumentType.getName()+" - ClassLoader:"+currentArgumentType.getClassLoader());
									System.out.println("actual param:"+GFrame.getInstance().getCachedClasses().get(j).getName()+" - ClassLoader:"+GFrame.getInstance().getCachedClasses().get(j).getClassLoader());
									if(currentArgumentType.getName().equals(GFrame.getInstance().getCachedClasses().get(j).getName())) {
										for(int k=0; k<GFrame.getInstance().getCachedCallHistory().size(); k++) {
											
											if(GFrame.getInstance().getCachedCallHistory().get(k) instanceof ConstructorCallItem) {
												if(GFrame.getInstance().getCachedCallHistory().get(k).getOutput() == input) {
													inputs[i] = input;
													modifiedInputs[i] = GFrame.getInstance().getCachedCallHistory().get(k);
													break;
												}
											}else if(GFrame.getInstance().getCachedCallHistory().get(k) instanceof NullCallItem) {
												if(GFrame.getInstance().getCachedCallHistory().get(k).getOutput() == input) {
													inputs[i] = null;
													modifiedInputs[i] = GFrame.getInstance().getCachedCallHistory().get(k);
													break;
												}
											}
										}
										break;
									}
								}
							}
						}
						if(i < getParameters().length-1)
							view += inputs[i]+", ";
						else
							view +=inputs[i]+")";
					}
					try {
						setInstance(getConstructor().newInstance(inputs));
						GFrame.getInstance().getCachedCallHistory().add(new ConstructorCallItem(getClazz(), view, getInstance(), modifiedInputs));//primitiv or Reference
						return true;
					}catch(NumberFormatException e) {
						GUtil.exceptionMessage(e.getClass().getSimpleName(),e.getMessage());
						return false;
					}
			}else {
				if(GUtil.isNullPointer()) {
					if(!NullCallItem.isDuplicate()) {
						GFrame.getInstance().getCachedCallHistory().add(NullCallItem.getInstance());//Null Pointer
					}
					GUtil.setNullPointer(false);
				}else {
					setInstance(getConstructor().newInstance());
					view = "new "+getConstructor().getName().substring(getConstructor().getName().lastIndexOf(".")+1)+"()";
					GFrame.getInstance().getCachedCallHistory().add(new ConstructorCallItem(getClazz(), view, getInstance(), modifiedInputs));//primitiv or Reference
				}
				return true;
			}			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
		}
		return false;
	}

	@Override
	public void invoke(Object obj, Object... arguments) {
		// TODO Auto-generated method stub
	}
}
