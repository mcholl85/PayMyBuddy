package com.payme.buddy.dto;

import com.payme.buddy.entity.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @NotBlank
    private String connection;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal fee;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    public static TransactionDto fromEntity(Transaction transaction) {
        return new TransactionDto(transaction.getSender().getFirstname(), transaction.getDescription(), transaction.getFee(), transaction.getAmount());
    }
}
