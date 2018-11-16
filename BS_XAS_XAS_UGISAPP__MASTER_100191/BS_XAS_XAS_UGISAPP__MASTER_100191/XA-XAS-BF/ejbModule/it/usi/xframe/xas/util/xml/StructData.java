package it.usi.xframe.xas.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility used to read data organized as nested information inside XML file.
 * 
 * 
 * Examples:
 * 
 * Sample 1
 * <person>
 * 	<name>John</name>
 * 	<surname>Doe</surname>
 * </person>
 * 
 * StructData config = loadStructDataFromXML(...);
 * StructData surname = config.getByKey("surname");
 * System.out.println(surname.getString());	// output: "Doe"
 * 
 * 
 * Sample 2
 * <list>
 * 	<item>item 1</item>
 * 	<item>item 2</item>
 * </list>
 * 
 * StructData config = loadStructDataFromXML(...);
 * StructData item1 = configData.getByIndex(0);
 * System.out.println(item1.getString());	// output: "item 1"
 *
 * 
 * @author us00081
 *
 */
public class StructData {
	
	protected String name=null;
	protected Object data=null;
	
	/**
	 * Returns a StructData object representaed data loaded from XML file.
	 * 
	 * @param file - XML file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static StructData loadStructDataFromXML(File file) throws Exception {
		return loadStructDataFromXML(new FileInputStream(file));
	}
	
	/**
	 * Returns a StructData object representaed data loaded from XML file.
	 * 
	 * @param fileStream - stream for XML data
	 * @return
	 * @throws Exception 
	 */
	public static StructData loadStructDataFromXML(InputStream fileStream) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fileStream);
		
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		Element root = doc.getDocumentElement();
		//System.out.println("Root element name ["+root.getNodeName()+"].");
		
		return loadRecursively(root);

	}
	
	/**
	 * Returns the name of this StructData object (i.e. XMl tag containing data structure).
	 * 
	 * @return
	 */
	public String getname() {
		return this.name;
	}
	
	
	public boolean isNull() {
		return (this.data==null) ?true :false;
	}
	
	public boolean isString() {
		if (this.data!=null && this.data instanceof String)
			return true;
		return false;
	}
	
	public boolean isList() {
		if (this.data!=null && this.data instanceof List)
			return true;
		return false;
	}
	
	public boolean isMap() {
		if (this.data!=null && this.data instanceof Map)
			return true;
		return false;
	}
	
	/**
	 * Collection size.
	 * 
	 * @return
	 */
	public int size() {
		if (isList())
			return ((List)this.data).size();
		if (isMap())
			return ((Map)this.data).size();
		// typically a String
		return 0;
	}
	
	/**
	 * Returns string value inside string StructData.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getString() throws Exception {
		if (!isNull() && !isString())
			throw new Exception("Method not valid: this is not a String!");
		return (String)this.data;
	}
	
	
	//
	// List related methods
	//
	
	/**
	 *  Returns sub-StructData given a index position.
	 *  Only valid for List structures.
	 *  
	 * @param
	 * @return
	 * @throws Exception 
	 */
	public StructData getByIndex(int idx) throws Exception {
		if (!isList())	// this StructData is not a Map
			throw new Exception("Method not valid: this is not a List!");
		return (StructData) ((List)this.data).get(idx);
	}
	
	//
	// Map related methods
	//
	
	/**
	 * Returns sub-StructData given a key.
	 * Only valid for Map structures.
	 *   
	 * @param key - map key
	 * @return sub-StructData if present. Otherwise null.
	 * @throws Exception 
	 */
	public StructData getByKey(String key) throws Exception {
		if (!isMap()) 	// this StructData is not a Map
			throw new Exception("Method not valid: this is not a Map!");
		Object val = ((Map)this.data).get(key);
		if (val==null)
			val = new StructData();
		return (StructData)val;
	}
	
	
	/**
	 * Pretty-print for loaded data structure.
	 */
	public String toString() {
		return _toString(0, this);
	}
	private String _toString(int depth, StructData s) {
		String tab = "    ";
		StringBuffer tmp = new StringBuffer();
		for (int i=0; i<depth ;i++)
			tmp.append(tab);
		String prefix = tmp.toString();
		
		StringBuffer str = new StringBuffer();
		str.append(prefix+"<"+s.name+" ["+s.data.getClass()+"] >\n");
		if (s.data instanceof String) {
			str.append( prefix+tab+(String)s.data+"\n" );
		} else
		if (s.data instanceof List) {
			List sub = (List)s.data;
			for (int i=0; i<sub.size() ;++i) {
				StructData ss = (StructData) sub.get(i);
				str.append(ss._toString(depth+1, ss));
			}
		} else
		if (s.data instanceof Map) {
			Map sub = (Map)s.data;
			for (Iterator keys=sub.keySet().iterator(); keys.hasNext() ;) {
				String k = (String)keys.next();
				StructData ss = (StructData)sub.get(k);
				str.append(ss._toString(depth+1, ss));
			}
		}
		return str.toString();
	}
	
	/**
	 * Recursively loads XML graph represented by this node.
	 *   
	 * @param node - top DOM node
	 * @return
	 */
	private static StructData loadRecursively(Node node) {
		StructData ret = new StructData();
		ret.name = node.getNodeName();
		
		// dynamically determine return object type (List, Map, String)
		Object type = computeContainerType(node);
		//System.out.println("computed type: "+type.getClass());
		
		if ( type instanceof String) {
			if (node.hasChildNodes())
				ret.data = node.getFirstChild().getNodeValue();
			else
				ret.data = "";
			// leaf node ... no recursion needed
		} else
		if ( type instanceof List ) {
			List o = (List)type;
			NodeList nodes = node.getChildNodes();
			for (int i=0; i<nodes.getLength() ;++i) {	// loop through inner nodes
				Node n = nodes.item(i);
				if (n.getNodeType()==Node.TEXT_NODE ||		// ignore text node (used only for indentation)
					n.getNodeType()==Node.COMMENT_NODE	) 	// and comments
					continue;
				//System.out.println("<"+n.getNodeName()+">");
				
				// recursion on this node
				o.add( loadRecursively(n) );
			}
			ret.data = o;
		} else
		if ( type instanceof Map ) {
				Map o = (Map)type;
				NodeList nodes = node.getChildNodes();
				for (int i=0; i<nodes.getLength() ;++i) { // loop through inner nodes
					Node n = nodes.item(i);
					if (n.getNodeType()==Node.TEXT_NODE ||		// ignore text node (used only for indentation)
						n.getNodeType()==Node.COMMENT_NODE	) 	// and comments
							continue;
					//System.out.println("{"+n.getNodeName()+"}");
					
					// recursion on this node
					o.put(n.getNodeName(), loadRecursively(n) );
				}
				ret.data = o;
			}
		
		return ret;
	}
	
	/**
	 * Given a DOM node it determines which kind of container to use.
	 * This is the piece of code where lies the interpretation logic for XML data. 
	 * 
	 * @param node - top DOM node
	 * @return
	 */
	private static Object computeContainerType(Node node) {
		if ( node.hasChildNodes()==false )	// empty tag ...  means "" string 
			return new String();
		
		if ( node.getChildNodes().getLength()==1 && node.getFirstChild().getNodeType()==Node.TEXT_NODE )	// leaf node
			return new String();
		
		// if all nested tags have different names 
		// we consider it as a Map
		NodeList subs = node.getChildNodes();
		ArrayList uniqueNames = new ArrayList();
		boolean canBeMap = true;
		for (int i=0; i<subs.getLength() ;++i) {
			Node n = subs.item(i);
			if (n.getNodeType()==Node.TEXT_NODE ||		// ignore text node (used only for indentation)
				n.getNodeType()==Node.COMMENT_NODE	) 	// and comments
				continue;
			// now test name
			String key = n.getNodeName();
			//System.out.println(key);
			for (int j=0; j<uniqueNames.size() ;++j)
				if (uniqueNames.get(j).equals(key))
					canBeMap = false;
			uniqueNames.add(key);
		}
		if (canBeMap && uniqueNames.size()>1)
			return new HashMap();
		
		// default
		return new ArrayList();
	}
		

}
