package core.utility;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * A minimal XML parser utility for parsing
 * certain type of XML documents
 * TODO: Better method names, they are confusing
 * @author Jani Nykänen
 *
 */
public class XMLParser {

	
	/** Input stream */
	private InputStream input;
	/** DB factory **/
	private DocumentBuilderFactory dbf;
	/** Document builder */
	private DocumentBuilder db;
	/** Document */
	private Document doc;
	/** Root */
	private Element root;
	/** Node list, "params" */
	private NodeList nodes;
	/** Node pointer */
	private int nodePointer;
	
	
	/**
	 * Opens XML parser
	 * @param path Path
	 * @throws Exception If something goes wrong
	 */
	public XMLParser(String path) throws Exception {
		
		// Open the file
		input = this.getClass().getClassLoader().getResourceAsStream(path);
		// Ready for parsing
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		doc = db.parse(input);
	}
	
	
	/**
	 * Opens XML parser
	 * @param path Path
	 * @throws Exception If something goes wrong
	 */
	public XMLParser(InputStream st) throws Exception {
		
		// Open the file
		input = st;
		// Ready for parsing
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		doc = db.parse(input);
	}
	
	
	/**
	 * Get root and prepare it to be used
	 * (or ignored)
	 */
	public void readyRoot() {
		
		root = doc.getDocumentElement();
	}

	
	/**
	 * Get a root attribute
	 * @param key Key
	 * @param def Default value
	 * @return Value, or default, if not found
	 */
	public String getRootAttribute(String key, String def) {
		
		String ret = root.getAttributes().getNamedItem(key).getTextContent();
		if(ret == null)
			return def;
		else
			return ret;
	}
	
	
	/**
	 * Ready a list of nodes to be used
	 * @param name Name
	 */
	public void readyNodeList(String name) {
		
		nodes = root.getElementsByTagName(name);
		nodePointer = 0;
	}
	
	
	/**
	 * Get next parameter
	 * @return If there is something remaining
	 */
	public boolean getNextParam() {
		
		return nodePointer ++ < nodes.getLength();
	}
	
	
	/**
	 * Get text content of a node
	 * @param name Parameter name
	 * @return Text content
	 */
	public String getTextContent(String name) {
		
		return getTextContent(nodes.item(nodePointer-1), name);
	}
	
	
	/**
	 * Get text content of a node
	 * @param n Node
	 * @param name Name
	 * @return Text content
	 */
	public String getTextContent(Node n, String name) {
		
		return n.getAttributes().getNamedItem(name).getTextContent();
	}
	
	
	/**
	 * Stop parsing
	 * @throws Exception If fails to close
	 */
	public void close() throws Exception {
		
		input.close();
	}
	
	
	/**
	 * Get document builder
	 * @return Document builder
	 */
	public DocumentBuilder getDocumentBuilder() {
		
		return db;
	}
	
	
	/**
	 * Get the root element
	 * @return Root element
	 */
	public Element getRoot() {
		
		return root;
	}
}
