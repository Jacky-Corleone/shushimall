package com.camelot.common;

public class LoginContextHolder {
	private static final ThreadLocal<LoginContext> CONTEXT = new ThreadLocal<LoginContext>();
	private LoginContextHolder(){
	}
	public static void put(LoginContext context){
		CONTEXT.set(context);
	}
	public static LoginContext get(){
		return CONTEXT.get();
	}
	public static void clear(){
		CONTEXT.remove();
	}
}
