package org.mondego.ics.uci;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParser {
	
	private static Map<String, Integer> testCountMapByCommit = 
			new HashMap<String, Integer>();
	
	private static Map<String, Integer> errorsCountMapByCommit = 
			new HashMap<String, Integer>();
	
	private static Map<String, Integer> failuresCountMapByCommit = 
			new HashMap<String, Integer>();
	
	private static Map<String, Integer> skippedCountMapByCommit = 
			new HashMap<String, Integer>();
	
	private static String summaryFile = "XX";
	
	public static void main(String[] args) {
		System.out.println("here");
		// TODO Auto-generated method stub
		if (args == null || args.length == 0) {
			System.out.println("No argument passed... pass a directory as a parameter");
			return;
		}
		if(args.length >=2) {
			summaryFile = args[1];
		}
		String directoryName = args[0];

		List<String> htmlFiles = scanHTMLFiles(directoryName);
		
		for (int i = 0; i< htmlFiles.size(); i++) {
			getTotalTestsRun(htmlFiles.get(i));
		}		
		ReportWriter reportWriter = new ReportWriter();
		
		// Writes total test count, error, failure, skip (in sequence)
		reportWriter.writeReportSummaryInCsv(
				summaryFile, 
				testCountMapByCommit, 
				errorsCountMapByCommit, 
				failuresCountMapByCommit, 
				skippedCountMapByCommit);
		
		Map<String, Integer> sum1 = new HashMap<String, Integer>();
		sum1.put("SUM", getSum(testCountMapByCommit));	
		sum1.put("AVERAGE", getAverage(testCountMapByCommit));
		
		Map<String, Integer> sum2 = new HashMap<String, Integer>();
		sum2.put("SUM", getSum(errorsCountMapByCommit));	
		sum2.put("AVERAGE", getAverage(errorsCountMapByCommit));

		Map<String, Integer> sum3 = new HashMap<String, Integer>();
		sum3.put("SUM", getSum(failuresCountMapByCommit));	
		sum3.put("AVERAGE", getAverage(failuresCountMapByCommit));
		
		Map<String, Integer> sum4 = new HashMap<String, Integer>();
		sum4.put("SUM", getSum(skippedCountMapByCommit));	
		sum4.put("AVERAGE", getAverage(skippedCountMapByCommit));
		
		reportWriter.writeReportSummaryInCsv(summaryFile, sum1, sum2, sum3, sum4);
	}
	
	private static int getTotalTestsRun(String fileName) {
		int testCount = 0;
		int errorsCount = 0;	
		int failuresCount = 0;	
		int skippedCount = 0;
		File input = new File(fileName);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Element table = doc.select("table").get(0); //select the first table.
			Elements rows = table.select("tr");

			for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
			    Element row = rows.get(i);
			    Elements cols = row.select("td");
			    testCount = Integer.parseInt(cols.get(0).text());
			    errorsCount = Integer.parseInt(cols.get(1).text());
			    failuresCount = Integer.parseInt(cols.get(2).text());
			    skippedCount = Integer.parseInt(cols.get(3).text());

			    // First splits the filepath and get the last part.
			    // Example file path - DONE-LOG/EKSTAZI/bval/3_0356c9d349fd76b43fcd9d4ba164327d387d4934_2.html
			    String [] parts = fileName.split("/");
			    String html = parts[parts.length - 1]; // gets 3_0356c9d349fd76b43fcd9d4ba164327d387d4934_2.html
			    
			    // Make sure it contains a hash code. Therefore, html files with other names will be discaded.
			    if (html.length() > 40 && html.contains("_")) { 
			    	String [] segments = html.split("_");
			    	String hash = segments[1]; //gets 0356c9d349fd76b43fcd9d4ba164327d387d4934
				     
			    	if (testCountMapByCommit.containsKey(hash)) {
			    		testCountMapByCommit.put(hash, testCountMapByCommit.get(hash) + testCount);
			    	} else {
			    		testCountMapByCommit.put(hash, testCount);
			    	}
			    	
			    	if (errorsCountMapByCommit.containsKey(hash)) {
			    		errorsCountMapByCommit.put(hash, errorsCountMapByCommit.get(hash) + errorsCount);
			    	} else {
			    		errorsCountMapByCommit.put(hash, errorsCount);
			    	}
			    	
			    	if (failuresCountMapByCommit.containsKey(hash)) {
			    		failuresCountMapByCommit.put(hash, failuresCountMapByCommit.get(hash) + failuresCount);
			    	} else {
			    		failuresCountMapByCommit.put(hash, failuresCount);
			    	}
			    	
			    	if (skippedCountMapByCommit.containsKey(hash)) {
			    		skippedCountMapByCommit.put(hash, skippedCountMapByCommit.get(hash) + skippedCount);
			    	} else {
			    		skippedCountMapByCommit.put(hash, skippedCount);
			    	}
			    	
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println(
					"******** Problem in converting string to number ********");
		}
		return testCount;
	} 
	
	private static int getSum(Map<String, Integer> map) {
		int sum = 0;
		for (Map.Entry<String, Integer> entry: map.entrySet()) {
			sum += entry.getValue();
		}
		return sum;
	}
	
	private static int getAverage(Map<String, Integer> map) {
		int sum = 0;
		for (Map.Entry<String, Integer> entry: map.entrySet()) {
			sum += entry.getValue();
		}
		
		return map.size() == 0 ? 0 : (int)(sum / map.size());
	}
	
	private static void print() {
		int sum = 0;
		for (Map.Entry<String, Integer> entry: testCountMapByCommit.entrySet()) {
			System.out.println(entry.getKey()+"		" + entry.getValue()	);
		}
	}
	
	private static List<String> scanHTMLFiles(String directoryName) {	
		List<String> pom = new ArrayList<String>();
		File directory = new File(directoryName);
	    File[] fList = directory.listFiles();	    	
	    if(fList != null) {
	    	for (File file : fList) {    	        	
	            if (file.isFile()) {
	            	String fileAbsolutePath = file.getAbsolutePath();	
	                if(fileAbsolutePath.endsWith(".html")){
	                	pom.add(fileAbsolutePath);
	                }	                	         
	            } else if (file.isDirectory()) {
	            	pom.addAll(scanHTMLFiles(file.getAbsolutePath()));
	            }
	        }
	    }
	    return pom;
    }
}
