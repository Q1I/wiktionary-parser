/*
 * XMLParserDemo.java
 */

// allgemeine Java-Klassen 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.Hashtable;

// Klassen der SAX-Parser-API
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;



/**
 * Demoprogramm zum Parsen von XML-Daten.
 * <P>
 * Klasse ist von DefaultHandler abgeleitet. Diese implementiert alle
 * notwendigen Methoden des ContentHandler-Interface, so dass nur die
 * projektspezifischen Methoden &uuml;berladen werden m&uuml;ssen.
 * <P>
 * Diese Demo-Klasse liest alle wichtigen Daten aus der cities.xml Datei ein.
 * F&uuml;r diese Daten fehlt nur noch die Datenbankschnittstelle.<BR>
 * Sie sollte sich aber auch einfach an die Anforderungen der country????.xml
 * Dateien anpassen lassen.
 * 
 * @author Timo B&ouml;hme
 * @version 1.0
 */
public class WiktionaryParser extends DefaultHandler {

	// -- Globale Variablen ----------------------------------------------------
	/** XML Parser (mit SAX API) */
	public final static String parserClass = "org.apache.xerces.parsers.SAXParser";

	/**
	 * enth&auml;lt Zeichendaten eines Elements<BR>
	 * Achtung: mixed content wird nicht bzw. falsch behandelt
	 */
	private StringBuffer elementTextBuf = new StringBuffer();
	/** enth&auml;lt die Elementnamen des aktuellen Pfades */
	private Vector xmlPath = new Vector();

	public int countError=0;
	public int countOk=0;
	
	
	// Log
	private String logError;
	private String log;
	
	private Resource curRes;
	private String lastUri;

	private boolean start =true;
	private String outputFile="resource/add_index2.xml";
//	private static String inputFile="resource/data.rdf";
	private static String inputFile="resource/w-split02";
	private Queue<Label> qLabel;
	private Queue<Resource> qRes;
	
	private Label curLabel;
	
	// -------------------------------------------------------------------------
	/** Constructor */
	public WiktionaryParser() {
		this.qLabel=new LinkedList<Label>();
		this.qRes=new LinkedList<Resource>();
		this.curLabel=new Label();
		
	curRes=new Resource();
		logError="\n\n===========================================================" +
				"\nError-Log:\n";
		log="Log:\n";

	}

	// == XML spezifische Methoden =============================================
	// -------------------------------------------------------------------------
	/**
	 * Ermittelt den Namen des &uuml;bergeordneten Elementes.
	 * 
	 * @return Namen des &uuml;bergeordneten Elementes oder <code>null</code>
	 *         wenn kein &uuml;bergeordnetes Element vorhanden ist
	 */
	private String getParent() {
		return (xmlPath.size() > 1) ? (String) xmlPath
				.elementAt(xmlPath.size() - 2) : "";
	}

