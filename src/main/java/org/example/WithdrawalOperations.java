package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface WithdrawalOperations {
    BigDecimal withdraw(BigDecimal balance, BigDecimal amount, BankType bankType);
    default BigDecimal applyComission(BigDecimal amount, BankType bankType) {
        if (amount == null || bankType == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal comissionRate = bankType.Comission();
        BigDecimal comission = amount.multiply(comissionRate);
        return comission.setScale(2, RoundingMode.HALF_UP);
    }
}
