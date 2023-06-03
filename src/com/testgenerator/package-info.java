
/**http://docs.oracle.com/javase/specs/index.html
 * <p>Stand der Implimentierung:</p>
 * <p>Die Position und die Gr&ouml;&szlig;e der Anwendung entspricht jeweils
 * eins zehntel und vier f&uuml;nftel der Gr&ouml;&szlig;e der Bildschirms des
 * Ger&auml;ts
 * </p>
 * <p>
 * Man kann eine oder mehrere Klassenobjekte in der Anwendung laden. Das 
 * Dokument-Verzeichnis des Benutzers ist der standard Ort, in dem die 
 * Anwendung Klassenobjekte sucht. Nach jeder Suche merkt sich die Anwen-
 * dung das aktuelle Verzeichnis für die n&auml;chstfe Suche.
 * </p>
 * Das Pull-down-Men&uuml; filtert Duplikate von Klassenobjekten. Die 
 * Berührung eines Klassenobjekts listet alle Konstruktore und Methoden 
 * der Klasse. Die Modifikatoren, den R&uuml;ckgabewert, die Liste der 
 * Parameter und die geworfenen Ausnahme sind auch angezeigt.
 * </p>
 * <p>
 * JConstructor enth&auml;lt die Daten und Methoden zur besseren Darstellung
 *  von Konstruktoren im Pull-Down-Men&uuml;.
 * JMethod enth&auml;lt die Daten und Methoden zur besseren Darstellung
 *  von Methoden im Pull-Down-Men&uuml;.
 *  </p>
 *  <p>
 * Wenn man einen pameterlosen Konstruktoren bet&auml;tigt, dann wird
 * eine Instanzen der entsprechenden Klasse erstellt und im Men&uml; Objects
 * hinzugef&uuml;gt. 
 * Die Bet&auml;tigung einen Konstruktor mit Parametern ouml;ffnet ein 
 * Dialog-Fenster, damit der Benutzer Argumente angibt. Die Zahl der 
 * Angabefelder entspricht die Zahl der Argumente. Dei Ber&uuml;hrung eines 
 * Feldes zeigt den Typ des Arguments.
 * </p>
 * Erstellung von Objekten mit Objekten beliebiges Typs und Daten-
 * strukturen als Argument.
 * Liste von Instanzen
 * TODO: Testklasse generieren
 * 
 * Abgrenzungen:
 * - Darstellung von verschachtelten generischen Typen
 * - neue Instanz von Konstruktoren, die Datenstrukturen oder Objekte als
 * Argument erhalten, erstellen
 * - Laden von Klassenobjekten der java Bibliothek. Ist diese F&auml;higkeit
 * sinnvoll.
 * - Entpacken von jar/zip Dateien
 * - Benutzereingabe auf Korrekheit &uuml;berpr&uuml;fen.
 * -Generierung der Testklasse. Was ist sinnvoller. Eine Testklasse f&uuml;r
 * alle Klassen im Pull-Down-Men&uum; oder je eine Testklasse?
 * @author SergeOliver
 *
 */
package com.testgenerator;

