package io.github.bloodnighttw.PDFEAnalysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class writtingThread extends Thread {

    private static HashMap<String, Short> hmap;
    private static String[] st;
    private static int i = -1;

    writtingThread(HashMap<String,Short>  hmap, String[] st) {
        writtingThread.hmap = hmap;
        writtingThread.st = st;
    }

    @Override
    public void run() {

        //String[] st = hmap.keySet().toArray(new String[0]);
        //Arrays.sort(st);


        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("result.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (; i < st.length - 1; ) {
            i++;
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

}
