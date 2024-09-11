package com.ewallet.dto;

import com.ewallet.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInitiateRequest {

    @NotBlank
    private String receiver;

    @NotNull
    private Double amount;

    private String purpose;

    public Transaction to() {
        return Transaction.builder()
                .amount(amount)
                .purpose(purpose)
                .receiver(receiver)
                .build();
    }
}
