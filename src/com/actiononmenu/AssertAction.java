package com.actiononmenu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.application.GFrame;
import com.callitem.AssertCallItem;
import com.util.GUtil;

/**
 * <p>Die Klasse AssertAction &uuml;berwacht Ereignisse auf das Men&uuml; 
 * Assertions.</p>
 * @author SergeOliver
 *
 */
public class AssertAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Typ der assert-Method</p>
	 */
	private String name;
	
	public AssertAction(String name) {
		this.name = name;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		new Thread(this.name){
			
			private String name = this.getName();

			@Override
			public void run() {
				
				int option = -1;
				String subAssertionsString = "";
				if(this.name .equals("assertEquals")) {//[1 TextField], 2 editable ComboBox
					
					option = JOptionPane.showConfirmDialog(GFrame.getInstance(), GUtil.createAssertionInputFields(4, this.name), 
							this.name+"()\n", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, GFrame.getInstance().getIcon());
					if(option == JOptionPane.OK_OPTION) {
						
						if(((JTextField)GUtil.getInputFields()[0]).getText().isEmpty()) {
							
							if(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem() instanceof Double || ((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem() instanceof Double){
								
								subAssertionsString = this.name+"("+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+", "+((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()+", "+((JTextField)GUtil.getInputFields()[3]).getText()+")";
								GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()),GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()),Double.parseDouble(((JTextField)GUtil.getInputFields()[3]).getText())));
							}else {
								
								subAssertionsString = this.name+"("+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+", "+((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()+")";
								GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()),GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem())));
							}
						}else {
							
							if(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem() instanceof Double || ((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem() instanceof Double){
								
								subAssertionsString = this.name+"(\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\", "+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+", "+((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()+", "+((JTextField)GUtil.getInputFields()[3]).getText()+")";
								GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString, "\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\"",GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()),GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()),Double.parseDouble(((JTextField)GUtil.getInputFields()[3]).getText())));
							}else {
								
								subAssertionsString = this.name+"(\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\", "+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+", "+((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()+")";
								GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString, "\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\"",GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()),GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem())));
							}
						}
						
						GFrame.getInstance().getTestcasesMenu().add(subAssertionsString);
						
						GUtil.displayHistory("HIstory:", GFrame.getInstance().getCachedCallHistory());
					}
				}else if(this.name.equals("assertSame") || this.name.equals("assertNotSame")) {//[1 TextField], 2 ComboBox
					
					option = JOptionPane.showConfirmDialog(GFrame.getInstance(), GUtil.createAssertionInputFields(3, this.name), 
							this.name+"()\n", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, GFrame.getInstance().getIcon());
					if(option == JOptionPane.OK_OPTION) {
						
						if(((JTextField)GUtil.getInputFields()[0]).getText().isEmpty()) {
							
							subAssertionsString = this.name+"("+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+", "+((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem()+")";
							GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()),GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem())));
						}else {
							subAssertionsString = this.name+"(\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\", "+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+", "+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+")";
							GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString, "\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\"",GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()),GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[2]).getSelectedItem())));
						}
						
						GFrame.getInstance().getTestcasesMenu().add(new JMenuItem(subAssertionsString));
						
						GUtil.displayHistory("HIstory:", GFrame.getInstance().getCachedCallHistory());
					} 
				}else if(this.name.equals("assertTrue") || this.name.equals("assertFalse")) {//[1 TextField], 1 JComboBox
					
					option = JOptionPane.showConfirmDialog(GFrame.getInstance(), GUtil.createAssertionInputFields(2, this.name), 
							this.name+"()\n", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, GFrame.getInstance().getIcon());
					if(option == JOptionPane.OK_OPTION) {
						
						if(((JTextField)GUtil.getInputFields()[0]).getText().isEmpty()) {
							subAssertionsString = this.name+"("+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+")";
							GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem())));
						}else {
							subAssertionsString = this.name+"(\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\", "+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+")";
							GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,  "\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\"",GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem())));
						}
						GFrame.getInstance().getTestcasesMenu().add(new JMenuItem(subAssertionsString));
						
						GUtil.displayHistory("HIstory:", GFrame.getInstance().getCachedCallHistory());
					}
				}else if(this.name.equals("assertNull") || this.name.equals("assertNotNull")) {//[1 TextField], 1 ComboBox
					
					option = JOptionPane.showConfirmDialog(GFrame.getInstance(), GUtil.createAssertionInputFields(2, this.name), 
							this.name+"()\n", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, GFrame.getInstance().getIcon());
					if(option == JOptionPane.OK_OPTION) {
						
						if(((JTextField)GUtil.getInputFields()[0]).getText().isEmpty()) {
							subAssertionsString = this.name+"("+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+")";
							GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem())));
						}else {
							subAssertionsString = this.name+"(\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\", "+((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem()+")";
							GFrame.getInstance().getCachedCallHistory().add(new AssertCallItem(this.name,subAssertionsString,  "\""+((JTextField)GUtil.getInputFields()[0]).getText()+"\"",GUtil.checkType(((JComboBox<?>)GUtil.getInputFields()[1]).getSelectedItem())));
						}
						GFrame.getInstance().getTestcasesMenu().add(new JMenuItem(subAssertionsString));
						
						GUtil.displayHistory("HIstory:", GFrame.getInstance().getCachedCallHistory());
					}
				}
			}
		}.start();
	}

	/**
	 * <p>Typ der assert-Method</p>
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
