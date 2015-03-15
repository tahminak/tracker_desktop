/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

/**
 *
 * @author Tahmina Khan
 */
public class Script implements Comparable<Script>{
    
    private String scriptTitle="";
    private String scriptText="";
    
    public Script(){
        
    }
    public Script(String title,String text){
        scriptTitle=title;
        scriptText=text;
        
    }
    
    
    public void setScriptTitle(String title){
        scriptTitle=title;
        
    }
    
    public String getScriptTitle(){
        
        return scriptTitle;
    }
    public void setScriptText(String text){
        scriptText=text;
        
    }
    
    /**
     *
     * @returncompareToIgnoreCase
     */
    public String getScriptText(){
        
        return scriptText;
    }
    
    
    @Override
    public String toString(){
        
        return "Title is : "+scriptTitle+"\n"+"Script is : "+scriptText;
    }

    @Override
    public int compareTo(Script o) {
        
        return this.getScriptTitle().compareToIgnoreCase(o.getScriptTitle());
    }
}
