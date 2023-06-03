package com.actiononmenu;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.application.GClassLoader;
import com.application.GFrame;
import com.application.GLauncher;
import com.classmember.GConstructor;
import com.classmember.GMethod;
import com.util.GUtil;

/**
 * <p>Die Klasse GAction &uuml;berwacht Ereignisse auf die Men&uuml;s 
 * Classes und Objects.</p>
 * @author SergeOliver
 *
 */
public class GAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Verweis auf das aktuelste besuchte Verzeichnis im Dateisystem. Das
	 * Projekt ist das Defaultverzeichnis</p>
	 */
	private static File currentDirectory = new File(GLauncher.class.getResource("../../").getFile());
	
	public GAction(){
		GUtil.message("Erzeugt eine Instanz von GAction...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GUtil.message("Bearbeitet das Ereignis auf dem Menüelement Open File(s)...");
		
		JFileChooser fileChooser = new JFileChooser(currentDirectory);
		fileChooser.setDialogTitle(".class File(s) Chooser");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.class)", "class");
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(true);
		
		GUtil.message("Erzeugt ein (*.class) FileChooser und setzt das Titel.");
		GUtil.message("aktuelles Verzeichnis: "+fileChooser.getCurrentDirectory().toString());
		
		if(fileChooser.showOpenDialog(GFrame.getInstance()) == JFileChooser.APPROVE_OPTION){
			
			File[] selectedFiles = fileChooser.getSelectedFiles();
			if(selectedFiles.length != 0) {
				currentDirectory = fileChooser.getCurrentDirectory();
			}
			GUtil.display("Liste von ausgewählten Dateien", (Object[])selectedFiles);
			
			for(final File file : selectedFiles){
				
				if(!GFrame.getInstance().getCachedFiles().contains(file)) {
					
					
					final Class<?> clazz = new GClassLoader(file).getClassObject();
					if(clazz == null) {
						return;
					}
					GFrame.getInstance().getCachedFiles().add(file);
					final JMenu classesSubmenu = new JMenu(file.getName());
					final Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
					final Method[] declaredMethods = clazz.getDeclaredMethods();
					
					classesSubmenu.addMenuListener(new GMenuListenerAdapter() {
					
						@Override
						public void menuSelected(MenuEvent e) {
							
							
							for(Constructor<?> constructor : declaredConstructors){
								
								final GConstructor gConstructor = new GConstructor(clazz, constructor);
								
								JMenuItem constructorMenuItem = new JMenuItem(new AbstractAction(gConstructor.toString()){

									private static final long serialVersionUID = 1L;

									@Override
									public void actionPerformed(ActionEvent e) {
										
										new Thread() {
											
											@Override
											public void run() {
												boolean instanceSet = false;
												if(gConstructor.getNumberOfParameter() > 0) {
													
													int option = JOptionPane.showConfirmDialog(GFrame.getInstance(), GUtil.createConstructorInputFields(gConstructor.getNumberOfParameter(), gConstructor), 
															GUtil.createLabel(gConstructor)+"\n", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, GFrame.getInstance().getIcon());
													
													if(option == JOptionPane.OK_OPTION) {
														
														instanceSet = gConstructor.createInstance(GUtil.getInputFields());
													}
													
												}else {
													
													instanceSet = gConstructor.createInstance();
												}
												if(instanceSet) {
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
																								gMethod.invoke(gConstructor.getInstance(), GUtil.getInputFields());
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
												}
											}
										}.start();
									}
								});
								
								classesSubmenu.add(constructorMenuItem);
							}
							
							classesSubmenu.addSeparator();
							
							for(Method method : declaredMethods) {
								final GMethod gMethod = new GMethod(clazz, method);
								if(gMethod.getModifier().contains("static") && !gMethod.getReturnType().equals("void")) {
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
															
															gMethod.invoke(null, GUtil.getInputFields());
														}
													}else {
														
														gMethod.invoke(null);
													}
												};
											}.start();
										}
									});
									classesSubmenu.add(methodMenuItem);
								}
							}
						}
						
						@Override
						public void menuDeselected(MenuEvent e) {
							classesSubmenu.removeAll();
						}
					});
					
					GFrame.getInstance().getClassesMenu().add(classesSubmenu);	
				}else
					GUtil.message("Klassenobjekt "+file.getName()+" schon im Menü 'Classes'!");
			}
			GUtil.displayClasses("Cached classes: ", GFrame.getInstance().getCachedClasses());
		}
	}

	public static File getCurrentDirectory() {
		return currentDirectory;
	}

	public static void setCurrentDirectory(File currentDirectory) {
		GAction.currentDirectory = currentDirectory;
	}		
}
