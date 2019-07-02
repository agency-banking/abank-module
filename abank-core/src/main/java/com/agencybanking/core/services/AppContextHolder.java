package com.agencybanking.core.services;

import javax.servlet.http.HttpServletRequest;

public class AppContextHolder {
	

	private ThreadLocal<HttpServletRequest>  requestThreadLocal = new ThreadLocal<>();
	private static AppContextHolder myObj;
	
	private AppContextHolder(){}
	
	public static AppContextHolder getInstance(){
	        if(myObj == null){
	            myObj = new AppContextHolder();
	        }
	        return myObj;
	 }

    public  void setRequest(HttpServletRequest httpServletRequest) {
        requestThreadLocal.set(httpServletRequest);
    }

    public HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    public String getUserAgent() {
	    return requestThreadLocal.get().getHeader("User-Agent");
    }
}
