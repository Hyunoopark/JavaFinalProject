package utils;

//import java.io.IOException;

public class Main {
	public static void main(String[] args) throws Exception {
		int numThreads = 5;
		Thread[] t = new Thread[numThreads];
		
		for(int i=0;i<numThreads;i++) {
			ZipReader zipReader = new ZipReader(); 
			zipReader.setArg(args);
			t[i] = new Thread(zipReader);
			t[i].start();
		}
	}
}