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

    @Test
    public void testGetHttpStockSymbolDateRanges() throws StockServiceException{
        HttpStockService svc = new HttpStockService();

        List<StockQuote> stockQuotes;
        GregorianCalendar untilStockDate;
        GregorianCalendar fromStockDate;

        untilStockDate = (GregorianCalendar)GregorianCalendar.getInstance();
        untilStockDate.setTimeInMillis(0);
        untilStockDate.set(2019, 10, 25);

        fromStockDate = (GregorianCalendar)GregorianCalendar.getInstance();
        fromStockDate.setTimeInMillis(0);
        fromStockDate.set(2019, 10, 23);
        stockQuotes = svc.getQuotes("GOOG", fromStockDate, untilStockDate);

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