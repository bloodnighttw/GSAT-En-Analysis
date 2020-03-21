package io.github.bloodnighttw.PDFEAnalysis;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ReadFileThread extends Thread{

    static HashMap<String, Short> hmap = new HashMap();
    private String path = "file:/home/bbeenn1227/IdeaProjects/EnAnalysis/pdfex.pdf";
    static int nowRunnung = 0;

    ReadFileThread(String path){
        this.path = path ;
    }


    @Override
    public void run() {

        nowRunnung++;


        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder str = new StringBuilder();
        for(int i = 1 ; i <= pdfReader.getNumberOfPages() ; i++ ) {
            try {
                str.append(PdfTextExtractor.getTextFromPage(pdfReader, i)+" ");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        String string = str.toString();
        string = string.replaceAll("\t"," ");
        string = string.replaceAll("’"," ");
        string = string.replaceAll("”"," ");
        string = string.replaceAll("）"," ");
        string = string.replaceAll("（"," ");
        string = string.replaceAll("’s"," ").replaceAll("“"," ");

        string = string.replaceAll("\\p{Punct}"," ");


        InputStream inputStream = new ByteArrayInputStream(string.getBytes());
        Scanner sc = new Scanner(inputStream);

        Pattern p = Pattern.compile("[A-Z a-z]");
        Pattern p2 = Pattern.compile("[0-9]");

        while (sc.hasNext()){

            String st = sc.next();
            if(!st.matches("[A-Z]") && p.matcher(st).find() && !p2.matcher(st).find()){
                st = st.toLowerCase();
                //System.out.printf("%-15s +1\t\t\t",st);
                try {
                    short cache = hmap.get(st);
                    cache++;
                    //System.out.println(cache);
                    hmap.put(st,cache);
                }catch (NullPointerException e){
                    short cache = 1;
                    //System.out.println(cache);
                    hmap.put(st,cache);
                }
            }
        }


        sc.close();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nowRunnung --;
        if(nowRunnung == 0) {



            String[] st = hmap.keySet().toArray(new String[0]);
            Arrays.sort(st);

            File file = new File("result.txt");

            if(file.exists())
                file.delete();

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileWriter fileWriter = null ;
            try {
                fileWriter = new FileWriter("result.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }




            for (int i = 0; i < st.length; i++) {
                String cache = st[i];
                short vaule = hmap.get(cache);
                String stcache = String.format("%-30s\t\t\t%-10d\n", cache, vaule);
                System.out.printf(stcache);

                try {
                    fileWriter.write(stcache);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        stop();




    }
}
