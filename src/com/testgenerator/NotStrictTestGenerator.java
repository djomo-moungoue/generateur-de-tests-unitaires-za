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
 * <p>Die Klasse NotStrictTestGenerator umfasst Merkmale und Methode zur
 * Generierung einer Testklasse aus der Handlungen des Benutzers auf
 * die GUI. Die erstellte Klasse kann mit JUnit aufgef&uuml;hrt werden.</p>
 * @author SergeOliver
 *
 */
public class NotStrictTestGenerator extends TestGenerator{
	
	public NotStrictTestGenerator(ArrayList<CallItem> history) {
		super(history, "NotStrictTest.java");
		this.setUp = getFreeLine()+getTab()+"@Before";
		this.setUp += getNewLine()+getTab()+"public void setUP(){";
		this.testCase +=getFreeLine();
		this.testCase += getTab()+"@Test";
		this.testCase += getNewLine()+getTab()+"public void test(){";
		
		generateTestClass();
		
		createFile(getPathName(), getFileName(), getHead() + getSetUp() + getTestCase() + getTail());
		
		GUtil.message(getFile()+" successfully generated.");
	}
	
	@Override
	public void generateTestClass() {
		
		for(int i=0; i<getHistory().size(); i++) {
			
			if(getHistory().get(i) instanceof ConstructorCallItem) {
				setHead("private "+getHistory().get(i).getClazz().getName()+" obj"+getHistory().get(i).getConstructorCallID());
				setSetUp(" obj"+getHistory().get(i).getConstructorCallID() +" = "+getHistory().get(i).restoreCall());
					
			}else if(getHistory().get(i) instanceof AssertCallItem) {
				
				generateTestCase(getHistory().get(i).restoreCall(),getHistory().get(i).getAssertCallID());
			}
		}
		this.setUp += getNewLine()+getTab()+"}";
		this.testCase += getNewLine()+getTab()+"}";
		setTail(getNewLine()+"}");
	}

	@Override
	public void generateTestCase(String testCase, int assertID) {
		
		this.testCase += getNewLine()+getTab()+getTab()+testCase+";";
	}

}
