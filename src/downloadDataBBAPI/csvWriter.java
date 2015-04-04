package downloadDataBBAPI;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {
	
	public void writeToFile(ArrayList<priceInfo> priceList,String fileName,String[] dataList){
		try
		{
			File file = new File(fileName);
			File parent_directory = file.getParentFile();

			if (null != parent_directory)
			{
			    parent_directory.mkdirs();
			}

			FileWriter writer = new FileWriter(file);
			
		    //write header of the csv file
			for(int i=0;i<dataList.length-1;i++){
				writer.append(dataList[i]);
				writer.append(",");
			}
			writer.append(dataList[dataList.length-1]);
			writer.append('\n');
		    
			//write data of the csv file
		    for(priceInfo info:priceList){
		    	//write data
		    	for(int i=0;i<dataList.length-1;i++){
					writer.append(String.valueOf(info.hashmap.get(dataList[i])));
					writer.append(",");
				}
				writer.append(String.valueOf(info.hashmap.get(dataList[dataList.length-1])));
				writer.append('\n');
		    }
	 
		    System.err.println("writing to "+fileName+"...");
		    
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	
	
	
	
	
	
	public void writeToFileCombined(ArrayList<CombinedPriceInfo> priceList,String fileName){
		try
		{
			File file = new File(fileName);
			File parent_directory = file.getParentFile();

			//check parent directory
			if (null != parent_directory)
			{
			    parent_directory.mkdirs();
			}
			
			//if empty file

			FileWriter writer = new FileWriter(file);
			
		    //write header of the csv file
			String[] keyHeaderList = priceList.get(0).getKeys();
			for(int i=0;i<keyHeaderList.length-1;i++){
				writer.append(keyHeaderList[i]);
				writer.append(",");
			}
			writer.append(keyHeaderList[keyHeaderList.length-1]);
			writer.append('\n');
		    
			//write data of the csv file
		    for(CombinedPriceInfo info:priceList){
		    	//write data
		    	for(int i=0;i<keyHeaderList.length-1;i++){
		    		String theKey = keyHeaderList[i];
		    		if(info.hashmap.containsKey(theKey)){
		    			writer.append(String.valueOf(info.hashmap.get(theKey)));
		    		}
					writer.append(",");
				}
		    	
		    	//write last item
		    	if(info.hashmap.containsKey(keyHeaderList[keyHeaderList.length-1])){
		    		writer.append(String.valueOf(info.hashmap.get(keyHeaderList[keyHeaderList.length-1])));
		    	}
				writer.append('\n');
		    }
	 
		    System.err.println("writing to "+fileName+"...");
		    
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	
	
	
	
	
	public void writeToFileABT(ArrayList<CombinedPriceInfo> priceList,String fileName,ArrayList<String> itemKeyList){
		try
		{
			File file = new File(fileName);
			File parent_directory = file.getParentFile();

			//check parent directory
			if (null != parent_directory)
			{
			    parent_directory.mkdirs();
			}
			
			//if empty file

			FileWriter writer = new FileWriter(file);
			
		    //write header of the csv file
//			String[] keyHeaderList = priceList.get(0).getKeys();
			for(int i=0;i<itemKeyList.size()-1;i++){
				writer.append(itemKeyList.get(i));
				writer.append(",");
			}
			writer.append(itemKeyList.get(itemKeyList.size()-1));
			writer.append('\n');
		    
			//write data of the csv file
		    for(CombinedPriceInfo info:priceList){
		    	//write data
		    	for(int i=0;i<itemKeyList.size()-1;i++){
		    		String theKey = itemKeyList.get(i);
		    		if(info.hashmap.containsKey(theKey)){
		    			writer.append(String.valueOf(info.hashmap.get(theKey)));
		    		}
					writer.append(",");
				}
		    	
		    	//write last item
		    	if(info.hashmap.containsKey(itemKeyList.get(itemKeyList.size()-1))){
		    		writer.append(String.valueOf(info.hashmap.get(itemKeyList.get(itemKeyList.size()-1))));
		    	}
				writer.append('\n');
		    }
	 
		    System.err.println("writing to "+fileName+"...");
		    
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	
	
	
	

}
