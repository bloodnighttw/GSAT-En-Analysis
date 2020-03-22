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
    private String[] st = null;

    ReadFileThread(String path){
        this.path = path ;
    }


    @Override
    public void run() {
        /*
         *  執行序安全的設置
         *  執行序開始後 會加 1
         *  結束時 會減 1
         *  也就是說 當最號一條 thread 執行完之後
         *  now running 會被執行完
         *
         */
        nowRunnung++;

        File file = new File("result.txt");

        if (file.exists())
            file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder str = new StringBuilder();
        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
            try {
                str.append(PdfTextExtractor.getTextFromPage(pdfReader, i)+" ");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        String string = str.toString();
        string = string.replaceAll("\t"," ");
        string = string.replaceAll("’", " ");
        string = string.replaceAll("‘", " ");
        string = string.replaceAll("”", " ");
        string = string.replaceAll("）"," ");
        string = string.replaceAll("（"," ");
        string = string.replaceAll("’s"," ").replaceAll("“"," ");

        string = string.replaceAll("\\p{Punct}"," ");


        InputStream inputStream = new ByteArrayInputStream(string.getBytes());
        Scanner sc = new Scanner(inputStream);

        Pattern p = Pattern.compile("[A-Z a-z]");
        Pattern p2 = Pattern.compile("[0-9]");

        while (sc.hasNext()) {

            String st = sc.next();
            if (!st.matches("[A-Z]") && p.matcher(st).find() && !p2.matcher(st).find()) {

                st = st.toLowerCase();
                add(st);

            }
        }


        sc.close();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nowRunnung--;
        if (nowRunnung == 0) {
            String[] st = hmap.keySet().toArray(new String[0]);
            Arrays.sort(st);
            this.st = st;

            /*
             *  原本使用多執行序優化
             *  結果反而比一條 thread 慢（考慮執行安全後）
             *
             */
            for (int i = 0; i < 1; i++)
                startWritting();

        }

    }

    private static synchronized void add(String st) {

        try {

            short cache = hmap.get(st);
            cache++;
            //System.out.println(cache);
            hmap.put(st, cache);

        } catch (NullPointerException e) {
            short cache = 1;
            //System.out.println(cache);
            hmap.put(st, cache);
        }

    }

    private synchronized void startWritting() {

        new writtingThread(path, hmap, st).start();
        try {
            sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
