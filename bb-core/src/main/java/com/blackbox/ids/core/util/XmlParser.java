package com.blackbox.ids.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * @author rajatjain01
 *
 */
public class XmlParser{

	
	/**
	 * Conerts xml into java object
	 * @param file TODO
	 * @param clazz TODO
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static Object fetchXmlData(File file, Class clazz) throws FileNotFoundException {
		// XML and Java binding
		final XStream xStream = new XStream(new StaxDriver());
		xStream.autodetectAnnotations(true);
        xStream.processAnnotations(clazz);
        return xStream.fromXML(new FileReader(file));
	}
	/**
	 * Validates xml against given schema
	 * 
	 * @param inputFile
	 * @param schemaFile
	 * @return
	 */
	public static boolean validateXml(final String inputFile, final String schemaFile) {
		try {
            final SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final Schema schema = factory.newSchema(new File(schemaFile));
            final Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(inputFile)));
        } catch (IOException | SAXException e) {
            //logger.info("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
	
}
