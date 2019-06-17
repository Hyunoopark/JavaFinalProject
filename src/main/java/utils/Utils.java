package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Utils {
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		//String addLine = "";
		//int count = 1;
		
		try (CSVPrinter printer = new CSVPrinter(new FileWriter(targetFileName), CSVFormat.DEFAULT)) {
		    for(String line:lines) {
		    		String[] aline = line.split("###");
		    		if(aline.length < 5) {
		    			String aline2 = "###";
		    			aline2 += line;
		    			String alines2[] = aline2.split("###");
		    			printer.printRecord(alines2);
		    		}
		    		else {
		    			printer.printRecord(aline);
		    		}
		    	}
		    	
		    
		    
		    System.out.println("Finish!");
		 } catch (IOException ex) {
		     ex.printStackTrace();
		 }
	}

}
