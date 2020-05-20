package org.mondego.ics.uci;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CSVParser {
	
	private static Map<String, Double> selectionTime = new HashMap<String, Double>();
	private static Map<String, Double> executionTime = new HashMap<String, Double>();
	
	public static void main(String[] args) {
		String directory = args[0];
		String log = args[1];
		List<String> allCSVs = scanSummaryCSVFiles(directory);
		
		for (int i = 0; i < allCSVs.size(); i++) {
			readCSVFile(allCSVs.get(i));
		}
		ReportWriter reportWriter = new ReportWriter();
		
		reportWriter.writeReportSummaryInCsv(log, selectionTime, executionTime);
	}
	
	
	
	private static void readCSVFile(String fileName) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(fileName));
		    while (scanner.hasNextLine()) {	 
		    	String line = scanner.nextLine();
		    	String[] parts = line.split(",");
		    	if (parts.length < 4) {
		    		continue;
		    	}
		    	
		    	String hash = parts[1];
		    	String selectionString = parts[2].contains("\"") ? 
		    			parts[2].substring(1, parts[2].length() - 1) :
		    				parts[2]; // Needed cause values are "" form

		    	String testString = parts[3].contains("\"") ? 
		    			parts[3].substring(1, parts[3].length() - 1) :
		    				parts[3]; // Needed cause values are "" form
	
		    			
		    	Double selection = Double.parseDouble(selectionString);
		    	Double test = Double.parseDouble(testString);
		    	selectionTime.put(hash, selection);
		    	executionTime.put(hash, test);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		}
	}
	
	private static List<String> scanSummaryCSVFiles(String directoryName) {	
		List<String> pom = new ArrayList<String>();
		File directory = new File(directoryName);
	    File[] fList = directory.listFiles();	    	
	    if(fList != null) {
	    	for (File file : fList) {    	        	
	            if (file.isFile()) {
	            	String fileAbsolutePath = file.getAbsolutePath();	
	                if(fileAbsolutePath.endsWith("SUMMARY.csv")){
	                	pom.add(fileAbsolutePath);
	                }	                	         
	            } else if (file.isDirectory()) {
	            	pom.addAll(scanSummaryCSVFiles(file.getAbsolutePath()));
	            }
	        }
	    }
	    return pom;
    }

}
