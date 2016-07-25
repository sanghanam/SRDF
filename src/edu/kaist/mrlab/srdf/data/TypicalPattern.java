package edu.kaist.mrlab.srdf.data;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypicalPattern {
	public boolean isMatchedType1(String input){
		boolean result = false;
		
		Pattern p = Pattern.compile("nv+");
		Matcher m = p.matcher(input);
		result = m.matches();
		
		return result;
	}
	
	public boolean isMatchedType2(String input){
		boolean result = false;
		
		Pattern p = Pattern.compile("n+v");
		Matcher m = p.matcher(input);
		result = m.matches();
		
		return result;
	}
	
	public boolean isMatchedType3(String input){
		boolean result = false;
		
		Pattern p = Pattern.compile("n+v+");
		Matcher m = p.matcher(input);
		result = m.matches();		
		
		return result;
	}
	
	public boolean isMatchedType4(String input){
		boolean result = false;
		
		Pattern p = Pattern.compile("n+v+n+v+(n+v+)*");
		Matcher m = p.matcher(input);
		result = m.matches();
				
		return result;
	}
}
