/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sinu
 */
public class Scripts {
    
    
    private List<Mainmenu> menus=null;
    
    
    public Scripts(){
        
    }
    
    
    public Scripts(List<Mainmenu> mMenu){
        
        menus=mMenu;
    }
    
    
    public void setMenus(List<Mainmenu> mMenu){
        
        menus=mMenu;
    }
    
    
    public List<Mainmenu> getMenus(){
        return menus;
    }
    
    public List<Mainmenu> sortedScripts(){
        
     //  Collections.sort(menus);
       return this.getMenus();
    }
    
    @Override
    public String toString(){
        
        return menus.toString();
    }
}
