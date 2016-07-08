package com.dsm.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {

	public static void firstFunc() {
		Pattern pattern = Pattern.compile("(?<=<tb>).*(?=<tb/>)");
		Matcher matcher = pattern.matcher("<tb>夕阳西下，断肠人在天涯<tb/>");
		if (matcher.find()) {
			System.out.println("The search result: " + matcher.group());
		}
	}
	
	
	public static void secondFunc() {
		Pattern pattern = Pattern.compile("(?=.*?[0-9])[a-zA-Z0-9]+");
		Matcher matcher = pattern.matcher("cheng1xiangchao");
		
		System.out.println("The search result2: " + matcher.matches());
	}
	
	public static void threeFunc() {
		/*Pattern pattern = Pattern.compile("{\"username\":\"[a-zA-Z]+\"/s*,/s*\"password\":\"[a-zA-Z0-9]+\"}");
		Matcher matcher = pattern.matcher("{\"username\":\"sss\",\"password\":\"dddd\"}");
		
		System.out.println("The search result2: " + matcher.matches());*/
		
		
		Pattern pattern = Pattern.compile("^\"[a-zA-Z]+\":\"[a-zA-Z0-9]+\"$");
		Matcher matcher = pattern.matcher("{\"username\":\"sss\",\"password\":\"dddd\"}");
		if (matcher.find()) {
			System.out.println("The search result: " + matcher.group());
		}
	}
	
}
