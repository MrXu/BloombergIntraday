package downloadDataBBAPI;

import java.util.HashMap;

import com.bloomberglp.blpapi.Datetime;

public class priceInfo {
	
	public Datetime dateTime;
	public double close;
	public double open;
	public double high;
	public double low;
	public int numEvents;
	public long volume;
	
	public HashMap<String,Object> hashmap = new HashMap<String,Object>();
	
	public priceInfo(Datetime dateTime,double closePrice,long volume){
		this.dateTime = dateTime;
		this.close = closePrice;
		this.volume = volume;
	}
	
	public priceInfo(Datetime dateTime,double open,double high,double low,double closePrice,int numEvents,long volume){
		this.dateTime = dateTime;
		this.close = closePrice;
		this.open = open;
		this.high = high;
		this.low = low;
		this.numEvents = numEvents;
		this.volume = volume;
		
		hashmap.put("time", this.dateTime);
		hashmap.put("open", this.open);
		hashmap.put("high", this.high);
		hashmap.put("low", this.low);
		hashmap.put("close", this.close);
		hashmap.put("numEvents", this.numEvents);
		hashmap.put("volume", this.volume);
	}
	

}