	// == Call back Routinen des Parsers =======================================
	// -------------------------------------------------------------------------
	/**
	 * Wird vom Parser beim Start eines Elements aufgerufen.<BR>
	 * (call back method)
	 */
	public void startElement(String namespaceURI, String localName,
			String rawName, Attributes atts) {

		// neues Element -> Textinhalt zuruecksetzen
		elementTextBuf.setLength(0);
		// aktuellen Elementnamen an Pfad anf�gen
		xmlPath.addElement(rawName);

		// gesonderte Behandlungsroutinen je nach Elementtyp
		// Book
		if (rawName.equals("rdf:Description")) { // neues Buch gefunden
			lastUri= parseUri(atts.getValue("rdf:about"));
			echo("###" + localName + 
					"\nattr[0]: " + atts.getValue("rdf:about"));
			return;
		}
		if (rawName.equals("ns0:label")) {
			curLabel = new Label();
//			System.out.println("lastUri: "+lastUri);
			curLabel.setUri(lastUri);
			write(false);
//			if(start == false && curRes.getLanguage()!=null){
//				write();
//
//	            curRes.reset();
//			}
//			echo("echo: "+lastUri);
//			curRes.setUri(lastUri);
			return;
		}

		if (rawName.equals("ns0:hasLangUsage")) {
			echo("hasLangUsage");
			Label l = getLabel(lastUri);
			if (l==null){
				l = new Label();
			}
			Label newL = new Label();
			newL.setLabel(l.getLabel());
			newL.setUri(parse(atts.getValue("rdf:resource")));
			echo("parse(atts.getValue(rdf:resource)) "+parse(atts.getValue("rdf:resource")));
			addLabel(newL);
		}
		
		if (rawName.equals("ns0:language")) { // neues Buch gefunden
			if(start==false && curRes.getLanguage()!=null){
				write(false);

//	            curRes.reset();
			}
			Label l = getLabel(lastUri);
			if (l == null){
				System.out.println("get Label is null: "+lastUri);
				curRes = new Resource();
				
			}else{
//			l.setLabel(atts.getValue("rdf:resource"));
			curRes = getRes(l);
			curRes.setLanguage(parse(atts.getValue("rdf:resource")));
			
			}
//			echo("echo: "+lastUri);
//			curRes.setUri(lastUri);
//			echo("###" + localName + 
//					"\nUri: " + lastUri);
			return;
		}
	
		if (rawName.equals("ns0:hasPoS")) { // neues Buch gefunden
			curRes.addHasPoS(parse(atts.getValue("rdf:resource")));
		}
		
//		if (rawName.equals("ns0:language")) { // neues Buch gefunden
//			curRes.setLanguage(parse(atts.getValue("rdf:resource")));
//		}
//		if (rawName.equals("ns0:hasSense")) { // neues Buch gefunden
//			curRes.addHasSense(parse(atts.getValue("rdf:resource")));
//		}
		
	}

	// -------------------------------------------------------------------------
	/**
	 * Wird vom Parser beim Ende eines Elements aufgerufen.<BR>
	 * (call back method)
	 */
	public void endElement(String namespaceURI, String localName, String rawName) {

		// entferne Whitespace an Zeichendatengrenzen
		String elementText = parse(elementTextBuf.toString().trim());


			if (rawName.equals("ns0:language")) {

					start=false;
//					curRes.setLanguage(elementText);
//					countOk++;
			}
			
			if (rawName.equals("ns0:label")) { // neues Buch gefunden
				curLabel.setLabel(elementText);
				addLabel(curLabel);
			
				curRes = getRes(curLabel);
//				curRes.setLabel(elementText);
			}
			
			if (rawName.equals("ns0:hasEtymology")) { // neues Buch gefunden
				curRes.setHasEtymology(elementText);
			}
			if (rawName.equals("ns0:hasExampleSentence")) { // neues Buch gefunden
				curRes.addHasExample(elementText);
			}
			if (rawName.equals("ns0:hasMeaning")) { // neues Buch gefunden
				curRes.addHasMeaning(elementText);
			}
			if (rawName.equals("ns0:hasProverb")) { // neues Buch gefunden
				curRes.addHasProverb(elementText);
			}
			if (rawName.equals("ns0:hasHyphenationSingular")) { // neues Buch gefunden
				curRes.setHasHyphenationSingular(elementText);
			}
			if (rawName.equals("ns0:hasHyphenationPlural")) { // neues Buch gefunden
				curRes.setHasHyphenationPlural(elementText);
			}
			if(rawName.equals("rdf:RDF"))
				write(false);
				return;
			
	
//			

	}
	

