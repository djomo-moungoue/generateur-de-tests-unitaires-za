package com.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import com.actiononmenu.AssertAction;
import com.actiononmenu.GAction;
import com.callitem.CallItem;
import com.testgenerator.NotStrictTestGenerator;
import com.testgenerator.OrdinaryTestGenerator;
import com.testgenerator.StrictTestGenerator;
import com.util.GUtil;

/**
 * <p>GFrame ist eine Singleton-Klasse. Sie repräsentiert 
 * die graphische Oberfl&uml;che der Anwendung und umfasst eine 
 * Men&uuml;-Leiste und ein Inhalts-Fenster.</p>
 * <p>Die Men&uuml;-Leiste enth&auml; f&uuml;nf Pull-Down-Men&uuml;s und 
 * zwar von links nach rechts <b>Classes</b>, <b>Objects</b>, <b>Assertions</b> 
 * <b>TestCases</b> und <b>Help</b>.</p>
 * <p>Das Inhalt-Fenster enth&auml;hlt zwei ComboBox am oberen Rand, ein 
 * großer Knopf im Zentrum und zwei kleineren Knöpfe am unteren Rand.</p>
 * <p>Das Menü Classes für das Laden und die Auflistung von Klassenobjekt.
 * Dient auch zur Anzeige von Konstruktoren und statitischen Methoden.</p>
 * <p>Das Menü Objects dient zur Anzeige von erstellten Klasseninstanzen und
 * den verbundenen Klassenmethoden.</p>
 * <p>Das Menü Assertions enthält die Liste von unterstützen assert-Methoden.</p>
 * <p>Das Menü Testfälle listet die aufgerufenen assert-Methoden auf.</p>
 * <p>Das Menü Help ist für die Bentzungsanleitung reserviert.</p>
 * <p>Über den obersten ComboBox kann man eingestellen ob die Anwendung 
 * NullPointer-Argumente annehmen soll (Default-Opiton) oder ob sie die in einer 
 * DefaultInstanz des Parameterstyps umwandeln soll. </p>
 * <p>Über den unteren ComboBox kann mann die Struktur der zu generierenden
 * Testklasse auswählen. Wählt man die erste Möglichkeit, dann wird ein Testklasse,
 * die die Benzutzerinteraktion eins zu eins entspricht. Die zweite Möglichkeit
 * trennt die Erstellung von Ojekten mit dem Aufruf von Assertionen. Die letzte
 * Möglichkeit trennt sogar die Assertionen und isoliert jede in einer Testmethode.</p>
 * <p>Der große Knopf dient zur Generierung der Testklasse. Die Knöpfe am
 * unteren Rand ermöglichen das Leeren von Menüs.</p>
 * @author SergeOliver
 *
 */
