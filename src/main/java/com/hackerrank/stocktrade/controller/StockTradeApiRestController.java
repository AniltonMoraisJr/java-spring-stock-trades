package com.hackerrank.stocktrade.controller;

import java.net.Authenticator.RequestorType;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.repository.TradeRepository;
import com.hackerrank.stocktrade.repository.UserRepository;

@RestController
@RequestMapping("/")
public class StockTradeApiRestController {
	
	@Autowired
	private TradeRepository tradeRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(path="erase",method=RequestMethod.DELETE)
	public ResponseEntity<String>deleteAllTrade(){
		try {
			tradeRepo.deleteAll();
			userRepo.deleteAll();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Deletado com sucesso", HttpStatus.OK);
	}
	
	@RequestMapping(path="trades", method=RequestMethod.POST)
	public ResponseEntity<String>createTrades(@RequestBody Trade trade){
		try {
			if(tradeRepo.existsById(trade.getId())) {
				return new ResponseEntity<String>("Trade já existe", HttpStatus.UNAUTHORIZED);
			}
			userRepo.save(trade.getUser());
			tradeRepo.save(trade);
			return new ResponseEntity<String>("Criado com sucesso", HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(path="trades", method=RequestMethod.GET)
	public ResponseEntity<List<Trade>> findAllTrades(){
		List<Trade>trades = new ArrayList<Trade>();
		trades = tradeRepo.findAllTrades();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Contenty-type", "application/json");
		return new ResponseEntity<List<Trade>>(trades, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(path="trades/users/{userID}", method=RequestMethod.GET)
	public ResponseEntity<List<Trade>> findTradesUser(@PathVariable("userID") Long id){
		List<Trade>trades = new ArrayList<Trade>();
		trades = tradeRepo.findTradeByUser(id);
		if(trades.isEmpty()) {
			return new ResponseEntity<List<Trade>>(trades, HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Contenty-type", "application/json");
		return new ResponseEntity<List<Trade>>(trades, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(path="stocks/{stockSymbol}/price", method=RequestMethod.GET)
	public ResponseEntity<List<Trade>> findTradesByStockAndDates(@PathVariable("stockSymbol") String stockSymbol, @RequestParam("start") String startDate, @RequestParam("end") String endDate) throws ParseException{
		List<Trade>trades = new ArrayList<Trade>();
		// formating dates
		DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = (Date) formatter.parse(startDate);
        Timestamp timeStampDate1 = new Timestamp(date.getTime());
        
        Date date2 = (Date) formatter.parse(endDate);
        Timestamp timeStampDate2 = new Timestamp(date2.getTime());
        
		trades = tradeRepo.findStock(stockSymbol,timeStampDate1, timeStampDate2);
		if(trades.isEmpty()) {
			return new ResponseEntity<List<Trade>>(trades, HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Contenty-type", "application/json");
		return new ResponseEntity<List<Trade>>(trades, headers, HttpStatus.CREATED);
	}
	
}
