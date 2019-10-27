package com.origamisoftware.teach.advanced.services;

import com.origamisoftware.teach.advanced.model.StockQuote;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class HttpStockServiceTest {
    StockQuote stockQuote;
    List<StockQuote> stockQuotes;
    GregorianCalendar untilStockDate;
    GregorianCalendar fromStockDate;
    
    @Before
    public void setup() throws Exception {
        HttpStockService svc = new HttpStockService();
        
        untilStockDate = (GregorianCalendar)GregorianCalendar.getInstance();
        untilStockDate.setTimeInMillis(0);
        untilStockDate.set(2019, 10, 25);
        
        stockQuote = svc.getQuote("MSFT", untilStockDate);
        
        fromStockDate = (GregorianCalendar)GregorianCalendar.getInstance();
        fromStockDate.setTimeInMillis(0);
        fromStockDate.set(2019, 10, 23);
        stockQuotes = svc.getQuotes("GOOG", fromStockDate, untilStockDate);
    }
    
    @Test
    public void testGetHttpStockSymbol() {
        assertEquals("Quote symbols are equal", "MSFT", stockQuote.getSymbol());
    }
    
    @Test
    public void testGetHttpStockDate() {
        Calendar actualDate = stockQuote.getDate();
        assertEquals("Quote date are equal", untilStockDate, actualDate);
    }
    
    @Test
    public void testGetHttpStockPrice()  {
        BigDecimal expectedPrice = new BigDecimal("140.7300");
        expectedPrice.setScale(4, RoundingMode.HALF_UP);
        
        BigDecimal actualPrive = stockQuote.getPrice();
        actualPrive.setScale(4, RoundingMode.HALF_UP);
        
        assertEquals("Quote price are equal", expectedPrice, actualPrive);
    }
    
    @Test
    public void testGetHttpStockSymbolDateRanges() {
        StockQuote actualFromStockQuote = stockQuotes.get(0);
        StockQuote actualUntilStockQuote = stockQuotes.get(2);
        
        BigDecimal actualFromPrice = actualFromStockQuote.getPrice();
        actualFromPrice.setScale(4, RoundingMode.HALF_UP);
        
        BigDecimal actualUntilPrice = actualUntilStockQuote.getPrice();
        actualUntilPrice.setScale(4, RoundingMode.HALF_UP);
        
        
        BigDecimal expectedFromPrice = new BigDecimal("1259.1300");
        expectedFromPrice.setScale(4, RoundingMode.HALF_UP);
        
        BigDecimal expectedUntilPrice = new BigDecimal("1265.1300");
        expectedUntilPrice.setScale(4, RoundingMode.HALF_UP);
        
        
        assertEquals("Quote from price are equal", expectedFromPrice, actualFromPrice);
        assertEquals("Quote until price are equal", expectedUntilPrice, actualUntilPrice);
    }
}