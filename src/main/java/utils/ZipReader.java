package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import org.apache.commons.cli.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.csv.CSVRecord;

public class ZipReader extends Thread {
	
	private String dataPath;
	private String resultPath;
	private boolean help;
	private String[] argument;
	private File[] resultList;
	ArrayList<String> saveFile = new ArrayList<String>();

	/*public static void main(String[] args) {
		/*int numThreads = 5;
		Thread[] t = new Thread[numThreads];
		
		for(int i=0;i<numThreads;i++) {
			t[i] = new Thread(new ZipReader());
			t[i].start();
		}
		
		//ZipReader zipReader = new ZipReader();
		//zipReader.run(args);
	}*/

	public void run() {
		try {
			if(argument.length < 1)
				throw new NotEnoughArgumentException();
			
			Options options = createOptions();
			
			if(parseOption(options, argument)){
				if (help){
					System.out.println("help");
					printHelp(options);
					System.exit(0);
				}
				
				else {
					//readFileInZip(dataPath);
					getZipFileList(dataPath);
					
					for(File f:resultList) {
						if(f.getName().contains("zip"))
						readFileInZip(dataPath + f.getName());
					}
					
					Utils.writeAFile(saveFile, resultPath);
				}
			}
		}
		catch(NotEnoughArgumentException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	private Options createOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		 
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.argName("Help")
				.build());
		
		return options;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "Java Final HW";
		String footer = "";
		formatter.printHelp("Java Final HW",header,options,footer,true);
	}

	private boolean parseOption(Options options, String[] args) {
		DefaultParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
					
		} catch(Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;	
	}
	
	/*public void fileList(String[] args) {
		List<File> list = getZipFileList(args[0]);
	}

	public List<File> getZipFileList(String args) {
		return getZipFileList(new File(args));
	}*/

	public File[] getZipFileList(String path) {
		File file = new File(path);
		resultList = file.listFiles();
		
		for(int i = 0; i < resultList.length; i++)
			System.out.println(resultList[i]);
		
		return resultList;
	}

	public void readFileInZip(String path) {
		ZipFile zipFile;
		
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
		       
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setArg(String[] args) {
		argument = args;
	}
}
