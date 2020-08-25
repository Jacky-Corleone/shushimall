package com.camelot.example.controller;

public enum MsgType {
	Text("text"),  
    Image("image"),  
    Music("music"),  
    Video("video"),  
    Voice("voice"),  
    Location("location"),
    Shortvideo("shortvideo"),
    Link("link"),
    event("event"),
    unsubscribe("unsubscribe"),
    subscribe("subscribe");
    private String msgType = "";  
  
    MsgType(String msgType) {  
        this.msgType = msgType;  
    }  
  
    /** 
     * @return the msgType 
     */  
    @Override  
    public String toString() {  
        return msgType;  
    }
}
