package downloadDataBBAPI;


import com.bloomberglp.blpapi.Datetime;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;

import java.util.ArrayList;
import java.util.Calendar;




public class IntradayTick {

//    public static void main(String[] args) throws Exception
//    {
//        IntradayTick example = new IntradayTick();
//        example.run();
//        System.out.println("Press ENTER to quit");
//        System.in.read();
//    }

    private Calendar getPreviousTradingDate()
    {
    Calendar rightNow = Calendar.getInstance();
    rightNow.roll(Calendar.DAY_OF_MONTH, -1);
    if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        rightNow.roll(Calendar.DAY_OF_MONTH, -2);
    }
    else if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
        rightNow.roll(Calendar.DAY_OF_MONTH, -1);
    }
    return rightNow;
    }

    private void run() throws Exception
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
        
        //get the list of stocks
        ArrayList<stockInfo> stockList = new ArrayList<stockInfo>();
        csvFile stockFile = new csvFile();
        stockList = stockFile.parseStockCSV();
        //add stock names 
//        for(stockInfo stock: stockList){
//        	
//        	
//        	Request request = refDataService.createRequest("IntradayTickRequest");
//            request.set("security", stock.stockName);
//            request.getElement("eventTypes").appendValue("TRADE");
//            request.getElement("eventTypes").appendValue("AT_TRADE");
//
//            Calendar tradedOn = getPreviousTradingDate() ;
//            request.set("startDateTime", new Datetime(tradedOn.get(Calendar.YEAR),
//                              tradedOn.get(Calendar.MONTH) + 1,
//                              tradedOn.get(Calendar.DAY_OF_MONTH),
//                              10, 30, 0, 0));
//            request.set("endDateTime", new Datetime(tradedOn.get(Calendar.YEAR),
//                            tradedOn.get(Calendar.MONTH) + 1,
//                            tradedOn.get(Calendar.DAY_OF_MONTH),
//                            10, 35, 0, 0));
//            request.set("includeConditionCodes", true);
//
//            System.out.println("Sending Request: " + request);
//            session.sendRequest(request, null);
//        	
//        	
//        }
        
        String stockName = "VOD LN Equity";
        
        Request request = refDataService.createRequest("IntradayTickRequest");
        request.set("security", stockName);
//        request.set("interval", 5);
        request.getElement("eventTypes").appendValue("TRADE");
        request.getElement("eventTypes").appendValue("AT_TRADE");

    Calendar tradedOn = getPreviousTradingDate() ;
        request.set("startDateTime", new Datetime(tradedOn.get(Calendar.YEAR),
                          tradedOn.get(Calendar.MONTH) + 1,
                          tradedOn.get(Calendar.DAY_OF_MONTH),
                          10, 30, 0, 0));
    request.set("endDateTime", new Datetime(tradedOn.get(Calendar.YEAR),
                        tradedOn.get(Calendar.MONTH) + 1,
                        tradedOn.get(Calendar.DAY_OF_MONTH),
                        10, 35, 0, 0));
        request.set("includeConditionCodes", true);

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

