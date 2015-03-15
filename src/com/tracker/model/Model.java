/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sinu
 */
public class Model {
    
    private String modelName="";
    private List<Issue> issues;
    
    public Model(){
        modelName="";
        issues=new ArrayList<Issue>(); 
    }
    
    public Model(String mName,List<Issue> iIssue){
        
        modelName=mName;
        issues=iIssue;
    }
    
    
    public void setModelName(String sName){
        modelName=sName;
    }
    
    public String getModelName(){
        return modelName;
    }
    
    
    public void setIssues(List<Issue> iIssues){
        issues=iIssues;
    }
    
    /**
     *
     * @return
     */
    public List<Issue> getIssues(){
        
        return issues;
    }
    
    
      public String toString(){
          
          return modelName+" "+issues.toString()+"\n";
      }
}
