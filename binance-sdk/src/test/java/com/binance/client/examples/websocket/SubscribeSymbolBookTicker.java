package com.binance.client.examples.websocket;

import com.binance.client.SubscriptionClient;
import com.binance.client.examples.constants.PrivateConfig;

public class SubscribeSymbolBookTicker {

    public static void main(String[] args) {

        SubscriptionClient client = SubscriptionClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY);
   
        client.subscribeSymbolBookTickerEvent("btcusdt", ((event) -> {
            System.out.println(event);
            client.unsubscribeAll();
        }), null);

    }

}
