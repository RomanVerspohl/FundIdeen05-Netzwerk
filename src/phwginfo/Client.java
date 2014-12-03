package phwginfo;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Throwable {
        // read address from the user
        String address = JOptionPane.showInputDialog("Connect to?");

        InetAddress inetAddress = InetAddress.getByName(address);
        Socket socket = new Socket(inetAddress, 1234);
        InputStream input = socket.getInputStream();
        FileOutputStream out = new FileOutputStream("output.txt");
        System.out.println("Receiving to file output.txt" );

        int numRead = 0;
        byte[] buffer = new byte[3*512];
        while((numRead = input.read(buffer))>= 0) {
            out.write(buffer, 0, numRead);
        }
        out.flush(); out.close();
        input.close(); socket.close();
        System.out.println("Finished, please open file:" );
        System.out.println(new File("output.txt").getAbsoluteFile());
    }


}
