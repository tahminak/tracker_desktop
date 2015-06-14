/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tahmina Khan
 */
public class Make {
    
    private int id=-1;
    private String makeName="";
    private List<Model> models;
    
    public Make(){
        id=-1;
        makeName="";
        models=new ArrayList<Model>();
    }
    
    
    public Make(int iId,String mName,List<Model> lModel){
        
        id=iId;
        makeName=mName;
        models=lModel;
    }
    
    
    public void setId(int iId){
        id=iId;
    }
    
    public int getId(){
        return id;
    }
    
    public void setMakeName(String nName){
        makeName=nName;
    }
    
    public String getMakeName(){
        
        return makeName;
    }
    
    public void setModels(List<Model> mModel){
        models=mModel;
    }
    
    public List<Model> getModels(){
        return models;
    }
  
    @Override
    public String toString(){
        return id+" "+makeName+models.toString();
    }
}
