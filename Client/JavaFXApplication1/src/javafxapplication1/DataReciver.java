/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.Service.Listener;
import javafx.application.Platform;

/**
 *
 * @author Eduard
 */
public class DataReciver implements Listener<String>{

    static String con;
    Object obj1 = new Object();
    String role="0";
    int mafiaId =-1;
    
    @Override
    public void  getObject(String object, int id) {
        
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                
                    String obj = object;
                    boolean first = obj.equals("mafia");
                    boolean second = obj.equals("citizen");
                    boolean firstIf = first || second;


                    if(obj.equals("night")||obj.equals("day"))
                    {
                        con = obj;
                    }
                    else if(firstIf)
                    {
                        if(role==null&&id == userProperty.userId)
                            role = object;
                        if(obj.equals("mafia")&&id!=userProperty.userId)
                            mafiaId = id;
                    }
                
            }
        });
    }
    public String getRole()
    {
        return this.role;
    }
    public  int getMafiaId()
    {
        return this.mafiaId;
    }
    
}