/*Besprechung 24.09.2015 12:00 - 13:30
 * 
 * Zummenfassung von meinem Verstand aus der Besprechung
 * 
 * Die Anwendung soll nur primitive Typen und Instanzen der Klassen des
 * Benutzers berücksichtigen. Das Erzeugen von Arrays und Objekte der Java 
 * Bibliothek ist bislang ausgeschlossen.
 * Jede Klasse soll ein Pull-Down-Menü sein, das Konstruktoren und statische Me-
 * Methoden auflistet.
 * Jedes Obekt soll ein Pull-Down-Menü sein, das Instanzmethoden der Klasse
 * auflistet.
 * Der Benutzer soll vorhandene Okjekte aus dem Menü von Objekten als
 * Argument von anderen Konstruktoren oder von Methoden angeben können.
 * Die Anwendung soll sich erstellte Objekte mit deren Argumenten merken.
 * Die Anwendung soll sich aufgerufene InstanzMethoden mit deren Argumenten und
 * dem entsprechenden Objekt merken.
 * Die Anwendung soll sich aufgerufene statische Methode mit deren Argumenten und
 * der entsprechenden Klasse merken.
 * Ein Assertion-Objekt soll so aussehen name(arg1, arg2), wobei:
 * - name ist assertEquals, asserNotEquals, asserTrue, asserFalse,
 * - arg1 ist ein primitiver Wert, ein Obekt oder einen existieren Methodenaufruf,
 * - arg2 ist ein Erwartungswert.
 * Die Testklasse soll eine Methode setUp, die aufgerufen werden soll um
 * neue Objekte erzeugen.
 * Habe ich irgendwas missverstanden oder ausgelassen?
 * 
 * Nächster Termin 01.10.2015 13:00
 * 
 * Lieber Herr Djomo Moungoue,
ich habe Ihre Zusammenfassung etwas umformuliert:
 
·        Die Anwendung soll nur primitive Typen und Instanzen der Klassen des Benutzers berücksichtigen. 
		 Das Erzeugen von Arrays und Objekte der Java Bibliothek ist bislang ausgeschlossen.(noch nicht beschränkt)
·        Zu jeder Klasse soll es ein Pull-Down-Menü geben, das Konstruktoren 
		 und statische Methoden der Klasse auflistet.(erledigt)
·        Zu jedem vom Benutzer erzeugten Objekt, soll es ein Pull-Down-Menü
 		 geben, das Instanzmethoden der Klasse auflistet.(erledigt)
·        Der Benutzer soll vorhandene Okjekte aus dem Menü von Objekten als 
		 Argument beim Aufruf von Konstruktoren oder Methoden angeben können.(erledigt)
·        Die Anwendung soll vom Benutzer erzeugte Objekte merken,(erledigt)
·        sowie den Methoden- oder Konstrukturaufruf und dessen Argumente, 
 		 durch den sie erzeugt wurden.(erledigt)
·        Die Anwendung soll sich aufgerufene Instanzmethoden mit deren 
		 Argumenten und dem entsprechenden Objekt merken.(erledigt)
·        Die Anwendung soll sich aufgerufene statische Methode mit deren 
		 Argumenten und der entsprechenden Klasse merken.(erledigt)
·        Eine Assertion besteht aus dem Methodennamen der Assertion und 
         zwei Argumenten: name([arg0], arg1,arg2, [arg3])
		o   name ist assertEquals, assertTrue, assertFalse, 
			assertSame, assertNotSame, assertNull, assertNotNull
		o   arg1 ist ein primitiver Wert, ein Obekt oder einen existieren Methodenaufruf,
		o   arg2 ist ein primitiver Wert, ein Obekt oder einen existieren Methodenaufruf,
·        Es soll ein JUnit-Test erzeugt werden. Alle aufgezeichneten [Methoden-] und (erledigt)
		 Konstruktorenaufrufe sollen in der setup-Methode des Testfalls ausgeführt werden. 
		 Die Assertions in einer (später vielleicht auch auch mehreren) Testmethoden.
		 Die zu ladenden Klassenobjekten soll sich auf dem CLASSPATH des Programms befinden.
		 Also in denselben Ordner als die Klasse mit der Methode main. z.B. Order bin in Eclipse

 * */
/*
 * 1. Filterfunktion für die Benutzereingaben (erledigt In Bearbeitung: http://www.javalobby.org/java/forums/t20551.html)
 * 2. Abfangen von Ausnahmen (erledigt)
 * 3. Fields(Path, Package name,  File name) (nicht notwendig)
 * 4. Woher kommen die null? (erledigt)[Grund war die Konkatenierung von ungesetzte Instanzvariable vom Typ String]
 * 5. Wie kann man die Objekte, die der Benutzer erstellt hat in der Testdatei nutzen?(erledigt)[Aufruf wiederherstellen]
 * 6. Aufbau der Benutzersicht umstrukturieren um den Vergleich zu verbessern.(erledigt)
 * Der Aufbau bei Konstruktoren, Methoden und Assertionen soll identisch sein.(erledigt)
 * 7. equals, hashcode und comparator zum Vergleich von CallItem (nicht notwendig)
 * 8. Generate Button editable erst, wenn die Liste von Assertionen nicht leer ist(nicht notwendig)
 * 9. assert[Not]Null oder assert[Not]Same möglich erst, wenn die Liste von Objekten nicht leer ist (nicht notwendig)
 * 10. Die Anwendung von Dritten testen lassen (erledigt)
 * 11. Besprechungstermin mit dem Betreuer vereinbaren (erledigt)
 * 
 * 12. Unterschiedliche Ergebnisse bei Konstruktoren die Seiteneffekte verursachen mit einem Beispiel darstellen (In bearbeitung)
 * 13. null Pointer statt parameterlose Instanz/ null Pointer zulässig / null Pointer unzulässig (TODO???)
 * 14. Eingabe von nicht geladenen Klassenobjekt als Argument
 * 13,14 in createConstructorInputFields und createMethodInputFields.
 * */
/*REFACTORING
 * 
 * 1. Klasse JLauncher
 * Startpunkt der Anwendung mit der Methode main
 * 
 * 2. Klasse JFrame
 * verwendet das Singleton-Pattern, sodass jede Funktionalität für den ganzen
 * Ablauf gilt.
 * 
 * 
 * */
 