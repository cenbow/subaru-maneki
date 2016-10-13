package com.subaru.maneki.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ThemeManager {
	
	List<String[]> getThemeProductId(HttpServletRequest request, String date);
}
