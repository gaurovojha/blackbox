package om.blackbox.ids.converter.util;

import com.blackbox.ids.core.model.CompareNumber;

public class CompareNumberUtility {

	public static boolean isValidNumber(CompareNumber compareNumber, String applicationNumber, String publicationNumber, String grantNumber){
		boolean validate = false;
		switch(compareNumber){
		case APG:
			validate = (applicationNumber.equals(publicationNumber) && applicationNumber.equals(grantNumber));
			break;
		case AP:
			validate = applicationNumber.equals(publicationNumber);
			break;
		case AG:
			validate = applicationNumber.equals(grantNumber);
			break;
		case PG:
			validate = publicationNumber.equals(grantNumber);
			break;
		case START_PA:
			validate = isValidNumberPrefix(compareNumber, applicationNumber);
			break;
		case START_BR:
			validate = isValidNumberPrefix(compareNumber, applicationNumber);
			break;
		case NONE:
			validate = true;
		}
		return validate;
		
	}
	
	private static boolean isValidNumberPrefix(CompareNumber compareNumber, String applicationNumber){
		String originalValue = compareNumber.toString();
		return applicationNumber.startsWith(originalValue.substring(originalValue.indexOf('_')+1,originalValue.length()));
	}
}
