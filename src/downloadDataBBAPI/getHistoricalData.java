package downloadDataBBAPI;

//import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import com.bloomberglp.blpapi.Datetime;

public class getHistoricalData {
	
	private static Properties config;
	
	private static final String TradeEvent = "TRADE";
	private static final String BidEvent = "BID";
	private static final String AskEvent = "ASK";
	
	//configuration file item names
	private static final String config_secuniversePath = "secuniversePath";
	private static final String config_countryUniversePath = "countryUniversePath";
	private static final String config_destinationFolder = "destinationFolder";
	private static final String config_dateChoice = "dateChoice";
	private static final String config_RangeOfDate = "RD";
	private static final String config_TodayDate = "TD";
	
	/**
	 * Arguments in sequence:
	 * dates: RD(Range of dates), TD(Today)
	 * case 1: "RD"
	 * case 2: "TD"
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		//initial variables
		ArrayList<stockInfo> stockList = new ArrayList<stockInfo>();
        csvFile csvFileReader = new csvFile();
        getHistoricalData downloadProcess = new getHistoricalData();
        ArrayList<String> dateList = new ArrayList<String>();
        ArrayList<String> countryList = new ArrayList<String>();
        //set default destination folder
		String destinationFolder = "F:/Data/IntraDay/";
        //set default configuration file path
		String configFilePath = "C:/SQTS/Automation/BBPARM/config.txt";
		
		//check the lengths of args
//		if(args.length>0){
			//String configFilePath = args[0];
			//testing 
			
			
			loadConfigFile(configFilePath);
			csvFileReader.setStockCsvFile(config.getProperty(config_secuniversePath));
			csvFileReader.setCountryCsvFile(config.getProperty(config_countryUniversePath));
			destinationFolder = config.getProperty(config_destinationFolder);
			String configChoice = config.getProperty(config_dateChoice);
			//configChoice="RD";
			//if choose the range of dates for ALL countries specified
			if(configChoice.equalsIgnoreCase(config_RangeOfDate)){
				System.out.println("*******************Download for Range of Dates (RD) ******************");
				//getting all dates from the stock files     
				csvFileReader.setDateCsvFile(config.getProperty("datesPath"));
		        dateList = csvFileReader.getDates();
			}
			else if(configChoice.equalsIgnoreCase(config_TodayDate)){
				System.out.println("*******************Download for Today (TD) *****************");
				//set today's date as the sole dates in the dateList
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				//get current date time with Date()
				Date todayDate = new Date();
				String todayDateStr = dateFormat.format(todayDate);
				dateList.add(todayDateStr);
			}
			else{
				System.err.println("No argument provided. Proceed with default arguments.");
				System.err.println("Default Security Universe: 		"+csvFileReader.getStockCsvFile());
				System.err.println("Default Dates List: 			"+csvFileReader.getDateCsvFile());
				System.err.println("Default Destination Folder:		"+destinationFolder);
				
				//getting all dates from the stock files
		        dateList = csvFileReader.getDates();
			}
			
			
			
			
			

			/**
			 * get the list of desired stocks by country code and stocks in security universe
			 */
	        //get the list of stocks to consider
	        stockList = csvFileReader.parseStockCSV();
	        ArrayList<stockInfo> stockListCopy = new ArrayList<stockInfo>(stockList);
	        
	        //get the list of countries to consider
	        countryList = csvFileReader.getCountries();
	        
	        //the stock must be within the countries specified
	        System.out.println("****Before removal: "+stockList.size());
	        for(stockInfo st:stockListCopy){
	        	if(!countryList.contains(st.countryCode)){
	        		System.out.println(st.stockName+" is removed!");
	        		stockList.remove(st);
	        	}
	        	else{
	        		System.out.println(st.stockName+" is kept!");
	        	}
	        }
	        System.out.println("After removal: "+stockList.size());
	        
	        
	        
	        /**
	         * get country gmt
	         */
	        //hashmap <countryCode, array of times>
			HashMap<String,ArrayList<String>> countryGMT = loadCountryGMT();
	        
	        
	        /**
	         * download data for dates specified
	         */
	        for(String date:dateList){
	        	//downloadProcess.dowloadForADay(date, destinationFolder, stockList,countryGMT);
	        	downloadProcess.dowloadForADayTBA(date, destinationFolder, stockList,countryGMT);
	        }
			
//		}
//		else{
			System.err.println("Arguement Missing!");
			System.err.println("Please choose from the following:");
//		}
		
        
        
        //end of program
        System.out.println("Program ending...");
//        System.in.read();
        
