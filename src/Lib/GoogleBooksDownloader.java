package Lib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * @author umang
 */
public class GoogleBooksDownloader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        /*if (args.length <= 0 || args.length >= 3)
         {
         System.out.println("\nInvalid arguments provided.Exitting");
         System.exit(0);
         }*/
        Scanner s = new Scanner(System.in);
        //https://books.google.com.cu/books?id=L4xElUcbymkC&printsec=frontcover&dq=La+historia+del+Derecho+Publico+Internacional&hl=es&sa=X&redir_esc=y#v=onepage&q&f=false
        System.out.println("Book to download: ");
        StringBuffer URL = new StringBuffer(s.nextLine()); /*new StringBuffer("https://books.google.co.in/books?id=ZC6yU-EEWZwC&printsec=frontcover&dq=java&hl=en&sa=X&ei=Y1g1T7WGO8WxrAeL0726Dw&ved=0CDIQ6AEwADgK#v=onepage&q&f=false"); //*/// new StringBuffer(args[0]);
        System.out.println("Destination folder: ");
        StringBuffer locationStringBuffer = new StringBuffer(s.nextLine());/* new StringBuilder("/home/umang/test/");//*/ //new StringBuffer(args[1]);


        File location = new File(locationStringBuffer.toString());
        if (location.isDirectory() == false) {
            System.out.println("\nEntered location is not a directory or no such directory exists. Dying... \n:'-(");
            System.exit(0);
        }
        if (location.canRead() == false || location.canWrite() == false) {
            System.out.println("\nYou do not have rights to access theis place. \nPlease try running the utility with sudo/man or get admin rights.\n:'-(");
            System.exit(0);
        }
        LinkedList<StringBuffer> imageURLS = ImageUrlCreater.imageURLs(URL);
        imageURLS.stream().forEach(System.out::println);
        int i = 0;
        for (StringBuffer url : imageURLS) {
             BufferedImage bi = ImageIO.read(new URL("https:/"+url));
             ImageIO.write(bi, "jpg", new File(location + "/image" + (++i) + ".jpg"));
            //ImageDownloader.getImage(url, new StringBuffer(location.getAbsolutePath()), ++i);
        }
        System.out.println("success");
        System.out.println("---Completed writing---");
    }
}
