package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class ZipReader {

	public static void main(String[] args) {
		ZipReader zipReader = new ZipReader();
		zipReader.run(args);
	}

	private void run(String[] args) {
		String path = args[0];
		
		readFileInZip(path);
		
	}
	
	/*public void fileList(String[] args) {
		List<File> list = getZipFileList(args[0]);
	}

	public static List<File> getZipFileList(String args) {
		return getZipFileList(new File(args));
	}

	private static List<File> getZipFileList(File file) {
		List<File> resultList = new ArrayList<File>();
		
		if(!file.exists()) 
			return resultList;
		
		for(File f : list) {
			
		}
		
		return null;
	}*/

	public void readFileInZip(String path) {
		ZipFile zipFile;
		ArrayList<String> saveFile = new ArrayList<String>();
		
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();

		    while(entries.hasMoreElements()){
		    	ZipArchiveEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		    
		        ExcelReader myReader = new ExcelReader();
		       
		        
		        for(String value:myReader.getData(stream)) {
		        	saveFile.add(value);
		        }
		        
		        Utils.writeAFile(saveFile, "/Users/hyunwoo/Desktop/result.csv");
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
