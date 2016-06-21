package phwginfo;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Throwable {
        new Server().run();
    }

    void run() throws Throwable {
        // which file to serve?
        JFileChooser chooser = new JFileChooser();
        File file = null;
        if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
            file = chooser.getSelectedFile();
        else
            return;
        long size = file.length();
        System.out.println("Ich habe die Datei " + file + " gewählt (Grösse " + file.length() + ").");

        // display my own address
        System.out.println("Meine Adresse: " );
        System.out.println(InetAddress.getLocalHost().getHostAddress());

        // start listening on our address and port
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Waiting on port 1234: " );
        Socket sock = serverSocket.accept();

        // someone has called and wishes the data
        OutputStream out = sock.getOutputStream();
        FileInputStream input = new FileInputStream(file);
        System.out.println("Transmitting." );
        System.out.println("For about " + (size/100) + " little dots.");


        // simple variant
        int r = 0;
        while( (r=input.read())!=-1) {
            out.write(r);
        }

        // // buffered variant
        // int numRead = 0;
        // byte[] buffer = new byte[5];
        // while( (numRead = input.read(buffer))>=0) {
        //     out.write(buffer, 0, numRead);
        // }
        out.flush(); out.close();
        input.close(); serverSocket.close();
        System.out.println("Finished." );
    }


}
