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
			String csvFileName, Map.Entry<String, Integer> entry) {
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
	
	public void writeReportSummaryInCsv(String csvFileName, Map<String, Integer> map) {
	    for (Map.Entry<String, Integer> entry: map.entrySet()) {
	    	writeReportSummaryInCsv(csvFileName, entry);
	    }   
	}
}
