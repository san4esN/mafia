/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Service;

/**
 *
 * @author Константин
 * @param <T>
 */
public interface Listener<T> {
    public void getObject(T object, int id);
}
