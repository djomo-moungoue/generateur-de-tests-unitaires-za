package com.classmember;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.application.GFrame;
import com.callitem.ConstructorCallItem;
import com.callitem.MethodCallItem;
import com.callitem.NullCallItem;
import com.util.GUtil;

/**
 * <p>Die Klasse GMethod ist eine Variante des <code>java.lang.reflect.Method</code>,
 * die Funktionalit&auml;ten zur besseren Darstellung einer Methode als
 * String im Pull-Down-Men&uuml; der Anwendung definiert. </p>
 * @author SergeOliver
 *
 */
public class GMethod extends GMember{
	
	/**
	 * modifier name(parameters) exceptions
	 * @param constructor
	 */
	public GMethod(Class<?> clazz, Method method) {
		super(clazz);
		setMethod(method);
		setModifier(method.getModifiers());
		setReturnType(method.getGenericReturnType(), method.getReturnType());
		setName(method.getName());
		setParameters(method.getGenericParameterTypes(), method.getParameterTypes());
		setExceptions(method.getExceptionTypes());
		setNumberOfParameter(getParameters().length);
		setNumberOfException(getExceptions().length);
		if(getNumberOfParameter() < 1) 
			name = getModifier() +" "+getReturnType()+" "+name+" () ";
		if(getNumberOfParameter() == 1) 
			name = getModifier()+" "+getReturnType()+" "+name+" ("+getParameters()[0]+") ";
		if(getNumberOfParameter() > 1){
			name = getModifier()+" "+getReturnType()+" "+name+ " (";
			for(int i = 0; i<getParameters().length; i++){
				if(i < getParameters().length-1)
					name += getParameters()[i]+", ";
				else
					name += getParameters()[i]+") ";
			}
		}
		if(getNumberOfException() >= 1) {
			name += " throws ";
			for(int i = 0; i < getNumberOfException(); i++) {
				if(i < getNumberOfException()-1)
					name += getExceptions()[i]+", ";
				else
					name +=getExceptions()[i];
			}
		}
	}

	public void invoke(Object obj, Object...arguments) {
		try {

				Object[] inputs = new Object[arguments.length];
				Object[] modifiedInputs = new Object[arguments.length];
				Object input = null;
				if(obj != null)
					view = obj+"."+getMethod().getName().substring(getMethod().getName().lastIndexOf(".")+1)+"(";
				else
					view = this.getClazz().getName()+"."+getMethod().getName().substring(getMethod().getName().lastIndexOf(".")+1)+"(";
				
				for(int i=0; i<arguments.length; i++) {
					
					if(arguments[i] instanceof JTextField) {
						
						input = ((JTextField)arguments[i]).getText();
					}else if(arguments[i] instanceof JComboBox<?>) {
						
						input = ((JComboBox<?>) arguments[i]).getSelectedItem();
					}
					Class<?> currentArgumentType = getMethod().getParameterTypes()[i];
					
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
				if(!view.endsWith(")"))
						view+=")";
				try {
					setReturnValue(getMethod().invoke(obj, inputs));
				}catch(NumberFormatException e) {
					GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
				}
				GFrame.getInstance().getCachedCallHistory().add(new MethodCallItem(this.getClass(), getMethod().getName(), view, getReturnValue(), obj, inputs));
				GUtil.displayHistory("History: ", GFrame.getInstance().getCachedCallHistory());
				
		} catch (IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			GUtil.exceptionMessage(e.getClass().getSimpleName(), e.getMessage());
		}
	}

	@Override
	public boolean createInstance(Object... arguments) {
		return false;
		// TODO Auto-generated method stub
	}
}
