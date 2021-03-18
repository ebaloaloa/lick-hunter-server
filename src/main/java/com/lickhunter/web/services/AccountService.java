package com.lickhunter.web.services;

public interface AccountService {

    void getAccountInformation() throws Exception;
    Boolean isOpenOrderIsolationActive(String key, Double isolationPercentage);
    Boolean isMaxOpenActive(String key, Long maxOpen);

}
