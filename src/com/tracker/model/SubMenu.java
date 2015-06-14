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
public class Submenu implements Comparable<Submenu>{
    
    private String subMenuName="";
    private String subMenuTitle="";
    private List<Script> subMenuScripts=null;
    
    
    public Submenu(){
        
        
    }
     public Submenu(String sMenu,String sTitle,List<Script> sMenuscript){
        
        subMenuName=sMenu;
        subMenuTitle=sTitle;
        subMenuScripts=sMenuscript;
    }
     
     
     public void setSubMenuName(String sName){
         
         subMenuName=sName;
     }
     
     public String getSubMenuName(){
         return subMenuName;
     }
     
     public void setSubMenuTitle(String sTitle){
         
         subMenuTitle=sTitle;
     }
     
     public String getsSubMenuTitle(){
         
         return subMenuTitle;
     }
     
     
     public void setSubMenuScripts(List<Script> mScripts){
         
         subMenuScripts=mScripts;
     }
     
      public List<Script> getSubMenuScripts(){
         
        return subMenuScripts;
     }
      
    @Override
      public String toString(){
          
          return subMenuName+" "+subMenuTitle+subMenuScripts.toString();
      }

    @Override
    public int compareTo(Submenu o) {
        return this.getsSubMenuTitle().compareToIgnoreCase(o.getsSubMenuTitle());
    }
}
