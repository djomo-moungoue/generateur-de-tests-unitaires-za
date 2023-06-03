package com.application;

import com.util.GUtil;

/**
 * <p>Startpunkt der Anwendung mit der Methode <code>main</code> die
 * eine Singleton-Instanz einer <code>GFrame</code> erstellt.</p>
 * @author SergeOliver
 *
 */
public class GLauncher {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GFrame.getInstance();
		GFrame.restore();
		GUtil.message("Anwendung laufend...");
	}
}
