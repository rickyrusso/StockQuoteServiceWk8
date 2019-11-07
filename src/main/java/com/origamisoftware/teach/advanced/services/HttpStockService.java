package com.origamisoftware.teach.advanced.services;

import com.origamisoftware.teach.advanced.model.StockQuote;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpStockService implements StockService {

    @Override
    public StockQuote getQuote(String symbol, Calendar stockDate) throws StockServiceException {
        List<StockQuote> quotes = getQuotes(symbol, stockDate, stockDate);
        if(quotes.isEmpty()){
            return null;
        } else {
            return quotes.get(0);
        }
    }

    @Override
    public List<StockQuote> getQuotes(String symbol, Calendar from, Calendar until) throws StockServiceException {
        String jsonStr = null;
        HttpURLConnection con = null;
        
        try{
            String urlBase = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=A276CZ4ZR06VZKIN";

            URL url = new URL(String.format(urlBase, symbol));

            con = (HttpURLConnection) url.openConnection();
            
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int statusCode = con.getResponseCode();
            if(statusCode != 200)
                throw new StockServiceException("Http service return with a status code of " + statusCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                
                jsonStr = content.toString();
            }
            
        } catch(MalformedURLException malformedEx){
            throw new StockServiceException("The symbol was not valid.", malformedEx);
        } catch(IOException ioEx){
            throw new StockServiceException("Unable to connect to http service.", ioEx);
        } finally{
            if(con != null)
                con.disconnect();
        }

        ArrayList<StockQuote> stockQuotes = new ArrayList<>();
        try
        {
            Calendar currentDate = (Calendar)from.clone();

            Object obj = new JSONParser().parse(jsonStr);
            JSONObject jo = (JSONObject) obj;
            JSONObject series = (JSONObject)jo.get("Time Series (Daily)"); 
            
            while(currentDate.compareTo(until) <= 0) {
                String jsonDate = getJsonDateFromCalendar(currentDate);
           
                JSONObject firstDt = (JSONObject)series.get(jsonDate);           
                String closePrice = (String)firstDt.get("4. close");

                StockQuote stockQuote = new StockQuote();
                stockQuote.setSymbol(symbol);
                stockQuote.setDate((Calendar)currentDate.clone());
                stockQuote.setPrice(new BigDecimal(closePrice));
                stockQuotes.add(stockQuote);
                
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            
        } catch(ParseException parseEx){
            throw new StockServiceException("Unable to parse json from service", parseEx);
        }
        
        return stockQuotes;
    }
    
    
    
    private String getJsonDateFromCalendar(Calendar stockDate){
        int year = stockDate.get(Calendar.YEAR);
        int month = stockDate.get(Calendar.MONTH);
        int day = stockDate.get(Calendar.DATE);        
        return String.format("%d-%d-%d", year, month, day);
    }  
}
