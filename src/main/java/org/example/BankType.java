package org.example;
import java.math.BigDecimal;

public enum BankType {

    NEO("НеоКредит Банк", new BigDecimal("0.01")),
    AUM("Арум Финтех", new BigDecimal("0.02")),
    VTA("Вектор Альянс Банк", new BigDecimal("0.00"));

    private final String bankName;
    private final BigDecimal Comission;

    BankType(String bankName, BigDecimal Comission) {
        this.bankName = bankName;
        this.Comission = Comission;
    }

    public String getbankName() {
        return bankName;
    }
    public BigDecimal Comission() {
        return Comission;
    }

}