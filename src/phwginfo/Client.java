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

        int byteRead;
        while((byteRead = input.read())!= -1) {
            out.write(byteRead);
        }
        out.flush(); out.close();
        input.close(); socket.close();
        System.out.println("Finished, please open file:" );
        System.out.println(new File("output.txt").getAbsoluteFile());
    }


}
