package downloadDataBBAPI;

import java.sql.Time;

public class stockInfo {
	
	public String stockName;
	public String startTime;
	public String endTime;
	public String secId;
	public String countryCode;
	
	public stockInfo(String name,String start, String end, String secId, String countryCode){
		this.stockName = name;
		this.startTime = start;
		this.endTime = end;
		this.secId = secId;
		this.countryCode = countryCode;
		//formatDateTime();
	}
	
	//not used after converting to database query
	private void formatDateTime(){
//		System.out.println(startTime);
		
		if(this.startTime.length()==3){
			this.startTime = "0"+this.startTime;
		}
		if(this.endTime.length()==3){
			this.endTime = "0"+this.endTime;
		}
		
		this.startTime = "T"+this.startTime.substring(0, 2)+":"+this.startTime.substring(2)+":00";
		this.endTime = "T"+this.endTime.substring(0, 2)+":"+this.endTime.substring(2)+":00";
//		System.out.println(endTime);
	}
	
	public int getHourST(){
		if(this.startTime.length()==3){
			this.startTime = "0"+this.startTime;
		}
		
		String temp = this.startTime.substring(0, 2);
		int tempint = Integer.parseInt(temp);
		return tempint;
	}
	
	public int getMinuteST(){
		if(this.startTime.length()==3){
			this.startTime = "0"+this.startTime;
		}
		String temp = this.startTime.substring(2);
		int tempint = Integer.parseInt(temp);
		return tempint;
	}
	
	public int getHourET(){
		if(this.endTime.length()==3){
			this.endTime = "0"+this.endTime;
		}
		String temp = this.endTime.substring(0,2);
		int tempint = Integer.parseInt(temp);
		return tempint;
	}
	
	public int getMinuteET(){
		if(this.endTime.length()==3){
			this.endTime = "0"+this.endTime;
		}
		String temp = this.endTime.substring(2);
		int tempint = Integer.parseInt(temp);
		return tempint;
	}
	
}
