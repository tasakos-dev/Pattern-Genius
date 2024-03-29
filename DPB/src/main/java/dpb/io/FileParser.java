package dpb.io;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dpb.exceptions.NoPropertiesException;
import dpb.model.Property;



public class FileParser implements IFileParser {
	private Document patternsDoc;
	
	
	
	public FileParser() throws ParserConfigurationException, SAXException,
						IOException, URISyntaxException {
		File patternsFIle = null;
		Bundle bundle = Platform.getBundle("DPB");
		URL filUrl = bundle.getEntry("src/main/resources/patterns.xml");
		patternsFIle = new File(FileLocator.resolve(filUrl).toURI());

		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	
		documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		patternsDoc = documentBuilder.parse(patternsFIle);
		patternsDoc.getDocumentElement().normalize();
	
	}
	
	@Override
	public Property[] getProperties(String pattern) throws NoPropertiesException {
		Element properties = (Element) getElementByTagAndId("pattern", pattern).getElementsByTagName("properties").item(0);
		if (properties == null) throw new NoPropertiesException();
		
		NodeList classPropertiesList = (NodeList) properties.getElementsByTagName("newClass");
		int length = classPropertiesList.getLength();
		String implementsString;
		String annotation;
		Property[] propertiesArray = new Property[length];
		
		for (int i = 0; i < length;i++) {
			implementsString = ((Element)classPropertiesList.item(i)).getAttribute("implements");	
			annotation = ((Element)classPropertiesList.item(i)).getAttribute("annotation")+"Annotation";	
			propertiesArray[i] = new Property(annotation, implementsString);
		}
		return propertiesArray;
		
	}
	


	@Override
	public String[] getPatternCategories() {
		NodeList categoriesNodeList = (NodeList) patternsDoc.getElementsByTagName("category");
		int length = categoriesNodeList.getLength();
		String[] categories = new String[length];
		String categoryString;
		
		for (int i = 0;i < length;i++) {
			Element element = (Element) categoriesNodeList.item(i);
			categoryString = element.getAttribute("id").strip();
			categories[i] = categoryString;
			
		}
		
		return categories;
	}

	@Override
	public String[] getPatternsOfCategory(String category) {
		NodeList patternList = (NodeList) getElementByTagAndId("category", category).getElementsByTagName("pattern");
		int length = patternList.getLength();
		String[] patterns = new String[length];
		String patternString;
		
		for (int i = 0;i < length;i++) {
			Element element = (Element) patternList.item(i);
			patternString = element.getAttribute("id").strip();
			patterns[i] = patternString;
			
		}
		
		return patterns;
	}
	
	private Element getElementByTagAndId(String tag, String id) {
		Element retElement = null;
		NodeList categoryElements = patternsDoc.getElementsByTagName(tag);
		int length = categoryElements.getLength();
		
		for (int i = 0;i < length; i++) {
			Element element = (Element) categoryElements.item(i);
			if (element.hasAttribute("id") && element.getAttribute("id").equals(id)) {
				retElement = element;
				break;
			}
			
		}
		
		return retElement ; 
		
	}

	@Override
	public String[] getClasses(String pattern) {
		
		NodeList classList = (NodeList) getElementByTagAndId("pattern", pattern).getElementsByTagName("class");
		int length = classList.getLength();
		String[] classes = new String[length];
		String classString;
		
		for (int i = 0;i < length;i++) {
			Element element = (Element) classList.item(i);
			classString = element.getAttribute("id").strip();
			classes[i] = classString;
		}

		return classes;
	}

	// TODO change return type!
	@Override
	public String[][] getClassMethods(String className, String pattern) {
			
		Element element = getPatternElement(className, pattern, "class");
		if (element == null) return new String[][] {};
		
		NodeList methods = (NodeList) element.getElementsByTagName("method");
		int length = methods.getLength();
		String[][] classMethods = new String[length][3];
		
		for (int j = 0; j<length;j++) {
			Element method =(Element) methods.item(j);
			String visibility = method.getAttribute("visibility").strip();
			if (visibility.isBlank()) visibility = "public";
			String name = method.getAttribute("id").strip();
			String type = method.getElementsByTagName("type").item(0).getTextContent().strip();
			classMethods[j] = new String[] {visibility, type, name};
		}
		
		return classMethods;
	}
	
	private Element getPatternElement(String name, String pattern, String type) {
		NodeList classesList = (NodeList) getElementByTagAndId("pattern", pattern).getElementsByTagName(type);	
		for (int i = 0; i < classesList.getLength(); i++) {
			Element element = (Element) classesList.item(i);
			if (element.getAttribute("id").equals(name)) {
				return element;
			}
		}
		return null;	
	}

	// TODO change return type!
	@Override
	public String[][] getInterfaceMethods(String interfaceName, String pattern) {
		Element element = getPatternElement(interfaceName, pattern, "interface");
		if (element == null) return new String[][] {};
		NodeList methods = element.getElementsByTagName("method");
		int length = methods.getLength();
		String[][] interfaceMethods =  new String[length][2];
		for (int j = 0;j<length;j++) {
			Element method = (Element) methods.item(j);
			String name = method.getAttribute("id").strip();
			String type = method.getElementsByTagName("type").item(0).getTextContent().strip();
			interfaceMethods[j] = new String[] {type, name};					
		}
		
		return interfaceMethods;
	}
	
