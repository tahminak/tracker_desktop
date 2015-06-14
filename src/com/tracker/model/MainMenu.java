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
public class MainMenu {
    
   
    private int id=-1;
    private String menuName="";
    private List<SubMenu> subMenus=null;
    
    public MainMenu(){
        
    }
    
    
    public MainMenu(int i,String nName, List<SubMenu> sMenu){
        
         id=i;
         menuName=nName;
         subMenus=sMenu;
    }
    
    
    
    public void setId(int i){
        
        id=i;
    }
    
    public int getId(){
        
        return id;
    }
    
    public void setMenuName(String mName){
        
        menuName=mName;
    }
    
    /**
     *
     * @return
     */
    public String getMenuName(){
        return menuName;
    }
    
    
    public void setSubMenus(List<SubMenu> sMenu){
        
        subMenus=sMenu;
    }
    
    public List<SubMenu> getSubMenus(){
        
        return subMenus;
    }
    public String toString(){
        
        return "Id is : "+id+"\n Name : "+menuName+subMenus.toString();
    }
    
}
