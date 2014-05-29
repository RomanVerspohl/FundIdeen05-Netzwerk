package phwginfo;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws Throwable {


        // which file to serve?
        JFileChooser chooser = new JFileChooser();
        File file = null;
        if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
            file = chooser.getSelectedFile();
        else
            return;
        long size = file.length();
        System.out.println("Ich habe die Datei " + file + " gewählt (Grösse " + size + ").");


        // start listening on our address and port
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Holen Sie die Datei an http://" + InetAddress.getLocalHost().getHostAddress() + ":1234/");
        Socket sock = serverSocket.accept();

        // someone has called and wishes the data

        // first listen for the query (but ignore it)
        InputStream in = sock.getInputStream();
        // listen until they give twice CR LF (13 then 10)
        int a=0, b=0, c=0, d=0;
        while(!(a==13 && b ==10 && c==13 && d==10)) {
            // shift things to the left
            a = b; b = c; c = d;
            // last one is read from the socket
            d = in.read();
            // print out what we receive
            System.out.print((char) d);
        }

        System.out.println("Fertig mit Header.");

        // time to serve the data
        OutputStream out = sock.getOutputStream();
        FileInputStream dateiInput = new FileInputStream(file);

        // write http header
        String contentType = guessContentType(file);
        out.write("HTTP/1.0 200 OK\r\n".getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
        //out.write("Connection: close".getBytes());
        out.write("\r\n".getBytes());

        // transmit data
        System.out.println("Am Verschicken.");
        System.out.println("Für ca. " + (size/100) + " Pünktchen.");
        int n=0;
        int byteRead = dateiInput.read();
        while(byteRead != -1) {
            out.write(byteRead);
            System.out.print((char) byteRead);
            byteRead = dateiInput.read();
            n++;
            if(n % 100 == 0)
                System.out.println(".");
        }
        out.flush(); out.close();
        dateiInput.close(); serverSocket.close();
        System.out.println("Fertig." );

    }

    static String guessContentType(File file) {
        String name= file.getName().toLowerCase();
        if(name.endsWith(".html")) return "text/html";
        if(name.endsWith(".png")) return "image/png";
        if(name.endsWith(".jpeg")) return "image/jpeg";
        if(name.endsWith(".jpg")) return "image/jpeg";
        if(name.endsWith(".txt")) return "text/plain";
        if(name.endsWith(".js")) return "application/javascript";
        if(name.endsWith(".css")) return "text/css";
        // not found? ah well
        return "application/octet-stream";
    }

}
