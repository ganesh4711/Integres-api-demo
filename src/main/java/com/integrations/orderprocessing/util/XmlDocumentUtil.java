package com.integrations.orderprocessing.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlDocumentUtil {
	public static String removeXmlAttributes(String xmlString, String[] attributeNames) throws Exception {

		// Parse the XML string into a Document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inputSource = new InputSource(new StringReader(xmlString));
		Document document = builder.parse(inputSource);

		// Remove the xsi:nil attribute

		for (String attribute : attributeNames) {
			removeXmlAttribute(document.getDocumentElement(), attribute);
		}

		// Serialize the modified document back to a string
		String modifiedXml = documentToString(document);

		return modifiedXml;
	}

	private static void removeXmlAttribute(Element element, String attributeName) {
		if (element.hasAttribute(attributeName)) {
			element.removeAttribute(attributeName);
		}

		// Recursively remove the attribute from child elements
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			Node childNode = element.getChildNodes().item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				removeXmlAttribute((Element) childNode, attributeName);
			}
		}
	}

	private static String documentToString(Document document) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

//    public static void main(String[] args) {
//        String xmlString = "<PHYS_INV_REF xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
//
//        try {
//            String[] attributeNames = new String[] {"xsi:nil", "xmlns:xsi"};
//            String modifiedXmlData = removeXmlAttributes( xmlString, attributeNames);
//
//            // Print the modified XML
//            System.out.println(modifiedXmlData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
