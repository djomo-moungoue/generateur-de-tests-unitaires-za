package com.application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.ProtectionDomain;

import com.util.GUtil;

/**
 * <p> Die Klasse GClassLoader erzeugt mit Hilfe der Methode <code>getClassObject()<code> einen 
 * KlassenObjekt aus einer gegebenen .class-Datei. Die .class-Datei muss 
 * im Order des Werkzeugs liegen, sonst wird eine ClassNotFoundException
 * ausgelöst. Sie darf dazu keine Klasse der Java-Bibliothek sein, ansonsten wird
 * eine SecurityException ausgel&ouml;st.</p>
 * @author SergeOliver
 *
 */
public class GClassLoader extends ClassLoader{
	
	/**
	 * <p>Datei zu laden.</p>
	 */
	private File file;	
	
	private Class<?> clazz;
	
	private String name;

	/**
	 * @param file im Dateisystem
	 */
	public GClassLoader(File file){
		this.file = file;
	}
	
	/**Sucht die Datei <code>file</code> im ganzen Dateisystem. Dann gibt
	 * den eindeutigen <code>FileChannel</code> die mit der Datei verbunden 
	 * ist. Map die Datei im Speicher und schließlich erzeugt den Klassenobjekt. 
	 * @return Klassenobjekt
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public Class<?> getClassObject(){
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel roChannel = raf.getChannel();
			ByteBuffer byteBuffer;
			byteBuffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int)roChannel.size());
			raf.close();
			roChannel.close();
			clazz = defineClass((String)null, byteBuffer, (ProtectionDomain)null);
			name = clazz.getName();
			clazz = Class.forName(name);
			if(!GFrame.getInstance().getCachedClasses().contains(clazz))
				GFrame.getInstance().getCachedClasses().add(clazz);
			return clazz;
		} catch (IOException | ClassNotFoundException e) {
			GUtil.exceptionMessage(e.getClass().getName(), e.getMessage());
			return null;
		}
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
