package com.hackerrank.stocktrade.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hackerrank.stocktrade.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>{
	@Query("SELECT t FROM Trade t JOIN FETCH t.user ORDER BY t.id")
	List<Trade>findAllTrades();
	
	@Query("SELECT t FROM Trade t JOIN FETCH t.user u WHERE u.id = :user ORDER BY t.id")
	List<Trade>findTradeByUser(@Param("user") Long id);
	
	@Query("SELECT t FROM Trade t JOIN FETCH t.user u WHERE t.symbol = :symbol AND t.timestamp BETWEEN :startDate AND :endDate ORDER BY t.id")
	List<Trade>findStock(@Param("symbol") String stockSymbol, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
