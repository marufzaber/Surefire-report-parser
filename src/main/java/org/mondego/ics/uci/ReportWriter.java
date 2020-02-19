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
}
