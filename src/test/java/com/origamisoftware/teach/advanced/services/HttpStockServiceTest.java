package com.origamisoftware.teach.advanced.services;

import com.origamisoftware.teach.advanced.model.StockQuote;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class HttpStockServiceTest {
    StockQuote stockQuote;
    GregorianCalendar stockDate;
    
    @Before
    public void setup() throws Exception {
        HttpStockService svc = new HttpStockService();
        
        stockDate = (GregorianCalendar)GregorianCalendar.getInstance();
        stockDate.set(2019, 10, 25, 0, 0, 0);
        
        stockQuote = svc.getQuote("MSFT", stockDate);
    }
    
    @Test
    public void testGetHttpStockSymbol() throws Exception {
        assertEquals("Quote symbols are equal", "MSFT", stockQuote.getSymbol());
    }
    
    @Test
    public void testGetHttpStockDate() throws Exception {
        Calendar actualDate = stockQuote.getDate();
        assertEquals("Quote date are equal", stockDate, actualDate);
    }
    
    @Test
    public void testGetHttpStockPrice() throws Exception {
        BigDecimal expectedPrice = new BigDecimal("140.7300");
        expectedPrice.setScale(4, RoundingMode.HALF_UP);
        
        BigDecimal actualPrive = stockQuote.getPrice();
        actualPrive.setScale(4, RoundingMode.HALF_UP);
        
        assertEquals("Quote price are equal", expectedPrice, actualPrive);
    }
}