public class GFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Men&uuml;-Leiste, die f&uuml;nf Pull-Dowm-Men&uuml;s enth&auml;lt.</p>
	 */
	private JMenuBar menubar;
	
	
	/**
	 * <p>Pull-Dowm-Men&uuml;s geladene Klassenobjekten aufzulisten.</p>
	 */
	private JMenu classesMenu;
	
	
	/**
	 * <p>Pull-Dowm-Men&uuml;s zum Auflisten von erstellten Instanzen.</p>
	 */
	private JMenu objectsMenu;
	
	
	/**
	 * <p>Pull-Dowm-Men&uuml;s um Typen von Assertionen aufzulisten.</p>
	 */
	private JMenu assertionsMenu;
	
	/**
	 * <p>Pull-Dowm-Men&uuml;s um alle bereits erstellten Testf&auml;lle
	 * aufzulisten.</p>
	 */
	private JMenu testcasesMenu;
	
	/**
	 * <p>Pull-Dowm-Men&uuml;s zur Beschreibung der Anwendung. Wird die 
	 * Dokumentation der Anwendung und die Benutzungsanleitung enthalten.</p>
	 */
	private JMenu helpMenu;
	

	/**
	 * <p>Die Bet&auml;tigung &ouml;ffnet das Dateisystem und ermöglicht 
	 * die Auswahl von einer oder mehreren .class Dateien.</p>
	 */
	private JMenuItem newFileItem; 
	
	/**
	 * <p>Items zur Erstellung von Assertion durch Bet&auml;tigung und
	 * Eingabe der Argumenten im InputFensters.</p>
	 */
	private JMenuItem assertEquals, assertSame, assertNotSame,
	assertNull, assertNotNull, assertTrue, assertFalse;
	
	private JPanel topPanel, bottomPanel;
	
    /**
     * <p>Die Bet&auml;tigung von generateButton startet die Erstellung
     * der Testklasse.</p>
     */
    private JButton generateButton;
    
	/**
	 * <p>Entfernt Menüelemente aus Menüs</p>
	 */
	private JButton clearButton, clearAllButton;
	
	private String comboBoxItems[] = new String[3];
	private String comboBoxItems2[] = new String[2];
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox2;
	
	/**
	 * <p>Pfade von bereits geladenen Klassenobjekten um Doppelung in der
	 * Liste von Klassenobjekten zu vermeidung.</p>
	 */
	private ArrayList<File> cachedFiles = new ArrayList<File>();
	
	/**
	 * <p>Liste bereits geladener Klassenobjekten. N&uuml;tzlich f&uuml;r
	 * den Aufruf von statischen Methoden und die Erstellung von ConstructorCallItems
	 * und MethodCallItems.</p>
	 */
	private ArrayList<Class<?>> cachedClasses = new ArrayList<Class<?>>();
	
	/**
	 * <p>zeitliche Reihenfolge und Z&auml;hler von Aufrufen</p>
	 */
	private int callOrder = 0;
	
	/**
	 * <p>zeitliche Reihenfolge des Aufrufs von Konstruktoren.</p>
	 */
	private int constructorCallOrder = 0;
	
	/**
	 * <p>zeitliche Reihenfolge des Aufrufs von Assertionen.</p>
	 */
	private int assertCallOrder = 0;
	
	/**
	 * <p>Liste aller Aufrufe von Konstruktoren, Methoden und Assertionen auf
	 * die GUI der Anwendung. Die sind zeitlich sortiert.</p>
	 */
	private ArrayList<CallItem> cachedCallHistory = new ArrayList<>();
	
	/**
	 * <p>Name des Fensters.</p>
	 */
	private static String title = "Interactive Unit Test Case Generator";
	
	/**
	 * <p>Speicherort des ausführenden JAR-Datei im Dateisystem des Benutzers.</p>
	 */
	private static URL jarURL = GLauncher.class.getResource("../../");
	
	private URL giconURL = GLauncher.class.getResource("/com/resource/gicon.JPG");
	
	private final Icon icon = new ImageIcon(new ImageIcon(giconURL).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

	/**
	 * <p>Stelle des Fensters auf dem Bildschirm.</p>
	 */
	private Point position;
	
	/**
	 * <p>Gr&ouml:&szlig; des Fensters.</p>
	 */
	private Dimension dimension;
	
	/**
	 * <p>Referenz zu der einzigen Instanz der Singleton-Klasse GFrame.</p>
	 */
	private static GFrame gFrame = null;
	
	@Override
	public String toString() {
		return "GFrame [position=" + this.position + ", dimension="
				+ this.dimension + "]";
	}
	
	/**
	 * @return eine Singleton-Instanz der Klasse
	 */
	public static GFrame getInstance() {
		if(gFrame == null) {
			gFrame = new GFrame();
			GUtil.message("Erzeugt die Singleton-Instanz: "+gFrame);
		}
		return gFrame;
	}
	
	/**
	 * <p>Der Konstruktor GFrame ist einmalig bei der statischen Methode 
	 * getInstance aufgerufen.</p>
	 */
	private GFrame() {
		super(title);
		GUtil.message("Beginnt die Erzeugung des Fensters...");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		GUtil.message("Größe des Bildschirms: ("+dim.getWidth()+" , "+dim.getWidth()+")");
		
		int width = (int) (dim.getWidth())*3/5;
		int height = (int) (dim.getHeight())*3/5;
		position = new Point((int) (dim.getWidth() - width)/2, (int) (dim.getHeight() - height)/2);
		dimension = new Dimension(width, height);
		setLocation(position);
		setPreferredSize(dimension);
		pack();
		GUtil.message("Stelle des Fensters auf dem Bildschirm: ("+position.getX()+" , "+position.getY()+")");
		GUtil.message("Größe des Fensters: ("+width+" , "+height+")");
		
		setResizable(false);
		GUtil.message("Größe des Fensters nicht veränderbar.");
		
		
		setIconImage(new ImageIcon(giconURL).getImage());
		GUtil.message("Ersetzt das Standardicon des Fensters.");
		GUtil.message("Icon url: "+giconURL);
		GUtil.message("Icon url path: "+giconURL.getPath());
		

		//BAR
		menubar = new JMenuBar();
		GUtil.message("Erzeugt die Menü-Leiste.");
	
		//MENU
		classesMenu = new JMenu("Classes ");
		
		objectsMenu = new JMenu("Objects");
		
		assertionsMenu = new JMenu("Assertions");
		
		testcasesMenu = new JMenu("TestCases");
		
		helpMenu = new JMenu("Help");
		
		generateButton = new JButton("Generate Test Class");
		generateButton.setBackground(new Color(0,100,0));
		generateButton.setForeground(new Color(255,255,255));
		generateButton.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		clearButton = new JButton("Clear");
		clearButton.setBackground(new Color(255,185,15));
		clearButton.setForeground(new Color(255,255,255));
		clearButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		clearAllButton = new JButton("Clear all");
		clearAllButton.setBackground(new Color(100,0,0));
		clearAllButton.setForeground(new Color(255,255,255));
		clearAllButton.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		
		comboBoxItems[0] = "strict test class: 1 test method for all initialisations and cases. \nChronological order of user's interactions conserved.";
		comboBoxItems[1] = "not strict test class: 1 setUp method & 1 test method for all cases. \nChronological order of user's interactions not conserved.";
		comboBoxItems[2] = "ordinary test class': 1 setUp method & 1 test method per case. \nStructure of an usual JUnit test class.";
		comboBox = new JComboBox<String>(comboBoxItems);

		comboBoxItems2[0] = "Default instance instead of null pointer";
		comboBoxItems2[1] = "Null pointer allowed" ;
        comboBox2 = new JComboBox<String>(comboBoxItems2);
        comboBox2.setSelectedIndex(1);
        
		classesMenu.setMnemonic(KeyEvent.VK_C);
		objectsMenu.setMnemonic(KeyEvent.VK_O);
		assertionsMenu.setMnemonic(KeyEvent.VK_A);
		testcasesMenu.setMnemonic(KeyEvent.VK_T);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		generateButton.setMnemonic(KeyEvent.VK_G);
		
		classesMenu.setToolTipText("Geladene Klassenobjekten Alt+C");
		objectsMenu.setToolTipText("Erstellte Klasseninstanzen Alt+O");
		assertionsMenu.setToolTipText("Liste von AssertionTypen Alt+A");
		testcasesMenu.setToolTipText("Erstellte Testfällen Alt+T");
		helpMenu.setToolTipText("Anwendungshinweise Alt+H");
		comboBox.setToolTipText("<html>Eine option auswählen. 'Generate' drücken <br/>um die entsprechende Testklasse zu generieren</html>");
		comboBox2.setToolTipText("<html>Eine option auswählen.</html>");
		generateButton.setToolTipText("<html>Eine Option im ComboBox auswählen. Button drücken<br/>um die entsprechende Testklasse zu generieren Alt+G</html>");
		clearAllButton.setToolTipText("Menüs Classes, Objects und TestCases leeren");
		clearButton.setToolTipText("Menüs Objects und TestCases leeren");
		GUtil.message("Erzeugt die Pull-Down-Menüs und das Button...");
		
		menubar.add(classesMenu);
		menubar.add(objectsMenu);
		menubar.add(assertionsMenu);
		menubar.add(testcasesMenu);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(helpMenu);
		
		GUtil.message("Fügt die Pull-Down-Menüs in der Menü-Leiste ein.");
		
		newFileItem = new JMenuItem("Open File(s)...          Alt+F ");
		newFileItem.setMnemonic(KeyEvent.VK_F);
		newFileItem.addActionListener(new GAction());
		
		GUtil.message("Erzeugt das Menüelement 'Open File(s)...          Alt+F '" +
				", dass zur Auswahl von  .class Dateien im Dateisystem dient. ");

		classesMenu.add(newFileItem, 0);
		classesMenu.addSeparator();
		
		assertEquals = new JMenuItem("assertEquals   Alt+1");
		assertSame = new JMenuItem("assertSame     Alt+2");
		assertNotSame = new JMenuItem("assertNotSame Alt+3");
		assertTrue = new JMenuItem("assertTrue     Alt+4");
		assertFalse = new JMenuItem("assertFalse    Alt+5");
		assertNull = new JMenuItem("assertNull     Alt+6");
		assertNotNull = new JMenuItem("assertNotNull  Alt+7");
		
		assertEquals.setMnemonic(KeyEvent.VK_1);
		assertSame.setMnemonic(KeyEvent.VK_2);
		assertNotSame.setMnemonic(KeyEvent.VK_3);
		assertTrue.setMnemonic(KeyEvent.VK_4);
		assertFalse.setMnemonic(KeyEvent.VK_5);
		assertNull.setMnemonic(KeyEvent.VK_6);
		assertNotNull.setMnemonic(KeyEvent.VK_7);
		
		assertEquals.setToolTipText("<html>assertEquals(String message, Object expected, Object actual)" +
				"<br/>assertEquals(String message, long expected, long actual)" +
				"<br/>assertEquals(String message, double expected, double actual, double delta)</html>");
		
		assertSame.setToolTipText("assertSame(String message, Object expected, Object actual)");
		assertNotSame.setToolTipText("assertNotSame(String message, Object unexpected, Object actual)");
		assertTrue.setToolTipText("assertTrue(String message, boolean condition)");
		assertFalse.setToolTipText("assertFalse(String message, boolean condition)");
		assertNull.setToolTipText("assertNull(String message, Object object)");
		assertNotNull.setToolTipText("assertNotNull(String message, Object object)");
		
		assertEquals.addActionListener(new AssertAction("assertEquals"));
		assertSame.addActionListener(new AssertAction("assertSame"));
		assertNotSame.addActionListener(new AssertAction("assertNotSame"));
		assertTrue.addActionListener(new AssertAction("assertTrue"));
		assertFalse.addActionListener(new AssertAction("assertFalse"));
		assertNull.addActionListener(new AssertAction("assertNull"));
		assertNotNull.addActionListener(new AssertAction("assertNotNull"));
		GUtil.message("Erzeugt assertionsMenüItems...");

		assertionsMenu.add(assertEquals);
		assertionsMenu.add(assertSame);
		assertionsMenu.add(assertNotSame);
		assertionsMenu.add(assertTrue);
		assertionsMenu.add(assertFalse);
		assertionsMenu.add(assertNull);
		assertionsMenu.add(assertNotNull);
		GUtil.message("Fügt assertionsMenüItems im assertionsMenü hinzu...");
		setJMenuBar(menubar);
		
		setLayout(new BorderLayout());
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		bottomPanel.setLayout(new BorderLayout());
		
		topPanel.add(comboBox, BorderLayout.SOUTH);
		topPanel.add(comboBox2, BorderLayout.NORTH);
		bottomPanel.add(clearButton, BorderLayout.WEST);
		bottomPanel.add(clearAllButton, BorderLayout.EAST);
		add(topPanel, BorderLayout.PAGE_START);
		add(bottomPanel, BorderLayout.PAGE_END);
		
		generateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(((String)comboBox.getSelectedItem()).equals(comboBoxItems[0])) {
					
					new StrictTestGenerator(cachedCallHistory);
				
				}else if(((String)comboBox.getSelectedItem()).equals(comboBoxItems[1])) {
					
					new NotStrictTestGenerator(cachedCallHistory);
					
				}else if(((String)comboBox.getSelectedItem()).equals(comboBoxItems[2])) {
					
					new OrdinaryTestGenerator(cachedCallHistory);
					
				}
			}
		});
		add(generateButton, BorderLayout.CENTER);
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getObjectsMenu().removeAll();
				getTestcasesMenu().removeAll();
				setCachedCallHistory(new ArrayList<CallItem>());
				setCallOrder(0);
				setConstructorCallOrder(0);
				setAssertCallOrder(0);
			}
			
		});
		clearAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(getClassesMenu().getItemCount() > 2) {
					for(int i = 2; i<getClassesMenu().getItemCount(); i++)
						getClassesMenu().remove(i);
				}
				getObjectsMenu().removeAll();
				getTestcasesMenu().removeAll();
				setCachedClasses(new ArrayList<Class<?>>());
				setCachedFiles(new ArrayList<File>());
				setCachedCallHistory(new ArrayList<CallItem>());
				setCallOrder(0);
				setConstructorCallOrder(0);
				setAssertCallOrder(0);
			}
			
		});
		GUtil.message("Fügt den Button hinzu...");
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		       int option = JOptionPane.showConfirmDialog(GFrame.getInstance(), 
		            "Exit Interactive Unit Test Case Generator?", "Confirm Exit", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE);
		        	if(option == JOptionPane.YES_OPTION) {
		        		save();
			        	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        	}else if(option == JOptionPane.NO_OPTION) {
		        		GFrame.getInstance().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		        	}
		    }
		});

		setVisible(true);
		GUtil.message("Beendet die Erzeugung des Fensters.");
	}
	
	public JMenuBar getMenubar() {
		return menubar;
	}

	public void setMenubar(JMenuBar menubar) {
		this.menubar = menubar;
	}

	public JMenu getClassesMenu() {
		return classesMenu;
	}

	public void setClassesMenu(JMenu classesMenu) {
		this.classesMenu = classesMenu;
	}

	public JMenu getObjectsMenu() {
		return objectsMenu;
	}

	public void setObjectsMenu(JMenu objectsMenu) {
		this.objectsMenu = objectsMenu;
	}

	public JMenu getAssertionsMenu() {
		return assertionsMenu;
	}

	public void setAssertionsMenu(JMenu assertionsMenu) {
		this.assertionsMenu = assertionsMenu;
	}

	public JMenuItem getAssertNotEqualsItem() {
		return this.assertNotSame;
	}

	public void setAssertNotEqualsItem(JMenuItem assertNotEqualsItem) {
		this.assertNotSame = assertNotEqualsItem;
	}
	
	public JMenu getTestcasesMenu() {
		return this.testcasesMenu;
	}

	public void setTestcasesMenu(JMenu testcasesMenu) {
		this.testcasesMenu = testcasesMenu;
	}

	public void setHelpMenu(JMenu helpMenu) {
		this.helpMenu = helpMenu;
	}

	public JMenuItem getNewFileItem() {
		return newFileItem;
	}

	public void setNewFileItem(JMenuItem newFileItem) {
		this.newFileItem = newFileItem;
	}

	public JMenuItem getAssertEqualsItem() {
		return this.assertEquals;
	}

	public void setAssertEqualsItem(JMenuItem assertEqualsItem) {
		this.assertEquals = assertEqualsItem;
	}

	public JMenu getHelpMenu() {
		return helpMenu;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		GFrame.title = title;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
			
	public ArrayList<File> getCachedFiles() {
		return this.cachedFiles;
	}

	public void setCachedFiles(ArrayList<File> cachedFiles) {
		this.cachedFiles = cachedFiles;
	}

	public ArrayList<Class<?>> getCachedClasses() {
		return this.cachedClasses;
	}
		
	public void setCachedClasses(ArrayList<Class<?>> cachedClasses) {
		this.cachedClasses = cachedClasses;
	}
				
	public int getCallOrder() {
		return this.callOrder;
	}

	public void incCallOrder() {
		this.callOrder++;
	}

	public ArrayList<CallItem> getCachedCallHistory() {
		return this.cachedCallHistory;
	}

	public void setCachedCallHistory(
			ArrayList<CallItem> cachedCallHistory) {
		this.cachedCallHistory = cachedCallHistory;
	}

	/**
	 * @return wie oft Konstruktor bislang aufgerufen wurden.
	 */
	public int getConstructorCallOrder() {
		return this.constructorCallOrder;
	}

	/**
	 * erhöt die Zahl der Aufrufe um 1
	 */
	public void incConstructorCallOrder() {
		this.constructorCallOrder++;
	}

	/**
	 * @return wie oft assert-Methoden bislang aufgerufen wurden.
	 */
	public int getAssertCallOrder() {
		return this.assertCallOrder;
	}

	/**
	 * erhöt die Zahl der Aufrufe um 1
	 */
	public void incAssertCallOrder() {
		this.assertCallOrder++;
	}

	public JMenuItem getAssertEquals() {
		return this.assertEquals;
	}

	public void setAssertEquals(JMenuItem assertEquals) {
		this.assertEquals = assertEquals;
	}

	public JMenuItem getAssertSame() {
		return this.assertSame;
	}

	public void setAssertSame(JMenuItem assertSame) {
		this.assertSame = assertSame;
	}

	public JMenuItem getAssertNotSame() {
		return this.assertNotSame;
	}

	public void setAssertNotSame(JMenuItem assertNotSame) {
		this.assertNotSame = assertNotSame;
	}

	public JMenuItem getAssertNull() {
		return this.assertNull;
	}

	public void setAssertNull(JMenuItem assertNull) {
		this.assertNull = assertNull;
	}

	public JMenuItem getAssertNotNull() {
		return this.assertNotNull;
	}

	public void setAssertNotNull(JMenuItem assertNotNull) {
		this.assertNotNull = assertNotNull;
	}

	public JMenuItem getAssertTrue() {
		return this.assertTrue;
	}

	public void setAssertTrue(JMenuItem assertTrue) {
		this.assertTrue = assertTrue;
	}

	public JMenuItem getAssertFalse() {
		return this.assertFalse;
	}

	public void setAssertFalse(JMenuItem assertFalse) {
		this.assertFalse = assertFalse;
	}

	public JButton getGenerateButton() {
		return this.generateButton;
	}

	public void setGenerateButton(JButton generateButton) {
		this.generateButton = generateButton;
	}

	public String[] getComboBoxItems() {
		return this.comboBoxItems;
	}

	public void setComboBoxItems(String[] comboBoxItems) {
		this.comboBoxItems = comboBoxItems;
	}

	public String[] getComboBoxItems2() {
		return this.comboBoxItems2;
	}

	public void setComboBoxItems2(String[] comboBoxItems2) {
		this.comboBoxItems2 = comboBoxItems2;
	}

	public JComboBox<String> getComboBox() {
		return this.comboBox;
	}

	public void setComboBox(JComboBox<String> comboBox) {
		this.comboBox = comboBox;
	}

	public JComboBox<String> getComboBox2() {
		return this.comboBox2;
	}

	public void setComboBox2(JComboBox<String> comboBox2) {
		this.comboBox2 = comboBox2;
	}
	
	/**
	 * Speichert der zuletzt besuchte Order im Dateisystem
	 */
	private static void save() {
		File saveState = new File(jarURL.getFile()+"currentDirectory.txt");
		System.out.println("currentDirectory: "+saveState.getPath());
		
		ObjectOutputStream output = null;
		try{
			output = new ObjectOutputStream(new FileOutputStream(saveState));
			output.writeObject(GAction.getCurrentDirectory());
			output.close();
			
		}catch (IOException e){
			GUtil.exceptionMessage(e.getClass().getName(),e.getMessage());
		}catch(NullPointerException e){
			GUtil.exceptionMessage(e.getClass().getName(),e.getMessage());
		}
	}
	
	/**
	 * Wiederherstellt der zuletzt besuchte Order während dem vorherigen Lauf
	 * des Werkzeugs.
	 */
	public static void restore() {
		File saveState = new File(jarURL.getFile()+"currentDirectory.txt");
		if(saveState.exists()) {
			System.out.println("currentDirectory: "+saveState.getPath());
		    ObjectInputStream input = null;
		    try{
			    input = new ObjectInputStream(new FileInputStream(saveState));
			    GAction.setCurrentDirectory((File)input.readObject());
			   
		    }catch (IOException e){
		    	GUtil.exceptionMessage(e.getClass().getName(),e.getMessage());
		    }catch (ClassNotFoundException e) {
		    	GUtil.exceptionMessage(e.getClass().getName(),e.getMessage());
			}
		}
    }

	public URL getGiconURL() {
		return this.giconURL;
	}

	public void setGiconURL(URL giconURL) {
		this.giconURL = giconURL;
	}

	public static URL getJarURL() {
		return GFrame.jarURL;
	}

	public static void setJarURL(URL jarURL) {
		GFrame.jarURL = jarURL;
	}

	public JPanel getTopPanel() {
		return this.topPanel;
	}

	public void setTopPanel(JPanel topPanel) {
		this.topPanel = topPanel;
	}

	public JPanel getBottomPanel() {
		return this.bottomPanel;
	}

	public void setBottomPanel(JPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	public JButton getClearButton() {
		return this.clearButton;
	}

	public void setClearButton(JButton clearButton) {
		this.clearButton = clearButton;
	}

	public JButton getClearAllButton() {
		return this.clearAllButton;
	}

	public void setClearAllButton(JButton clearAllButton) {
		this.clearAllButton = clearAllButton;
	}

	public void setCallOrder(int callOrder) {
		this.callOrder = callOrder;
	}

	public void setConstructorCallOrder(int constructorCallOrder) {
		this.constructorCallOrder = constructorCallOrder;
	}

	public void setAssertCallOrder(int assertCallOrder) {
		this.assertCallOrder = assertCallOrder;
	}

	public Icon getIcon() {
		return this.icon;
	}
}