        return;
        
    }
	
	
	
	/**
	 * Download data for a specific date
	 * @param date
	 * @param destinationFolder
	 * @param stockList
	 * @param countryGMT
	 * @throws Exception
	 */
	public static void dowloadForADay(String date, String destinationFolder, ArrayList<stockInfo> stockList,HashMap<String,ArrayList<String>> countryGMT) throws Exception{
        
        //string to date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //get interval
        int interval = Integer.parseInt(config.getProperty("interval"));
        
        //loop through to get the result of query
        for(stockInfo stock:stockList){
        	
        	//set interval
        	IntradayBar intradayData = new IntradayBar(stock,interval);
        	
        	String startDate;
        	String endDate;      
        	
        	//ensure the country GMT is specified
        	if(countryGMT.containsKey(stock.countryCode)){
        		startDate = date + "T" + countryGMT.get(stock.countryCode).get(0);
        		endDate = date + "T" + countryGMT.get(stock.countryCode).get(1);
        		
        		//set date and time 
            	//modify after initialize
            	intradayData.setStartDateTime(startDate);
            	intradayData.setEndDateTime(endDate);
            	
            	
            	//set the list of data needed
            	String[] dataList = config.getProperty("dataListTrade").split(",");
            	for(int i=0;i<dataList.length;i++){
            		dataList[i]=dataList[i].trim();
            	}
            	
            	
            	//run result
            	intradayData.run();
            	
            	ArrayList <priceInfo> result = new ArrayList<priceInfo>();
            	result.addAll(intradayData.getPriceList());
            	
            	//write to a csv file
            	//path: detination folder / date / country code / securityId.csv 
                String fileName = destinationFolder+date+"/"+stock.countryCode+"/"+stock.secId+".csv";
                csvWriter writer = new csvWriter();
                writer.writeToFile(result,fileName,dataList);
        		
        		
        	}else{
        		System.err.println("Country GMT not found for country code: "+stock.countryCode);
        		System.err.println("The coutry code may be wrong or the country GMT is not specified");
        		
        	}
        	
//        	if(stock.getHourST()==8){
//        		startDate = date + "T00:00:00";
//        		endDate = date + "T02:00:00";
//        	}else if(stock.getHourST()==10){
//        		startDate = date + "T02:00:00";
//        		endDate = date + "T04:00:00";
//        	}
//        	else{
////        		String previousDate = previousDateString(date);
////        		System.err.println(previousDate);
////        		startDate = previousDate + "T23:00:00";  
//        		startDate = date + "T00:00:00";
//        		endDate = date + "T02:00:00";
//        	}
        	
        }
	}
	
	
	
	
	
	/**
	 * download for one day with Trade, Bid, Ask events
	 * @param date
	 * @param destinationFolder
	 * @param stockList
	 * @param countryGMT
	 * @throws Exception
	 */
	public static void dowloadForADayTBA(String date, String destinationFolder, ArrayList<stockInfo> stockList,HashMap<String,ArrayList<String>> countryGMT) throws Exception{
        
        //string to date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //get interval
        int interval = Integer.parseInt(config.getProperty("interval"));
        
        //loop through to get the result of query
        for(stockInfo stock:stockList){
        	//ensure the country GMT is specified
        	if(countryGMT.containsKey(stock.countryCode)){
        		
        		//three hashmaps of trade, bid and ask
        		String[] dataListTrade = trimListOfString("dataListTrade");
        		String[] dataListBid = trimListOfString("dataListBid");
        		String[] dataListAsk = trimListOfString("dataListAsk");
        		boolean t = false;
        		boolean b = false;
        		boolean a = false;
        		HashMap<String,priceInfo> tradeHashMap = null;
        		HashMap<String,priceInfo> bidHashMap = null;
        		HashMap<String,priceInfo> askHashMap = null;
        		
        		
        		
        		
        		
        		//if data list is larger zero
    	        System.out.println("dataListTrade.length: "+dataListTrade.length);
    	        System.out.println("dataListBid.length: "+dataListBid.length);
    	        System.out.println("dataListAsk.length: "+dataListAsk.length);    	        
            	if(dataListTrade.length>0){
            		tradeHashMap = getStockTimePriceHashMap(stock,TradeEvent,interval,date,countryGMT);
            		t = true;
            	}
            	if(dataListBid.length>0){
            		bidHashMap = getStockTimePriceHashMap(stock,BidEvent,interval,date,countryGMT);
            		b = true;
            	}
            	if(dataListAsk.length>0){
            		askHashMap = getStockTimePriceHashMap(stock,AskEvent,interval,date,countryGMT);
            		a = true;
            	}
            	
            	
            	//initialize the list of combined price info
            	ArrayList<CombinedPriceInfo> combinedResult = new ArrayList<CombinedPriceInfo>(); 
            	ArrayList<String> itemKeyList = new ArrayList<String>();
            	itemKeyList.add("DateTime");
            	
            	
            	//get the list of item key
            	for(String key:dataListTrade){
            		if(!key.equals("time")){
            			itemKeyList.add(TradeEvent+key);
            		}
            	}
            	for(String key:dataListBid){
            		if(!key.equals("time")){
            			itemKeyList.add(BidEvent+key);
            		}
            	}
            	for(String key:dataListAsk){
            		if(!key.equals("time")){
            			itemKeyList.add(AskEvent+key);
            		}
            	}
            	
            	
            	
            	//8 situations
            	if(t && b && a){
            		//the key is the time stamp
            		for(String timeStampKey:tradeHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//trade event info
                    	fillAllPrice(allprice,TradeEvent,timeStampKey,dataListTrade,tradeHashMap);
                    	//bid event info
                    	fillAllPrice(allprice,BidEvent,timeStampKey,dataListBid,bidHashMap);
                    	//ask event info
                    	fillAllPrice(allprice,AskEvent,timeStampKey,dataListAsk,askHashMap);
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	else if(t && b && !a){
            		//the key is the time stamp
            		for(String timeStampKey:tradeHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//trade event info
                    	fillAllPrice(allprice,TradeEvent,timeStampKey,dataListTrade,tradeHashMap);
                    	//bid event info
                    	fillAllPrice(allprice,BidEvent,timeStampKey,dataListBid,bidHashMap);
                    	//no ask event info
                    
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	else if(t && a && !b){
            		//the key is the time stamp
            		for(String timeStampKey:tradeHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//trade event info
                    	fillAllPrice(allprice,TradeEvent,timeStampKey,dataListTrade,tradeHashMap);
                    	//no bid event info
                    	
                    	//ask event info
                    	fillAllPrice(allprice,AskEvent,timeStampKey,dataListAsk,askHashMap);
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	else if(b && a && !t){
            		//the key is the time stamp
            		for(String timeStampKey:bidHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//no trade event info
                    	
                    	//bid event info
                    	fillAllPrice(allprice,BidEvent,timeStampKey,dataListBid,bidHashMap);
                    	//ask event info
                    	fillAllPrice(allprice,AskEvent,timeStampKey,dataListAsk,askHashMap);
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	else if(t && !a && !b){
            		//the key is the time stamp
            		for(String timeStampKey:tradeHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//trade event info
                    	fillAllPrice(allprice,TradeEvent,timeStampKey,dataListTrade,tradeHashMap);
                    	//no bid event info
                    	
                    	//no ask event info
                    	
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	else if(b && !t && !a){
            		//the key is the time stamp
            		for(String timeStampKey:bidHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//no trade event info
                    	
                    	//bid event info
                    	fillAllPrice(allprice,BidEvent,timeStampKey,dataListBid,bidHashMap);
                    	//no ask event info
                    	
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	else if(a && !t && !b){
            		//the key is the time stamp
            		for(String timeStampKey:askHashMap.keySet()){
            			//structure and store the downloaded information
                    	CombinedPriceInfo allprice = new CombinedPriceInfo();
                    	//set time
                    	allprice.putPair("DateTime", timeStampKey);
                    	
                    	//no trade event info
                    	
                    	//no bid event info
                    	
                    	//ask event info
                    	fillAllPrice(allprice,AskEvent,timeStampKey,dataListAsk,askHashMap);
                    	
                    	//add the new combined price info into the result list
                    	combinedResult.add(allprice);
            		}
            	}
            	
        		
            	
            	
            	//write to a csv file
            	//path: detination folder / date / country code / securityId.csv 
                String fileName = destinationFolder+date+"/"+stock.countryCode+"/"+stock.secId+".csv";
                csvWriter writer = new csvWriter();
//                writer.writeToFileCombined(combinedResult,fileName);
                writer.writeToFileABT(combinedResult,fileName,itemKeyList);
        		
        		
        	}else{
        		System.err.println("Country GMT not found for country code: "+stock.countryCode);
        		System.err.println("The coutry code may be wrong or the country GMT is not specified");
        		
        	}
        	
        	
        }
	}
	
	
	
	/**
	 * get the hashmap of one stock of one day
	 * @param stock
	 * @param eventType
	 * @param interval
	 * @param date
	 * @param countryGMT
	 * @return <time stamp, priceInfo>
	 */
	public static HashMap<String,priceInfo> getStockTimePriceHashMap(stockInfo stock,String eventType,int interval,String date,HashMap<String,ArrayList<String>> countryGMT){
		HashMap<String,priceInfo> TimePriceHashMap = new HashMap<String,priceInfo>();
		
		//set interval and eventType
    	IntradayBar intradayData = new IntradayBar(stock,interval,eventType);
    	
    	String startDate;
    	String endDate;      
    	
    	//ensure the country GMT is specified
    	if(countryGMT.containsKey(stock.countryCode)){
    		startDate = date + "T" + countryGMT.get(stock.countryCode).get(0);
    		endDate = date + "T" + countryGMT.get(stock.countryCode).get(1);
    		
    		//set date and time 
        	//modify after initialize
        	intradayData.setStartDateTime(startDate);
        	intradayData.setEndDateTime(endDate);
        	
        	
        	//run result
        	try {
				intradayData.run();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	TimePriceHashMap = intradayData.getSecPriceHashMap();
    	}
    	else{
    		System.err.println("Country GMT not found for country code: "+stock.countryCode);
    		System.err.println("The coutry code may be wrong or the country GMT is not specified");
    	}
		
		return TimePriceHashMap;
	}
	
	
	
	//helper functions
	/**
	 * return the list of data header in configuration file
	 * @param dataList
	 * @return
	 */
	public static String[] trimListOfString(String dataList){
		String[] newList = config.getProperty(dataList).split(",");
		for(int i=0;i<newList.length;i++){
			newList[i]=newList[i].trim();
    	}
		return newList;
	}
	
	
	/**
	 * 
	 * @param allPrice
	 * @param eventType
	 * @param timeStampKey
	 * @param dataList
	 * @param hashmap
	 */
	public static void fillAllPrice(CombinedPriceInfo allPrice,String eventType,String timeStampKey,String[] dataList,HashMap<String,priceInfo> hashmap){
		//create a new list of header
//		ArrayList<String> itemKeyList=null;
		//trade event info
    	for(String item:dataList){
    		if(!item.equals("time")){
    			String itemKey = eventType+item;
    			String itemValue = "";
    			//add key to header list
//    			itemKeyList.add(itemKey);
    			
    			if(hashmap.containsKey(timeStampKey)){
    				if(hashmap.get(timeStampKey).hashmap.containsKey(item)){
    					itemValue = hashmap.get(timeStampKey).hashmap.get(item).toString();
    				}
    			}
    			allPrice.putPair(itemKey, itemValue);
    		}
    	}
//    	return itemKeyList;
	}
	
	
	
	/**
	 * get the date of previous trading date
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public String previousDateString(String dateString) 
            throws ParseException {
        // Create a date formatter using your format string
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Parse the given date string into a Date object.
        // Note: This can throw a ParseException.
        Date myDate = dateFormat.parse(dateString);

        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        // Use the date formatter to produce a formatted date string
        Date previousDate = calendar.getTime();
        String result = dateFormat.format(previousDate);

        return result;
    }
	
	
	
	/**
	 * load configuration based on the specified configuration file 
	 * @param configFileName
	 */
	public static void loadConfigFile(String configFileName) {
		 
        //Load configuration file
        String filename = configFileName;
        config = new Properties();
 
        try {
 
            config.load(new FileInputStream(filename));
 
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        
         
        //Print out the configuration parameters
        Enumeration en = config.keys();
        
        System.out.println("******************* System configuration *******************");
        while (en.hasMoreElements()) {
            
            String key = (String) en.nextElement();
            System.out.println(key + " => " + config.get(key));
            
        }
    }
	
	
	/**
	 * load the GMT time of countries
	 * @return
	 */
	public static HashMap<String,ArrayList<String>> loadCountryGMT(){
		String fileName = config.getProperty("countryGMTPath");
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		HashMap<String,ArrayList<String>> countryGMTHashMap = new HashMap<String,ArrayList<String>>();
		
		
		try {
			 
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] valueList = line.split(cvsSplitBy);
				String countryCode = valueList[0];
				String startTime = valueList[1];
				String endTime = valueList[2];
				ArrayList<String> timeArray = new ArrayList<String>();
				timeArray.add(0, startTime);
				timeArray.add(1, endTime);
				countryGMTHashMap.put(countryCode, timeArray);
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
		
		System.out.println("********************Done: Country GMT loaded*****************");
		for(String key:countryGMTHashMap.keySet()){
			System.out.println(key+countryGMTHashMap.get(key).toString());
		}
		
		return countryGMTHashMap;	
		
	}
	
	
	
	
	
	
	
	
	
}
