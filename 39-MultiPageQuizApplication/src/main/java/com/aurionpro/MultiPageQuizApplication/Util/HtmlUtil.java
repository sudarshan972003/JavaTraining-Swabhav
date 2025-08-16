package com.aurionpro.MultiPageQuizApplication.Util;

public class HtmlUtil {
	public static String esc(String s) {
		if (s == null)
			return "";
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
