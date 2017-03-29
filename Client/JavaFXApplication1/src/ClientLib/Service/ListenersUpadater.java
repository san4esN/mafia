/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Service;

import java.util.ArrayList;

/**
 *
 * @author Константин
 */
public class ListenersUpadater<K> implements Updater<K>{
   private ArrayList<Listener<K>> listeners = new ArrayList<>();
   public ListenersUpadater(){}
   public void add(Listener<K> listener)
   {
       listeners.add(listener);
   }
   
   public void delete(Listener<K> listener)
   {
       listeners.remove(listener);
   }
   
   public void update(K object,int id)
   {
       for(Listener<K> listener : listeners)
       {
           listener.getObject(object,id);
       }
   }
}
