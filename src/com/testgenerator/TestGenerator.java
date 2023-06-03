/**
 * 
 */
package com.testgenerator;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.application.GFrame;
import com.application.GLauncher;
import com.callitem.CallItem;
import com.util.GUtil;

/**
 * <p>Die abstrakte Klasse TestGenerator umfasst Funktionalitäten zur
 * Generierung von TestKlassen.</p>
 * @author SergeOliver
 *
 */
public abstract class TestGenerator {

	private String fileName =  "";
	private File file = null;
	private String pathName = GFrame.getInstance().getJarURL().getFile(); //for launching a release jar file
//	private String pathName = new File(System.getProperty("user.dir")).getPath()+File.separator; //for launching inside eclipse
	private ArrayList<CallItem> history;
	protected String head = "";
	protected String setUp = "";
	protected String body = "";
	protected String testCase = "";
	private String tail = "";
	private String tab = "    ";
	private String newLine = "\n";
	private String freeLine = "\n\n";
	
	public TestGenerator(ArrayList<CallItem> history, String fileName) {
		this.setHistory(history);
		this.setFileName(fileName);
		head += "import static org.junit.Assert.*;"+newLine;
		head += "import org.junit.*;"+freeLine;
		head += "public class "+getFileName().substring(0, getFileName().indexOf(".java"))+" {"+newLine;
		GUtil.message("Generating...");
	}
	
	public abstract void generateTestClass();
	
	public abstract void generateTestCase(String testCase, int assertID);
	
	public void createFile(String pathName, String fileName, String fileContent) {
		try {
			file = new File(pathName+fileName);
			FileWriter fw = new FileWriter(file);
			fw.write(fileContent);
			fw.flush();
			fw.close();
		}catch(Exception e) {
			GUtil.exceptionMessage( e.getClass().getName(), e.getMessage());
		}
	}

	
	public String getFileName() {
		return this.fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHead() {
		return this.head;
	}
	public void setHead(String head) {
		this.head += newLine+tab+head+";";
	}

	public String getSetUp() {
		return this.setUp;
	}

	public void setSetUp(String setUp) {
		this.setUp += newLine+tab+tab+setUp+";";
	}
	
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body += newLine+body;
	}
	
	public String getTail() {
		return this.tail;
	}
	
	public void setTail(String tail) {
		this.tail = tail;
	}
	
	public String getTestCase() {
		return this.testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	
	public String getTab() {
		return this.tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getNewLine() {
		return this.newLine;
	}
	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}
	public String getFreeLine() {
		return this.freeLine;
	}
	public void setFreeLine(String freeLine) {
		this.freeLine = freeLine;
	}
	public ArrayList<CallItem> getHistory() {
		return this.history;
	}
	public void setHistory(ArrayList<CallItem> history) {
		this.history = history;
	}
	public File getFile() {
		return this.file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public String getPathName() {
		return this.pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
}
