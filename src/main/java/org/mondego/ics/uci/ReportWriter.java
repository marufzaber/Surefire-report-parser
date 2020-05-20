package org.mondego.ics.uci;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.opencsv.CSVWriter;
public class ReportWriter {
	private int count;
	
	public ReportWriter() {
		count = 1;
	}
	
	public void writeReportSummaryInCsv(
			String csvFileName, 
			Map.Entry<String, 
			Integer> entry) {
		
	    CSVWriter writer;
	    try {
			writer = new CSVWriter(new FileWriter(csvFileName, true));
			String [] record = {
					Integer.toString(count),
					entry.getKey(),
					Integer.toString(entry.getValue())};
			
		    writer.writeNext(record);
		    writer.close();
		    count++;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}      
	}
	
	public void readReport() {
		
	}
	
	public void writeReportSummaryInCsv(
			String csvFileName, 
			String[] record) {
		CSVWriter writer;
	    try {
			writer = new CSVWriter(new FileWriter(csvFileName, true));
		    writer.writeNext(record);
		    writer.close();
		    count++;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}     
	}
	
	public void writeReportSummaryInCsv(
			String csvFileName, 
			Map<String, Integer> map) {
	    for (Map.Entry<String, Integer> entry: map.entrySet()) {
	    	writeReportSummaryInCsv(csvFileName, entry);
	    }   
	}
	
	public void writeReportSummaryInCsv(
			String csvFileName, 
			Map<String, Integer> map1,
			Map<String, Integer> map2,
			Map<String, Integer> map3,
			Map<String, Integer> map4) {
		
		int count = 0;
	    for (Map.Entry<String, Integer> entry: map1.entrySet()) {
	    	count++;
	    	String [] record = {
					Integer.toString(count),
					entry.getKey(),
					Integer.toString(entry.getValue()),
					Integer.toString(map2.get(entry.getKey())),
					Integer.toString(map3.get(entry.getKey())),
					Integer.toString(map4.get(entry.getKey()))};
	    	
	    	writeReportSummaryInCsv(csvFileName, record);
	    }   
	}
	
	public void writeReportSummaryInCsv(
			String csvFileName, 
			Map<String, Double> map1,
			Map<String, Double> map2) {
		
		int count = 0;
		double selectionSum = 0;
		double testSum = 0;
		try {
		    for (Map.Entry<String, Double> entry: map1.entrySet()) {
		    	count++;
		    	selectionSum += entry.getValue();
		    	testSum += map2.get(entry.getKey());
		    	String [] record = {
						Integer.toString(count),
						entry.getKey(),
						Double.toString(entry.getValue()),
						Double.toString(map2.get(entry.getKey()))};
		    	
		    	writeReportSummaryInCsv(csvFileName, record);
		    } 
		    String [] record = {
					Integer.toString(count),
					"SUM",
					Double.toString(selectionSum),
					Double.toString(testSum)};
	    	
	    	writeReportSummaryInCsv(csvFileName, record);
		} catch (NullPointerException e ) {
			e.printStackTrace();
		}
	}
}
