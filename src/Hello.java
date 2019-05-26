
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PR(Y)DA
 */
public class Hello extends UnicastRemoteObject implements HelloInterface {
    
    private String message;
    public Hello(String msg) throws RemoteException{
    
        message=msg;
    }
    
    public String say() throws RemoteException{
    
        return message;
    }
    
}