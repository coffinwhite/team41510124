
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
  
// Server class 
public class Server  
{ 
    public static void main(String[] args) throws IOException  
    { 

          

            
            try{
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1088);
            System.out.println("Server is Connected and ready for operations");
            
        }
        
        catch(Exception e){
            
            System.out.println("Server not Connected"+e);
        }
        // server is listening on port 9090 
        ServerSocket ss = new ServerSocket(1099); 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 

                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 
                  
                // obtaining input and out streams 
                DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                Thread t = new ClientHandler(s, dis, dos); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (IOException e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    } 
} 
  
// ClientHandler class 
class ClientHandler extends Thread  
{ 
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
      
  
    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
    } 
  
    @Override
    public void run()  
    { 
        String received; 
        String toreturn; 
        while (true)  
        { 
            try { 
  
                // Ask user what he wants 
                dos.writeUTF("What do you want?[Date | Time]..\n"+ 
                            "Type Exit to terminate connection."); 
                  
                // receive the answer from client 
                received = dis.readUTF(); 
                  
                if(received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // creating Date object 
                Date date = new Date(); 
                  
                // write on output stream based on the 
                // answer from the client 
                    
             
                
        
                switch (received) { 
                  
                    case "Date" : 
                        toreturn = fordate.format(date); 
                        dos.writeUTF(toreturn);
                        Connection conn=DB.getconnection();
                        PreparedStatement stmt;
                        String sql="INSERT INTO `datax`(`dd`,`tt`) values(?,?);";
                        stmt=conn.prepareCall(sql);
                        stmt.setString(1,toreturn);
                        stmt.setString(2,"null");
                        stmt.execute();
                        
                        break; 
                          
                    case "Time" : 
                        toreturn = fortime.format(date); 
                        dos.writeUTF(toreturn); 
                        Connection connn=DB.getconnection();
                        PreparedStatement stmtt;
                        String sqql="INSERT INTO `datax`(`dd`,`tt`) values(?,?);";
                        stmt=connn.prepareCall(sqql);
                        stmt.setString(1,"null");
                        stmt.setString(2,toreturn);
                        stmt.execute();
                        break; 
                          
                    default: 
                        dos.writeUTF("Invalid input"); 
                        break; 
                } 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } 
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 
