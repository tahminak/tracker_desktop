/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

import java.util.List;

/**
 *
 * @author Tahmina Khan
 */
public class Issue {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.issueName != null ? this.issueName.hashCode() : 0);
        hash = 11 * hash + (this.issues != null ? this.issues.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Issue other = (Issue) obj;
        return true;
    }
    
    private String issueName="";
    private List<Step> issues=null;
    
    
    public Issue(){
        
        
    }
     public Issue(String iName,List<Step> iSteps){
        
        issueName=iName;
    
        issues=iSteps;
    }
     
     
     public void setIssueName(String sName){
         
         issueName=sName;
     }
     
     public String getIssueName(){
         return issueName;
     }
     
 
     
     
     public void setIssueSteps(List<Step> mScripts){
         
         issues=mScripts;
     }
     
      public List<Step> getIssueSteps(){
         
        return issues;
     }
      
    @Override
      public String toString(){
          
          return issueName+" "+issues.toString();
      }
    
}
