/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Tahmina Khan
 */
public class Scripts {
    
    
    private List<MainMenu> menus=null;
    
    
    public Scripts(){
        
    }
    
    
    public Scripts(List<MainMenu> mMenu){
        
        menus=mMenu;
    }
    
    
    public void setMenus(List<MainMenu> mMenu){
        
        menus=mMenu;
    }
    
    
    public List<MainMenu> getMenus(){
        return menus;
    }
    
    public List<MainMenu> sortedScripts(){
        
  
       return this.getMenus();
    }
    
    @Override
    public String toString(){
        
        return menus.toString();
    }
}
