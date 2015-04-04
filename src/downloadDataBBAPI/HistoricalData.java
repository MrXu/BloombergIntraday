package downloadDataBBAPI;
import java.util.ArrayList;

import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;
import com.bloomberglp.blpapi.Name;
import com.bloomberglp.blpapi.Datetime;


public class HistoricalData{

//    public static void main(String[] args) throws Exception
//    {
//    	HistoricalData example = new HistoricalData();
//        example.run(args);
//        System.out.println("Press ENTER to quit");
//        System.in.read();
//    }

    private void run(String[] args) throws Exception
    {
        String serverHost = "localhost";
        int serverPort = 8194;

        SessionOptions sessionOptions = new SessionOptions();
        sessionOptions.setServerHost(serverHost);
        sessionOptions.setServerPort(serverPort);

        System.out.println("Connecting to " + serverHost + ":" + serverPort);
        Session session = new Session(sessionOptions);
        if (!session.start()) {
            System.err.println("Failed to start session.");
            return;
        }
        if (!session.openService("//blp/refdata")) {
            System.err.println("Failed to open //blp/refdata");
            return;
        }
        Service refDataService = session.getService("//blp/refdata");
        Request request = refDataService.createRequest("HistoricalDataRequest");

        
        
        /*
         * added for getting data for all stocks
         * 
         */
        ArrayList<stockInfo> stockList = new ArrayList<stockInfo>();
        csvFile stockFile = new csvFile();
        stockList = stockFile.parseStockCSV();
        //add stock names 
        Element securities = request.getElement("securities");
        for(stockInfo stock: stockList){
        	securities.appendValue(stock.stockName);
        }
        
        //end of modification
        
        //Element securities = request.getElement("securities");
        securities.appendValue("IBM US Equity");
        securities.appendValue("MSFT US Equity");

        Element fields = request.getElement("fields");
        fields.appendValue("PX_LAST");
        fields.appendValue("OPEN");

        request.set("periodicityAdjustment", "ACTUAL");
        request.set("periodicitySelection", "MONTHLY");
        request.set("startDate", "20150101");
        request.set("endDate", "20150208");
        request.set("maxDataPoints", 100);
        request.set("returnEids", true);

        System.out.println("Sending Request: " + request);
        session.sendRequest(request, null);

        while (true) {
            Event event = session.nextEvent();
            MessageIterator msgIter = event.messageIterator();
            while (msgIter.hasNext()) {
                Message msg = msgIter.next();
                System.out.println(msg);
            }
            if (event.eventType() == Event.EventType.RESPONSE) {
                break;
            }
        }
    }
}
