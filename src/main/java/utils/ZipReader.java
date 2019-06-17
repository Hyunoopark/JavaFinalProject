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
	private String secondResultPath;
	private boolean help;
	private String[] argument;
	private File[] resultList;
	private ArrayList<String> saveFile = new ArrayList<String>();
	private ArrayList<String> saveFile2 = new ArrayList<String>();

	
	public class MyFile<T> {
		  private static final int DEFAULT_CAPACITY = 10;
		  private Object element[];
		  private int index;

		  public MyFile() {
		    element = new Object[DEFAULT_CAPACITY];
		  }

		  public void add(T t) {
		    this.element[index++] = t;
		  }

		  public T get(int index){
		    return (T) element[index];
		  }
		}

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
					MyFile<String> myFile = new MyFile();
					
					myFile.add(dataPath);
					
					getZipFileList(myFile.get(0));
					
					for(File f:resultList) {
						if(f.getName().contains("zip")) {
							saveFile.add(f.getName());
							saveFile2.add(f.getName());
							readFileInZip(myFile.get(0) + f.getName());
						}
					}
					
					Utils.writeAFile(saveFile, resultPath);
					Utils.writeAFile(saveFile2, secondResultPath);
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
		
		options.addOption(Option.builder("o2").longOpt("output2")
				.desc("Set an second output file path")
				.hasArg()
				.argName("Second Output path")
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
		String header = "Java Final";
		String footer = "";
		formatter.printHelp("Java Final",header,options,footer,true);
	}

	private boolean parseOption(Options options, String[] args) {
		DefaultParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			secondResultPath = cmd.getOptionValue("o2");
			help = cmd.hasOption("h");
					
		} catch(Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;	
	}

	public File[] getZipFileList(String path) {
		File file = new File(path);
		resultList = file.listFiles();
		
		return resultList;
	}

	public void readFileInZip(String path) {
		ZipFile zipFile;
		int flag = 0;
		
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();

		    while(entries.hasMoreElements()){
		    	ZipArchiveEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		    
		        ExcelReader myReader = new ExcelReader();
		       
		        for(String value:myReader.getData(stream)) {
		        	if(flag == 0)
		        		saveFile.add(value);
		        	if(flag == 1)
		        		saveFile2.add(value);
		        }
		        flag++;
	
		        saveFile.add("");
		        saveFile2.add("");
		       
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