	@Override
	public String getImplementedInterface(String className, String pattern) {
		NodeList patternClasses = getElementByTagAndId("pattern", pattern).getElementsByTagName("class");
		int length = patternClasses.getLength();

		for (int i = 0; i < length; i++) {
			Element element = (Element) patternClasses.item(i);
			if (element.getAttribute("id").equals(className))
				return element.getAttribute("implements").strip();
		}
		
		return "";
		
	}
	
	@Override
	public String getPatternDescription(String pattern) {
		NodeList desrciptioNodeList = (NodeList) getElementByTagAndId("pattern", pattern).getElementsByTagName("description");
		return desrciptioNodeList.item(0).getTextContent().trim().replaceAll("\\s+", " ");
		
	}

	@Override
	public boolean isAbstractClass(String className, String pattern) {
		Element element = getPatternElement(className, pattern, "class");
		if (element != null) {
			String isAbstract = element.getAttribute("isAbstract");
			return isAbstract.equals("true");
		}
		
		return false;
	}
	
	@Override
	public String getExtendedClass(String className, String pattern) {
		Element element = getPatternElement(className, pattern, "class");
		if (element != null) 
			return element.getAttribute("extends").strip();
		return "";
	}

	
	// TODO change return type!
	@Override
	public String[][] getClassFields(String className, String pattern) {
		Element element = getPatternElement(className, pattern, "class");
		if (element == null) return new String[][] {};
		
		NodeList fields = element.getElementsByTagName("field");
		int length = fields.getLength();
		String[][] classFields = new String[length][2];

		for (int i = 0; i < length; i++) {
			Element field = (Element) fields.item(i);
			String name = field.getAttribute("id").strip();
			String type = field.getElementsByTagName("type").item(0).getTextContent().strip();
			classFields[i] = new String[] {type, name};
		}
		
		return classFields;
	}
		
	@Override
	public String getMethodCode(String methodName, String classname, String pattern) {
		Element method = getClassElement(methodName, classname, pattern, "method");
		if (method == null) return null;  
		if (method.getElementsByTagName("code").item(0)!=null)
			return method.getElementsByTagName("code").item(0).getTextContent().strip();
		return null;
		
	}
	
	private Element getClassElement(String name, String classname, String pattern, String type) {
		Element classElement = getPatternElement(classname, pattern, "class");
		if (classElement == null) return null;
		
		NodeList methods = classElement.getElementsByTagName(type);
		
		for (int i = 0;i<methods.getLength();i++) {
			Element method =(Element) methods.item(i);
			if (method.getAttribute("id").equals(name))
				return method;
		}
		return null;
		
	}
	
	private Element getMethodElement(String methodName, String patternElementName, String pattern) {
		Element method = getClassElement(methodName, patternElementName, pattern, "method");
		if (method == null) {
			Element interfaceElement = getPatternElement(patternElementName, pattern, "interface");
			NodeList methods = interfaceElement.getElementsByTagName("method");
			
			for (int i = 0;i<methods.getLength();i++) {
				method =(Element) methods.item(i);
				if (method.getAttribute("id").equals(methodName))
					break;
			}
		}
		return method;
	}
	
	
	@Override
	public boolean isAbstractMethod(String methodName, String classname, String pattern) {
		Element method = getClassElement(methodName, classname, pattern, "method");
		if (method == null) return false;
		if (method.getAttribute("isAbstract") != null)
			return method.getAttribute("isAbstract").equals("true");
		return false;
	}
	
	@Override
	public boolean isStaticField(String fieldName, String classname, String pattern) {
		Element field = getClassElement(fieldName, classname, pattern, "field");
		if (field == null) return false;
		if (field.getAttribute("isStatic") != null)
			return field.getAttribute("isStatic").equals("true");
		return false;
	}
	
	@Override
	public boolean isStaticMethod(String methodName, String classname, String pattern) {
		Element method = getClassElement(methodName, classname, pattern, "method");
		if (method == null) return false;
		if (method.getAttribute("isStatic") != null)
			return method.getAttribute("isStatic").equals("true");
		return false;
	}

	
	// TODO change return type!
	@Override
	public String[][] getMethodParameters(String methodName, String classname, String pattern) {
		Element method = getMethodElement(methodName, classname, pattern);
		
		if (method == null)	return new String[][] {};
		NodeList parametersList = (NodeList) method.getElementsByTagName("parameter");
		int length = parametersList.getLength();
		String[][] parameters = new String[length][2];
		for (int i = 0;i<length;i++) {
			Element parameter = (Element) parametersList.item(i);
			String name = parameter.getAttribute("id").strip();
			String type = parameter.getElementsByTagName("type").item(0).getTextContent().strip();
			parameters[i][0] = type;
			parameters[i][1] = name;
		}
		
		return parameters;
		
	}
	
	@Override
	public String getAnnotation(String name, String category, String pattern) {
		Element patternElement = getPatternElement(name, pattern, "class");
		if (patternElement == null) patternElement = getPatternElement(name, pattern, "interface"); 
		
		return patternElement.getAttribute("annotation")+"Annotation";
	}

	

}
