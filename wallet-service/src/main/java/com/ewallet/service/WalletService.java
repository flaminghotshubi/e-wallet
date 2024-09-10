package com.ewallet.service;

import com.ewallet.model.Wallet;
import com.ewallet.repository.WalletRepository;
import com.ewallet.utils.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    JSONParser jsonParser;

    @Value("${wallet.create.opening-balance:100.0}")
    private Double openingBalance;

    @KafkaListener(topics = {Constants.USER_CREATED_TOPIC}, groupId = "test_123")
    public void create(String msg) {
        try {
            JSONObject event = (JSONObject) jsonParser.parse(msg);
            String mobile = (String) event.get("mobile");
            if (null == mobile) {
                logger.warn("create: unable to find mobile no. in the event, data = {}", event);
                return;
            }
            walletRepository.save(Wallet.builder()
                    .balance(openingBalance)
                    .mobile(mobile)
                    .build());
        } catch (Exception e) {
            logger.error("Error occurred while creating wallet", e);
        }
    }
}
