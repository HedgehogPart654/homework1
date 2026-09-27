package org.example;

import java.math.BigDecimal;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Account referenceAccount = new Account("12345", "999", new BigDecimal("10000.00"), BankType.AUM);

        System.out.print("Добро пожаловать! ");
        System.out.print("Введите номер карты: \n");

        String inputCardNum = scanner.next();

        if (!inputCardNum.equals(referenceAccount.getCardNum())) {
            System.out.print("Ошибка доступа: неверный номер карты.");
            return;
        }
        System.out.print("Введите ПИН-Код: \n");
        String inputPinCode = scanner.next();

        if (!inputPinCode.equals(referenceAccount.getPINCode())) {
            System.out.print("Ошибка доступа: неверный ПИН-Код.");
            return;
        }

        System.out.print("Авторизация прошла успешно. Добро пожаловать!");

        CashMachine cashMachine = new CashMachine();
        BigDecimal currentBalance = referenceAccount.getbalance();
        BankType currentBankType = referenceAccount.getBankType();

        System.out.println("Введите тип операции\n 1: Внесение наличных\n 2: Снятие наличных");
        int operationType = scanner.nextInt();

        if (operationType == 1) {
            System.out.print("Введите сумму для внесения: ");
            BigDecimal depositAmount = scanner.nextBigDecimal();

            currentBalance = cashMachine.deposit(currentBalance, depositAmount);
            System.out.print("Успешно. Обновлённый баланс: " + currentBalance + " руб.");
        }
        else {
            if (operationType == 2) {
                System.out.print("Введите сумму для снятия: ");
                BigDecimal withdrawAmount = scanner.nextBigDecimal();

                currentBalance = cashMachine.withdraw(currentBalance, withdrawAmount, currentBankType);
                System.out.print("Обновлённый баланс: " + currentBalance + " руб.");
            }
        }
    }
}