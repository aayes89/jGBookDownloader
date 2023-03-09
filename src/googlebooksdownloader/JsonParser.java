/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebooksdownloader;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Yo
 */
public class JsonParser {

    LinkedList<String> imgURLs = new LinkedList<>();
    private int cantPaginas;
    private int cantUrlImg;

    public JsonParser() {
        cantPaginas = 0;
        cantUrlImg = 0;
    }

    public int cantPaginas() {
        return cantPaginas;
    }

    public int cantUrlImg() {
        return cantUrlImg;
    }

    /*Obtiene la URL por cada ID de pagina encontrada*/
    public LinkedList<String> getJsonOfEachPage(LinkedList<String> li, String bookId) {
        //StringBuffer url = "books.google.co.in/books?id=" + bookId + "&lpg=PP1&pg=PR5&jscmd=click3";
        LinkedList<String> returnVal = new LinkedList<String>();
        try {
            for (String pageid : li) {
                System.out.println("Getting JSON for page id: " + pageid);
                String sURL = ("https://books.google.co.in/books?id=" + bookId + "&lpg=PR1&pg=" + pageid + "&jscmd=click3");
                URL url = new URL(sURL);
                URLConnection urlConnection = url.openConnection();

                urlConnection.setRequestProperty("Accept", "text/html");
                // do not remove this comment as it will enable the zipped response
                //urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                urlConnection.setRequestProperty("Accept-Language", "en-us,en;");
                urlConnection.setRequestProperty("Connection", "keep-alive");
                //urlConnection.setRequestProperty("Cookie", "PREF=ID=87cacd9eca7c3d3d:U=61453bc60ed21321:FF=0:TM=1316797527:LM=1316797529:S=DwCvNv2dwUC0PM-H; NID=51=LnjMsuN6oE0PSNXGGXuDA_TYtlP5rtV3JxyyfSxfZO2NlL3TMbDXG-mLUuGVw4-r0nnSG9XWK8-7pUJXM5daKkR0eUVP_HbaQM_qGnvvBwi21E6adRBIMDfA0jLyTCie; SID=DQAAAL0AAACahCJZfqJn-UUMhVINiKwAnVbikwsMpxv2coUq-umg1ajL7k9lO7rsImbAc5Cx712V03dMxEvLgBHpED9IEpjf7SZAI751rqX0CYtACZgfB93StxUacUiJXrrjMBrWCuZPpjeMOwcAis39iZI_T-XGfBSPExpp9YrZrItV_DphpWvATBuQmIjUAxZnXw9tEDjnnGLIS12YNu_qVyXPowrnWk-6qWsHAYXcZTIdFjqMNwR0cgF7uYIFIMSltvg2J28; HSID=A__R2WjxJOuu-PrE1; __utma=27880111.1388352251.1328869790.1328890429.1328895849.6; __utmz=27880111.1328885708.4.3.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=java; __utmc=27880111; PP_TOS_ACK=2; __utmb=27880111.1.10.1328895849");
                //urlConnection.setRequestProperty("Host", "books.google.co.in");
                //urlConnection.setRequestProperty("Referer", "https://books.google.co.in/books?id=" + bookId + "&printsec=frontcover&source=gbs_ge_summary_r&cad=0");
                //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0");

                Scanner s = new Scanner(new InputStreamReader(urlConnection.getInputStream()));
                String res = s.next();
                fetchImgUrlOnJson(res);
                returnVal.add(res);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return returnVal;
    }

    /*Devolver las URL de cada pag*/
    public LinkedList<String> getImgURLs() {
        return imgURLs;
    }

    /*Obtener el id del libro a partir de la URL*/
    public String getIdFromURL(String URL) {
        StringTokenizer tok = new StringTokenizer(URL, "&=");
        tok.nextToken();
        return (tok.nextToken());
    }

    /*Obtener los ID de las paginas en el JSON*/
    public LinkedList<String> getIdPagOnJson(String jsonContent) {
        LinkedList<String> pagID = new LinkedList<>();
        StringTokenizer st1 = new StringTokenizer(jsonContent, "[]{}:,\"");
        while (st1.hasMoreTokens()) {
            String tokens = st1.nextToken();
            if (tokens.startsWith("P")) {
                pagID.add(tokens);
            }
            //System.out.println(tokens);
        }
        System.out.println("Se encontraron " + pagID.size() + " páginas");
        cantPaginas = pagID.size();
        return pagID;
    }

    /*Obtener url de las paginas en el JSON*/
    public void fetchImgUrlOnJson(String jsonContent) {
        StringTokenizer st = new StringTokenizer(jsonContent, "[{\"}]");
        while (st.hasMoreTokens()) {
            String tokens = st.nextToken();
            if (tokens.contains("http")) {
                tokens = tokens.replace("\\u0026", "&");
                if (tokens.contains("img")) {
                    imgURLs.add(tokens);
                    //System.out.println("Encontro una URL\n\t" + tokens);
                }
            }
        }
        System.out.println("Encontraron " + imgURLs.size() + " URL de imagenes");
        cantUrlImg = imgURLs.size();
        /*StringTokenizer tok = new StringTokenizer(response, "[]");
         StringBuffer s1 = new StringBuffer(tok.nextToken());
         s1 = new StringBuffer(tok.nextToken());
         StringTokenizer tok1 = new StringTokenizer(s1.toString(), "{}");
         //StringBuffer s2 = tok1.nextToken();
         while (tok1.hasMoreElements()) {
         StringBuilder s3 = new StringBuilder(tok1.nextToken());
         if (s3.toString().equalsIgnoreCase(",") == false) {
         StringTokenizer tok2 = new StringTokenizer(s3.toString(), ",");
         while (tok2.hasMoreElements()) {
         StringBuilder s4 = new StringBuilder(tok2.nextToken());
         StringBuilder sub = new StringBuilder(s4.toString().substring(1, 4));
         if (sub.toString().equalsIgnoreCase("src")) {
         StringBuffer s = new StringBuffer(s4.toString().substring(7, s4.length()));
         s = new StringBuffer(s.toString().replace("\\u0026", "&"));
         s = new StringBuffer(s.toString().substring(0, s.length() - 1));
         //System.out.println("Extracted IMAGE URL " + s);
         imgURLs.add(s.toString());
         return;
         }
         }
         }
         }*/
    }

    /*private LinkedList<StringBuffer> getPids(StringBuffer source) {
     LinkedList<StringBuffer> pidList = new LinkedList<StringBuffer>();
     StringTokenizer tok = new StringTokenizer(source.toString(), "[]");
     StringBuffer level1 = new StringBuffer(tok.nextToken());
     level1 = new StringBuffer(tok.nextToken());
     StringTokenizer tok1 = new StringTokenizer(level1.toString(), "{}");
     while (tok1.hasMoreTokens()) {
     StringBuilder token = new StringBuilder(tok1.nextToken());

     // System.out.println(token);
     if (token.toString().equalsIgnoreCase(",") == false) {
     StringTokenizer quoteTokenizer = new StringTokenizer(token.toString(), ":,");
     quoteTokenizer.nextToken();
     StringBuffer pid1 = new StringBuffer(quoteTokenizer.nextToken());

     pid1 = new StringBuffer(pid1.substring(1, pid1.length() - 1));
     pidList.add(pid1);
     }

     }
     return pidList;
     }*/
    /*Obtener cantidad de paginas del libro*/
    /*public String getJsonByBookID(String bookId) {

     StringBuffer sURL = new StringBuffer("https://books.google.co.in/books?id=" + bookId + "&lpg=PR1&pg=PR1&jscmd=click3");
     StringBuffer data = new StringBuffer("");
     try {
     URL url = new URL(sURL.toString());
     URLConnection urlConnection = url.openConnection();
     System.out.println("Getting url" + sURL);
     urlConnection.setRequestProperty("Accept", "text/html");
     // do not remove this comment as it will enable the zipped response
     //urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
     urlConnection.setRequestProperty("Accept-Language", "en-us,en;");
     urlConnection.setRequestProperty("Connection", "keep-alive");
     //urlConnection.setRequestProperty("Cookie", "PREF=ID=87cacd9eca7c3d3d:U=61453bc60ed21321:FF=0:TM=1316797527:LM=1316797529:S=DwCvNv2dwUC0PM-H; NID=51=LnjMsuN6oE0PSNXGGXuDA_TYtlP5rtV3JxyyfSxfZO2NlL3TMbDXG-mLUuGVw4-r0nnSG9XWK8-7pUJXM5daKkR0eUVP_HbaQM_qGnvvBwi21E6adRBIMDfA0jLyTCie; SID=DQAAAL0AAACahCJZfqJn-UUMhVINiKwAnVbikwsMpxv2coUq-umg1ajL7k9lO7rsImbAc5Cx712V03dMxEvLgBHpED9IEpjf7SZAI751rqX0CYtACZgfB93StxUacUiJXrrjMBrWCuZPpjeMOwcAis39iZI_T-XGfBSPExpp9YrZrItV_DphpWvATBuQmIjUAxZnXw9tEDjnnGLIS12YNu_qVyXPowrnWk-6qWsHAYXcZTIdFjqMNwR0cgF7uYIFIMSltvg2J28; HSID=A__R2WjxJOuu-PrE1; __utma=27880111.1388352251.1328869790.1328890429.1328895849.6; __utmz=27880111.1328885708.4.3.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=java; __utmc=27880111; PP_TOS_ACK=2; __utmb=27880111.1.10.1328895849");
     //urlConnection.setRequestProperty("Host", "books.google.co.in");
     //urlConnection.setRequestProperty("Referer", "https://books.google.co.in/books?id=" + bookId + "&printsec=frontcover&source=gbs_ge_summary_r&cad=0");
     //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.36 Safari/535.7");

     Scanner s = new Scanner(new InputStreamReader(urlConnection.getInputStream()));
     while (s.hasNext()) {
     data.append(s.next());
     }
     System.out.println("Result: " + data);
     } catch (Exception ex) {
     System.out.println("Exception: " + ex.getMessage());
     }
     return data.toString();
     }*/
}
