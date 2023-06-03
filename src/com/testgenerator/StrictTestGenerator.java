/**
 * 
 */
package com.testgenerator;

import java.util.ArrayList;

import com.callitem.AssertCallItem;
import com.callitem.CallItem;
import com.callitem.ConstructorCallItem;
import com.util.GUtil;

/**
 * <p>Die Klasse StrictTestGenerator umfasst Merkmale und Methode zur
 * Generierung einer Testklasse aus der Handlungen des Benutzers auf
 * die GUI. Die erstellte Klasse kann mit JUnit aufgef&uuml;hrt werden.</p>
 * @author SergeOliver
 *
 */
public class StrictTestGenerator extends TestGenerator{
	
	public StrictTestGenerator(ArrayList<CallItem> history) {
		super(history, "StrictTest.java");
		this.body +=getNewLine();
		this.body += getTab()+"@Test";
		this.body += getNewLine()+getTab()+"public void test(){";
		
		generateTestClass();
		
		createFile(getPathName(), getFileName(), getHead() + getBody() + getTail());
		
		GUtil.message(getFile()+" successfully generated.");
	}
	
	@Override
	public void generateTestClass() {
		
		for(int i=0; i<getHistory().size(); i++) {
			
			if(getHistory().get(i) instanceof ConstructorCallItem) {
				
				setBody(getTab()+getTab()+getHistory().get(i).getClazz().getName()+" obj"+getHistory().get(i).getConstructorCallID() +" = "+getHistory().get(i).restoreCall()+";");
					
			}else if(getHistory().get(i) instanceof AssertCallItem) {
				
				generateTestCase(getHistory().get(i).restoreCall(),getHistory().get(i).getAssertCallID());
				setBody(getTestCase());
				setTestCase("");
			}
		}
		this.body += getNewLine()+getTab()+"}";
		setTail(getNewLine()+"}");
	}

	@Override
	public void generateTestCase(String testCase, int assertID) {
		
		this.testCase += getTab()+getTab()+testCase+";";
	}
}