	private void write(boolean end) {
		if(qRes.size()<30 && end==false){
			echo("qRes too small: "+qRes.size());
			return;
		}else{
			System.out.println("###WRITE:");
			Resource res = qRes.poll();
			if(res!=null)
				echo("res: "+res.getUri()+" , "+res.getLabel());
			else 
				System.out.println("res is null");
		if(res.getLanguage()==null)
			return;
		try{
			
            FileWriter fstream = new FileWriter(outputFile,true);
            BufferedWriter fbw = new BufferedWriter(fstream);
            fbw.write("<doc>\n");
            fbw.write("<field name='id'>"+res.getUri()+"</field>\n");
            fbw.write("<field name='label'>"+res.getLabel()+"</field>\n");
            fbw.write("<field name='language'>"+res.getLanguage()+"</field>");
            if(res.getHasPoS()!=null){
            	for(int i = 0;i<res.getHasPoS().size();i++)
            	fbw.write("\n<field name='hasPoS'>"+res.getHasPoS().get(i)+"</field>");
            }
//            fbw.newLine();
            if(res.getHasSense()!=null){
            	for(int i = 0;i<res.getHasSense().size();i++)
            	fbw.write("\n<field name='hasSense'>"+res.getHasSense().get(i)+"</field>");
//            fbw.newLine();
            }if(res.getHasEtymology()!=null){
            	fbw.write("\n<field name='hasEtymology'>"+res.getHasEtymology()+"</field>");
            }if(res.getHasMeaning()!=null){
            	for(int i = 0;i<res.getHasMeaning().size();i++)
            	fbw.write("\n<field name='hasMeaning'>"+res.getHasMeaning().get(i)+"</field>");
            }if(res.getHasExample()!=null){
            	for(int i = 0;i<res.getHasExample().size();i++)
            		fbw.write("\n<field name='hasExampleSentence'>"+res.getHasExample().get(i)+"</field>");
            }if(res.getHasProverb()!=null){
            	for(int i = 0;i<res.getHasProverb().size();i++)
            		fbw.write("\n<field name='hasProverb'>"+res.getHasProverb().get(i)+"</field>");
            }if(res.getHasHyphenationSingular()!=null){
            	fbw.write("\n<field name='hasHyphenationSingular'>"+res.getHasHyphenationSingular()+"</field>");
            }if(res.getHasHyphenationPlural()!=null)
            	fbw.write("\n<field name='hasHyphenationPlural'>"+res.getHasHyphenationPlural()+"</field>");
           fbw.write("\n</doc>\n");
//            fbw.newLine();
            fbw.close();
            countOk++;
//            if(countOk>50){
//            	try{
//       			  fstream = new FileWriter(outputFile,true);
//       	          fbw = new BufferedWriter(fstream);
//       	         fbw.write("</add>");
//       	      fbw.close();
//       	        
//       		     }catch (Exception e) {
//       		         System.out.println("Error: " + e.getMessage());
//       		     }
//       		
//            	System.out.println("END");
//            	System.exit(0);
//            	
//            }
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
		}
	}


	/** Remove invalid chars*/
	private String parse(String trim) {
		String parsed = trim.replaceAll("<", "");
		parsed = parsed.replaceAll(">", "");
		parsed = parsed.replaceAll("&nbsp;", "");
		parsed = parsed.replaceAll("&", "");
		
		return parsed;
	}

	private String parseUri(String trim) {
		echo("Parse URI: "+trim);
		String parsed = parse(trim);
		int count=0;
		int position=0;
		for(int i=0; i<trim.length();i++){
			if(trim.charAt(i)==('-')){
				++count;
				if (count == 2){
					position=i;
//					System.out.println("found -");
				}
			}
		}
		if(count>1){
			parsed = parsed.substring(0, position);
		}
		echo("Echo pasred: "+parsed);
		return parsed;
	}
	
	// -------------------------------------------------------------------------
	/**
	 * Wird vom Parser mit Textinhalt des aktuellen Elements aufgerufen.<BR>
	 * (call back method)
	 * <P>
	 * Achtung: Entities (auch Zeichenreferenzen wie &amp;ouml;) stellen eine
	 * Textgrenze dar und werden durch einen erneuten Aufruf dieser Funktion
	 * &uuml;bergeben.
	 */
	public void characters(char[] ch, int start, int length) {
		elementTextBuf.append(ch, start, length);
		// echo(elementTextBuf.toString());
	}

	// -------------------------------------------------------------------------
	/** Initialisiert Parser und started Proze� */
	public void doit(String dataFilename) {
		boolean success = (new File(outputFile)).delete();
		if (!success) {
		    // Deletion failed
		}
		
		
		// XML Parser
		XMLReader parser = null;

		// Parser instanziieren
		try {
			parser = XMLReaderFactory.createXMLReader(parserClass);
		} catch (SAXException e) {
			System.err.println("Fehler beim Initialisieren des Parsers ("
					+ parserClass + ")\n" + e.getMessage());
			System.exit(1);
		}
		try{
			 FileWriter fstream = new FileWriter(outputFile,true);
	         BufferedWriter fbw = new BufferedWriter(fstream);
	         fbw.write("<add>\n");
	         fbw.close();
	         System.out.println("add ");
	        
		     }catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		     }
		// Ereignisse sollen von dieser Klasse behandelt werden
		parser.setContentHandler(this);

		// Parser starten
		try {
			parser.parse(dataFilename);
		} catch (SAXException e) {
			System.err.println("Parser Exception:\n" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Parser IOException:\n" + e.getMessage());
		}
		
		while(qRes.size()>0){
			write(true);
		}
		// Write
		System.out.println(logError);
		System.out.println("\n\nCount Error: "+countError);
		System.out.println("Count Ok: "+countOk);
		
		
		try{
			 FileWriter fstream = new FileWriter(outputFile,true);
	         BufferedWriter fbw = new BufferedWriter(fstream);
	         fbw.write("</add>");
	         fbw.close();
	        
		     }catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		     }
		
	}

