/**
 *
 */
package com.blackbox.ids.core.util.file;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * @author ajay2258
 *
 */
public class JaxbXmlUtil {

	public static <T extends Object> File toXML(final T object, final String xmlFilePath) {
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			// for pretty-print XML in JAXB
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Write to File
			marshaller.marshal(object, new File(xmlFilePath));
			System.out.println("Data xml generated successfully.");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T extends Object> T toObject(final String xmlFilePath) {
		return null;
	}

	/*-private static final String FILE_NAME = "jaxb-emp.xml";

	public static void main(String[] args) {
		Employee emp = new Employee();
		emp.setId(1);
		emp.setAge(25);
		emp.setName("Pankaj");
		emp.setGender("Male");
		emp.setRole("Developer");
		emp.setPassword("sensitive");

		jaxbObjectToXML(emp);

		Employee empFromFile = jaxbXMLToObject();
		System.out.println(empFromFile.toString());
	}

	private static Employee jaxbXMLToObject() {
		try {
			JAXBContext context = JAXBContext.newInstance(Employee.class);
			Unmarshaller un = context.createUnmarshaller();
			Employee emp = (Employee) un.unmarshal(new File(FILE_NAME));
			return emp;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}*/
}

