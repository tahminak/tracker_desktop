/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;


/**
 *
 * @author Snigdha
 */
public class Notes implements Comparable<Notes> {
    private int id;
    private String notestitle;
    private String notes;
    
    public Notes(int idset,String title,String n){
        id=idset;
        notestitle=title;
        notes=n;
    }
    
    public void setId(int idtoset){
        
        id=idtoset;
    }
    
    public int getId(){
        return id;
    }
    
    public void setNotestitle(String title){
        notestitle=title;
        
    }
     public String getNotestitle(){
        return notestitle;
        
    }
     
     public void setNotes(String setNotes){
        notes=setNotes;
        
    }
     public String getNotes(){
        return notes;
        
    }
     
  
    public String toString(){
        
        String noteString="Id is : "+id+" Titels is : "+notestitle;
        
        return noteString;
        
    }

    @Override
    public int compareTo(Notes o) {
        
        return notestitle.compareToIgnoreCase(o.notestitle);
        
    }
}