	// -------------------------------------------------------------------------
	/** Gibt Aufrufsyntax zur&uuml;ck */
	public static void usage() {
		System.out.println("usage: java XMLParserDemo <XML_FILE>");
		System.exit(1);
	}

	public void echo(String s) {
//		System.out.println(s);
		
	}

	public void log(String s) {
//		System.out.println(s);
		// log normal
		
	}
	
	public void logError(String s) {
		if(s.length()!=0){
			System.out.println(s);
			// log normal 
			// log error in file
			logError+="## "+s+"\n";
		}
	}
	public void addLabel(Label label){
		// search queue
		echo("ADD label "+label.getUri()+ " , "+label.getLabel());
		echo("qLabel size "+qLabel.size());
		if(qLabel.size()>0){
			echo("Search qLabel for "+label.getUri()+ " , "+label.getLabel());
			Iterator<Label> it = this.qLabel.iterator();
			while(it.hasNext()){
				Label l = it.next();
//				System.out.println(l.getUri());
				if(l.getUri().equals(label.getUri())){
					return;
				}
			}
		}
		echo("Added: "+label.getUri());
		this.qLabel.add(label);
		if(qLabel.size()>500)
			qLabel.poll();
	}
	
	public Label getLabel(String uri){
		Iterator<Label> it = this.qLabel.iterator();
		while(it.hasNext()){
			Label l = it.next();
			if(l.getUri().equals(uri)){
				return l;
			}
		}
		return null;
	}
	
	
	public Resource getRes(Label label){
		echo("Get res: "+label.getUri()+" , "+label.getLabel());
		Iterator<Resource> it = this.qRes.iterator();
		while(it.hasNext()){
			Resource l = it.next();
//			System.out.println("res finding "+l.getUri()+" , "+l.getLabel());
			if(l.getUri().equals(label.getUri())){
				echo("res found!");
				return l;
			}
		}
		// new res
		Resource res = new Resource();
		res.setUri(label.getUri());
		res.setLabel(label.getLabel());
//		System.out.println("add res: "+res.getUri()+ " , "+res.getLabel());
		qRes.add(res);
		return res;
	}
	
	// -------------------------------------------------------------------------
	/**
	 * Programmstart
	 * 
	 * @param args
	 *            Aufrufparameter
	 */
	public static void main(String args[]) {

		// Programminstanz erzeugen
		WiktionaryParser prg = new WiktionaryParser();
		// ausfuehren
		prg.doit(inputFile);

	}

}
