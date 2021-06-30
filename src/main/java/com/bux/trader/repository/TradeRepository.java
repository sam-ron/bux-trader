package com.bux.trader.repository;

import com.bux.trader.entity.repository.TradePosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<TradePosition, Long> {
    TradePosition findByProductId(String productId);
}
