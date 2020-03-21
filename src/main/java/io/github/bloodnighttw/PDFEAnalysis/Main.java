package io.github.bloodnighttw.PDFEAnalysis;

import java.io.File;
import java.io.IOException;

public class Main {

    static String path = "/home/bbeenn1227/IdeaProjects/EnAnalysis";

    public static void main(String[] args) throws IOException {

        File file = new File(path);
        for(File f : file.listFiles()){
            if(f.getName().endsWith(".pdf")){
                System.out.println("Open new thread ");
                new ReadFileThread(f.getAbsolutePath()).start();
            }
        }
    }

}
