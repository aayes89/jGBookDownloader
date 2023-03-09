/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebooksdownloader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Yo
 */
public class GoogleTools {

    File book;
    String bookID;
    String url;
    String dirDest;
    String jsonData;
    LinkedList<String> docParts = new LinkedList<>();
    LinkedList<String> pagIds = new LinkedList<>();
    LinkedList<String> json = new LinkedList<>();
    JsonParser jsp;

    public GoogleTools(String url, String json, String dirDest ) {
        jsp = new JsonParser();
        this.url = url;
        this.dirDest = dirDest;
        this.jsonData = json;
    }
    
    public JsonParser getParser(){
        return jsp;
    }

    /*Proceso completo de descarga*/
    public void downloadBook() {
        //preparing 
        createBookDir();
        //downloading data
        fetchData();
        //saving images of pages
        saveAllParts();
    }

    /*Crea un fichero con nombre igual al ID del libro*/
    public void createBookDir() {
        bookID = jsp.getIdFromURL(url);
        book = new File(dirDest, "/" + bookID);
        if (!book.exists()) {
            book.mkdirs();
        }
    }

    public String getBookID() {
        return bookID;
    }

    public void fetchData() {
        jsp.fetchImgUrlOnJson(jsonData);
        pagIds = jsp.getIdPagOnJson(jsonData);//url
        //json = jsp.getJsonOfEachPage(pagIds, bookID);
        docParts = jsp.getImgURLs();
    }

    public void saveAllParts() {
        int i = 0;
        for (String imgUrl : docParts) {           
            System.out.println("URL:\n\t"+imgUrl+"\n\tPagina: "+i);
            savePartAsImage(imgUrl, ++i);
            System.out.println("Salvando imagen de pagina ");
        }
    }

    public void savePartAsImage(String url, int id) {
        try {
            BufferedImage bi = ImageIO.read(new URL(url));
            ImageIO.write(bi, "jpg", new File(dirDest, "/P" + id + ".jpg"));
        } catch (IOException ex) {
            Logger.getLogger(GoogleTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
