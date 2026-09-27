package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    private final String CardNum;
    private final String PINCode;
    private BigDecimal balance;
    private BankType bankType;

    public Account(String CardNum, String PINCode, BigDecimal balance, BankType bankType) {
        if (CardNum.length() != 5) {
            throw new IllegalArgumentException("Номер карты должен состоять из 5 цифр. Получено" + CardNum);
        }
        if (PINCode == null || PINCode.length() != 3)  {
            throw new IllegalArgumentException("ПИН-Код должен состоять из трёх цифр. Получено" + PINCode);

        }
        if (bankType == null) {
            bankType = BankType.NEO;
        }

        this.CardNum = CardNum;
        this.PINCode = PINCode;
        this.bankType = bankType;

        if (balance == null) {
            this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        else {
            this.balance = balance.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public String getCardNum() {
        return CardNum;
    }

    public String getPINCode() {
        return PINCode;
    }

    public BigDecimal getbalance() {
        return balance;
    }

    public BankType getBankType() {
        return bankType;
    }

    @Override
    public String toString() {
        String outbalance = balance.setScale(2, RoundingMode.HALF_UP).toPlainString();

        return String.format("[%s] Карта: %05d, Баланс: %s руб.",
                bankType.getbankName(),
                CardNum,
                outbalance);
    }
}
