package com.ewallet.controller;

import com.ewallet.dto.TransactionInitiateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @PostMapping("/transact")
    public String initiateTxn(@RequestBody @Valid TransactionInitiateRequest request) {
        return null;
    }
}
