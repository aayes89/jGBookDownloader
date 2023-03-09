/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebooksdownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Yo
 */
public class GoogleBooksDownloaders {

    public static void main(String[] args) throws FileNotFoundException {
        //String url = "https://books.google.com.cu/books?id=bJrlNTwxTM8C&printsec=frontcover&dq=Tratado+de+derecho+administrativo&hl=es&sa=X&redir_esc=y#v=onepage&q=Tratado%20de%20derecho%20administrativo&f=false";
        //String url = "https://books.google.co.in/books?id=rQO37uM5XNAC&printsec=frontcover&hl=es";
        //String urlJson = "https://books.google.co.in/books?id=" + "rQO37uM5XNAC" + "&lpg=PR4&pg=PR8&jscmd=click3";
        Scanner in = new Scanner(System.in);
        System.out.println("Ingrese la URL del libro a descargar...");
        String urls = in.nextLine();
        StringBuffer sbJson=new StringBuffer();
        fetchData(urls,"PR1","PR4", sbJson);

        /*JFileChooser jfc = new JFileChooser();//new File("D:/").getPath());
        jfc.setFileFilter(new FileNameExtensionFilter("JSON", "json"));
        jfc.showOpenDialog(null);
        File json = jfc.getSelectedFile();
        //JsonParser jsp = new JsonParser();
        in = new Scanner(json);
        String cad = "";
        while (in.hasNext()) {
            cad += (in.next());
        }
        jfc.showSaveDialog(null);*/        
        GoogleTools gt = new GoogleTools(urls, sbJson.toString(), "d:/TEMP");//(urls, cad, jfc.getSelectedFile().getAbsolutePath());
        gt.downloadBook();
        JsonParser jp = gt.getParser();
        int cantP = jp.cantPaginas();
        int cantUrl = jp.cantUrlImg();
        while(cantUrl!=cantP){
            String p1="PR"+cantUrl;
            String p2="PR"+(cantUrl*2);
            System.out.println("Saltando entre "+p1+" y "+p2);
            fetchData(urls, p1, p2, sbJson);
            gt=new GoogleTools(urls, sbJson.toString(), "d:/TEMP");
            gt.downloadBook();
            cantUrl*=2;
        }
        
    }
    
    static void fetchData(String urls, String p1, String p2, StringBuffer sbJson){
        try {
            String idLibro = idLibro(urls);
            System.out.println("Obteniendo JSON de las páginas...");
            URL url = new URL("https://books.google.co.in/books?id=" + idLibro + "&lpg="+p1+"&pg="+p2+"&jscmd=click3");
            URLConnection urlConnection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while(br.ready()){
                sbJson.append(br.readLine());
            }
            System.out.println("Lectura obtenida:\n"+sbJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String idLibro(String url) {
        StringTokenizer st = new StringTokenizer(url,"&=");
        st.nextToken();
        return st.nextToken();
    }

}
