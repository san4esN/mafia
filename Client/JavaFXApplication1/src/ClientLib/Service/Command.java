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
public class Command {
    private int id;
    private String command;
    public Command(int id, String command)
    {
        this.id = id;
        this.command =  command;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

}
