/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientLib.Service;

/**
 *
 * @author Константин
 */
public interface Updater<K> {
    public void add(Listener<K> listener);

    public void delete(Listener<K> listener);

    public void update(K object, int id);

}
