package io.github.bloodnighttw.PDFEAnalysis;

import java.io.File;
import java.io.IOException;

public class Main {

	static String path = "/home/bbeenn1227/IdeaProjects/";

	public static void main(String[] args) throws IOException {

		System.out.println("Working directory :\t" + System.getProperty("user.dir"));
		// 取得工作路徑
		path = System.getProperty("user.dir");
		
		File file = new File(path);

		System.out.println("h2");

		

		System.out.println("\n==================================================================================================\n");
		if(file.listFiles() == null ){
			System.out.println(file.toPath());
			return;
		}

		for (File f : file.listFiles()) {
			if (f.getName().endsWith(".pdf")) {
				System.out.println("Open new thread for file \" " + f.getPath() + " \"");
				//開一條執行序並執行
				new ReadFileThread(f.getAbsolutePath()).start();
		 	}
		}			

		System.out.println("\n==================================================================================================\n");

	}

}
