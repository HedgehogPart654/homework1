package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashMachine implements WithdrawalOperations, DepositOperations {
    @Override
    public BigDecimal deposit(BigDecimal balance, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return balance;
        }
        else {
            return balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal withdraw(BigDecimal balance, BigDecimal amount, BankType bankType) {
        if (amount == null) {
            return balance;
        }

        BigDecimal comission = applyComission(amount, bankType);
        BigDecimal WithdrawRequire = amount.add(comission);

        if (WithdrawRequire.compareTo(balance) > 0) {
            System.out.println("На балансе не достаточно средств");
            return balance;
        }

        else{
            return balance.subtract(WithdrawRequire).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
