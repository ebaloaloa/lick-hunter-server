package com.lickhunter.web.controllers;

import com.binance.client.model.market.PriceChangeTicker;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @PostMapping("/ticker")
    public ResponseEntity<?> getTickerByQuery(@RequestBody TickerQueryTO query) throws Exception {
        List<PriceChangeTicker> priceChangeTickerList = marketService.getTickerByQuery(query);
        return ResponseEntity.ok(priceChangeTickerList);
    }

    @GetMapping("/liquidation")
    public ResponseEntity<?> getLiquidation() throws Exception {
        marketService.getLiquidations();
        return ResponseEntity.ok(null);
    }
}
