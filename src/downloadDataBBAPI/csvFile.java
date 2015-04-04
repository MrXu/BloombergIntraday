package downloadDataBBAPI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import downloadDataBBAPI.stockInfo;

public class csvFile {

	public String stockCsvFile = "R:/Research/XW/7-java/secuniverse.csv";
	public String datesCsvFile = "R:/Research/XW/7-java/dates.csv";
	public String countryCsvFile = "C:/SQTS/Automation/BBPARM/countryuniverse.csv";
	public BufferedReader br = null;
	public String line = "";
	public String cvsSplitBy = ",";
	
	
	public ArrayList<stockInfo> parseStockCSV(){
		ArrayList<stockInfo> stockInfoList = new ArrayList<stockInfo>();
		DateFormat formatter = new SimpleDateFormat("HHmm");
		Date thedate;
		try {
			 
			br = new BufferedReader(new FileReader(stockCsvFile));
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] stock = line.split(cvsSplitBy);
				String name = stock[0];
				String startTimeStr = stock[1];
				String endTimeStr = stock[2];
				String secId = stock[3];
				
				//get the country code
				String[] stockNameElements = name.split(" ");
				String countryCode = stockNameElements[1];
//				System.err.println(countryCode);
				
				//convert string to time
//				Time start = new Time(formatter.parse(startTimeStr).getTime());
//				Time end = new Time(formatter.parse(endTimeStr).getTime());
				//create new stock info 
				stockInfo newstock = new stockInfo(name,startTimeStr,endTimeStr,secId,countryCode);
				//append to the arraylist
				stockInfoList.add(newstock);
//				System.out.println("Country [code= " + country[4] 
//	                                 + " , name=" + country[5] + "]");
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Done");
		
		return stockInfoList;
	}
	
	
	
	/**
	 * get dates from csv csv file
	 * @return
	 */
	public ArrayList<String> getDates(){
		String fileName = datesCsvFile;

		ArrayList<String> dateList = new ArrayList<String>();
		
		
		try {
			 
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] date = line.split(cvsSplitBy);
				String name = date[0];
				dateList.add(name);
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Done");
		
		
		return dateList;
	}
	
	/**
	 * get the list of countries from csv file
	 * @return
	 */
	public ArrayList<String> getCountries(){
		String fileName = this.countryCsvFile;

		ArrayList<String> countryList = new ArrayList<String>();
		
		
		try {
			 
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] date = line.split(cvsSplitBy);
				String name = date[0];
				countryList.add(name);
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Done");
		
		
		return countryList;
	}
	
	
	public void setStockCsvFile(String csv){
		this.stockCsvFile = csv;
	}
	
	public void setDateCsvFile(String csv){
		this.datesCsvFile = csv;
	}
	
	public void setCountryCsvFile(String csv){
		this.countryCsvFile = csv;
	}
	
	public String getStockCsvFile(){
		return this.stockCsvFile;
	}
	
	public String getDateCsvFile(){
		return this.datesCsvFile;
	}
	
	public String getCountryCsvFile(){
		return this.countryCsvFile;
	}
	
}
