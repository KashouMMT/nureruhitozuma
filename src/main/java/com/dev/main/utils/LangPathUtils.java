package com.dev.main.utils;

import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

public final class LangPathUtils {

	private static final Set<String> SUPPORTED_LANGS = Set.of("en", "ja","ko","zh");
    private static final String DEFAULT_LANG = "ja";

    private LangPathUtils() {}
    
    public static String resolveLangFromPath(HttpServletRequest request) {
    	String uri = request.getRequestURI();
    	String contextPath = request.getContextPath();
    	
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        String[] parts = uri.split("/");
        if(parts.length > 1) {
        	String canidate = parts[1];
        	if(SUPPORTED_LANGS.contains(canidate)) {
        		return canidate;
        	}
        }
        return DEFAULT_LANG;
    }
    
    public static String normalizeLang(String lang) {
    	if(lang == null) return DEFAULT_LANG;
    	lang = lang.toLowerCase();
    	return SUPPORTED_LANGS.contains(lang) ? lang : DEFAULT_LANG;
    }
    
    public static String buildLangUrl(HttpServletRequest request, String lang, String pathWithoutLang) {
    	String contextPath = request.getContextPath();
    	if(!pathWithoutLang.startsWith("/")) {
    		pathWithoutLang = "/" + pathWithoutLang;
    	}
    	String base = (contextPath != null ? contextPath : "");
    	return base + "/" + normalizeLang(lang) + pathWithoutLang;
    }
    
    public static String buildLangUrl(HttpServletRequest request, String pathWithoutLang) {
    	String lang = resolveLangFromPath(request);
    	String contextPath = request.getContextPath();
    	if(!pathWithoutLang.startsWith("/")) {
    		pathWithoutLang = "/" + pathWithoutLang;
    	}
    	String base = (contextPath != null ? contextPath : "");
    	return base + "/" + normalizeLang(lang) + pathWithoutLang;
    }
}
