package Clientes;

import java.io.IOException;
import java.net.Socket;

import Connections.Frame;
import Connections.TaggedConnection;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socketClient=null;
        try {
            socketClient= new Socket("localhost", 12345);
        } catch (IOException e) { e.printStackTrace();        }
        
        TaggedConnection tag = new TaggedConnection(socketClient);

        Frame frame1 = new Frame(0,"hi".getBytes());
        tag.send(frame1);

        Frame frame2 = tag.receive();
        
        System.out.println( frame2.toString());
        
        tag.close();
    }
}
