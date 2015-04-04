package downloadDataBBAPI;

import java.util.HashMap;

public class CombinedPriceInfo {

	public HashMap<String,String> hashmap = new HashMap<String,String>();
	
	public void putPair(String key,Object value){
		String valueStr = value.toString();
		hashmap.put(key, valueStr);
	}
	
	public String[] getKeys(){
		return (String[]) hashmap.keySet().toArray();
	}
	
	public boolean isEmpty(){
		if(hashmap.keySet().size()>0){
			return false;
		}
		else{
			return true;
		}
	}
	
}
