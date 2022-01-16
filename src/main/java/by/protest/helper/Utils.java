package by.protest.helper;

import java.util.HashSet;
import java.util.Set;

public class Utils {
	
	private static final Set<Character> ALLOWED_CHARS;
	static {
		//damned 1.8 ((
		ALLOWED_CHARS = new HashSet<>();
		ALLOWED_CHARS.add('A');
		ALLOWED_CHARS.add('B');
		ALLOWED_CHARS.add('C');
		ALLOWED_CHARS.add('E');
		ALLOWED_CHARS.add('H');
		ALLOWED_CHARS.add('I');
		ALLOWED_CHARS.add('K');
		ALLOWED_CHARS.add('M');
		ALLOWED_CHARS.add('O');
		ALLOWED_CHARS.add('P');
		ALLOWED_CHARS.add('T');
		ALLOWED_CHARS.add('X');
		ALLOWED_CHARS.add('0');
		ALLOWED_CHARS.add('9');
		ALLOWED_CHARS.add('8');
		ALLOWED_CHARS.add('7');
		ALLOWED_CHARS.add('6');
		ALLOWED_CHARS.add('5');
		ALLOWED_CHARS.add('4');
		ALLOWED_CHARS.add('3');
		ALLOWED_CHARS.add('2');
		ALLOWED_CHARS.add('1');
		ALLOWED_CHARS.add('0');
	}
	
	private static final Set<String> POSSIBLE_MATCHERS;
	static {
		POSSIBLE_MATCHERS = new HashSet<>();
		POSSIBLE_MATCHERS.add("^[0-9]{4,5}$");
		POSSIBLE_MATCHERS.add("[0-9]{1,4}[ABCEHIKMOPTX]{0,3}");
		POSSIBLE_MATCHERS.add("[0-9]{1,4}[ABCEHIKMOPTX]{2}[0-7]{1}$");
		POSSIBLE_MATCHERS.add("[0-9]{1,4}[ABCEHIKMOPTX]{2,3}$");
		POSSIBLE_MATCHERS.add("^[ABCEHIKMOPTX]{1,2}[0-9]{1,5}$");
	}

	public static final String REG_AA_0000_PATTERN = "^[ABCEHIKMOPTX]{2}[0-9]{4}$";
	public static final String REG_0000_AA_PATTERN  = "^[0-9]{4}[ABCEHIKMOPTX]{2}$";
	public static final String REG_0000_AAA_PATTERN  = "^[0-9]{4}[ABCEHIKMOPTX]{3}$";
	public static final String REG_AA_0000xR_PATTERN  = "^[ABCEHIKMOPTX]{2}[0-9]{4}[0-7]{1}$";
	public static final String REG_0000_AAxR_PATTERN  = "^[0-9]{4}[ABCEHIKMOPTX]{2}[0-7]{1}$";	

	public static String filter(String registration) {
		char[]chars = registration.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(char ch:chars) {
			if(ALLOWED_CHARS.contains(ch)) {
				sb.append(ch);
			}else sb.append(Utils.replace(ch));
		}
		return sb.toString();
	}
	
	public static String convertFromDbRepresentation(String registration) {
		if (registration.matches(REG_0000_AAA_PATTERN))
			return convertOldAutoRegistration3Letters(registration);
		if (registration.matches(REG_0000_AA_PATTERN))
			return convertOldAutoRegistration2Letters(registration);
		if (registration.matches(REG_0000_AAxR_PATTERN))
			return convertToPrivateAutoNumberWithRegion(registration);
		if (registration.matches(REG_AA_0000_PATTERN))
			return convertMunitipalAutoRegistration2Letters(registration);
		if (registration.matches(REG_AA_0000xR_PATTERN))
			return convertToAutoNumberWithRegion(registration);
		return registration;
	}
    
    /**
     * converts db-stored string to registration mark kind <<AA 00000>>
     * @param registration DB-stored string
     * @return registration mark
     */
    public static String convertMunitipalAutoRegistration2Letters(String registration) {
    	return registration.substring(0, 2).concat(" ").concat(registration.substring(2,6));
    }
    
    /**
     * converts db-stored string to registration mark kind <<0000 AA>>
     * @param registration DB-stored string
     * @return registration mark
     */
    public static String convertOldAutoRegistration2Letters(String registration) {
    	return registration.substring(0, 4).concat(" ").concat(registration.substring(4,6));
    }
    
    /**
     * converts db-stored string to registration mark kind <<0000 AAA>>
     * @param registration DB-stored string
     * @return registration mark
     */
    public static String convertOldAutoRegistration3Letters(String registration) {
    	return registration.substring(0, 4).concat(" ").concat(registration.substring(4,7));
    }
    
    /**
     * converts db-stored string to registration mark kind <<0000 AA-R>>
     * @param registration DB-stored string
     * @return registration mark
     */
	public static String convertToPrivateAutoNumberWithRegion(String registration) {
		return registration.substring(0,4).concat(" ").concat(registration.substring(4,6).concat("-").concat(registration.substring(6,7)));
	}
	
    /**
     * converts db-stored string to registration mark kind <<AA 0000-R>>
     * @param registration DB-stored string
     * @return registration mark
     */
	public static String convertToAutoNumberWithRegion(String registration) {
		return registration.substring(0,2).concat(" ").concat(registration.substring(2,6).concat("-").concat(registration.substring(6,7)));
	}
	
	public static boolean matchesPossiblePattern(String line) {
		for (String pattern : POSSIBLE_MATCHERS) {
			if (line.matches(pattern))
				return true;
		}
		return false;
	}

	public static String replace(char ch) {
		switch(ch){
		case 'А':
			return "A";
		case 'В':
			return "B";
		case 'С':
			return "C";
		case 'Е':
			return "E";
		case 'Н':
			return "H";
		case 'І':
			return "I";
		case 'К':
			return "K";
		case 'М':
			return "M";
		case 'О':
			return "O";
		case 'Р':
			return "P";
		case 'Т':
			return "T";
		case 'Х':
			return "X";
		default: return "";	
		}
}}
