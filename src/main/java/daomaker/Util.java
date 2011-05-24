package daomaker;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class Util {
	public static String objectNameify(String input) {
		String objectName = WordUtils.capitalizeFully(input, new char[] { '_' });
		objectName = objectName.replace(" ", "");
		return objectName.replace("_", "");
	}
	
	public static String propertyNameify(String input) {
		return StringUtils.uncapitalize(objectNameify(input));
	}
	
}
