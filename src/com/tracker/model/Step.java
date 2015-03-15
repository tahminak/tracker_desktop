/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

/**
 *
 * @author Sinu
 */
public class Step {
     private String stepTitle="";
    private String stepText="";
    
    public Step(){
        
    }
    public Step(String title,String text){
        stepTitle=title;
        stepText=text;
        
    }
    
    
    public void setStepTitle(String title){
        stepTitle=title;
        
    }
    
    /**
     *
     * @return
     */
    public String getStepTitle(){
        
        return stepTitle;
    }
    public void setStepText(String text){
        stepText=text;
        
    }
    
    /**
     *
     * @return
     */
    public String getStepText(){
        
        return stepText;
    }
    
    
    @Override
    public String toString(){
        
        return "Title is : "+stepTitle+"\n"+"Step is : "+stepText;
    }
}
