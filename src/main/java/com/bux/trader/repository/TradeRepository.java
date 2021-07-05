package com.bux.trader.repository;

import com.bux.trader.repository.entity.TradePosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<TradePosition, Long> {
    TradePosition findByProductId(String productId);
}
