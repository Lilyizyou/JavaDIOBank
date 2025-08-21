package com.dio;

import com.dio.exceptions.*;
import com.dio.repository.AccountRepository;
import com.dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    public static void main(String[] args) {
        System.out.println("Bem vindo ao DIO BANK!");
        while (true) {
            System.out.println("Selecione uma dessas opções:");
            System.out.println("1 - Criar nova conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Despositar");
            System.out.println("5 - Sacar");
            System.out.println("6 - Transferir");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar Investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Histórico de conta");
            System.out.println("0 - Sair");
            var option = scanner.nextInt();
            switch (option){
                case 1 -> createAccount();
                case 2 -> createInvestment();
                case 3 -> createWalletInvestment();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transferToAccount();
                case 7 -> incInvestment();
                case 8 -> rescueInvestment();
                case 9->
                        accountRepository.list().forEach(System.out::println);
                case 10->
                        investmentRepository.list().forEach(System.out::println);
                case 11->
                        investmentRepository.listWallets().forEach(System.out::println);
                case 12-> {
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos atualizados com sucesso!");
                }
                case 13 -> checkHistory();
                case 0 -> {
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private static void createAccount() {
        System.out.println("Informe as chaves pix separadas por ;");
        scanner.nextLine();
        var pix = Arrays.stream(scanner.next().split(",")).toList();
        System.out.println("Informe o valor inicial de deposito");
        var initialFunds = scanner.nextLong();
        try {
            var wallet = accountRepository.create(pix, initialFunds);
            System.out.println("Conta criada com sucesso!: " + wallet);
        } catch (PixInUseException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void createInvestment() {
        System.out.println("Informe a taxa do investimento");
        var tax = scanner.nextLong();
        System.out.println("Informe o valor inicial de deposito");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento criado com sucesso! "+ investment);
    }

    private static void deposit() {
        System.out.println("Informe a chave pix da conta para depósito:");
        scanner.nextLine();
        var pix = scanner.nextLine();
        System.out.println("Informe o valor a ser depositado:");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
            System.out.println("Depósito realizado com sucesso!");
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void withdraw() {
        System.out.println("Informe a chave pix da conta para saque:");
        scanner.nextLine();
        var pix = scanner.nextLine();
        System.out.println("Informe o valor a ser sacado:");
        var amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
            System.out.println("Saque realizado com sucesso!");
        } catch (AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void transferToAccount() {
        System.out.println("Informe a chave pix da conta de origem:");
        scanner.nextLine();
        var sourcePix = scanner.nextLine();
        System.out.println("Informe a chave pix da conta de destino:");
        var targetPix = scanner.nextLine();
        System.out.println("Informe o valor a ser depositado:");
        var amount = scanner.nextLong();
        try {
            accountRepository.transferMoney(sourcePix, targetPix, amount);
            System.out.println("Depósito realizado com sucesso!");
        } catch (AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void createWalletInvestment(){
        System.out.println("Informe a chave pix da conta");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o identificador do investimento");
        var investmentId = scanner.nextInt();
        var investmentWallet = investmentRepository.initInvestment(account, investmentId);
        System.out.println("Carteira de investimento criada com sucesso!" + investmentWallet);
    }

    private static void incInvestment() {
        System.out.println("Informe a chave pix da conta para investimento:");
        scanner.nextLine();
        var pix = scanner.nextLine();
        System.out.println("Informe o valor a ser investido");
        var amount = scanner.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
            System.out.println("Investimento realizado com sucesso!");
        } catch (WalletNotFoundException | AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void rescueInvestment() {
        System.out.println("Informe a chave pix da conta para resgatar investimento:");
        scanner.nextLine();
        var pix = scanner.nextLine();
        System.out.println("Informe o valor a ser resgatado:");
        var amount = scanner.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
            System.out.println("Resgate de investimento realizado com sucesso!");
        } catch (AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void updateInvestments() {
        investmentRepository.updateAmount();
        System.out.println("Investment amounts updated successfully.");
    }

    public static void checkHistory() {
        System.out.println("Informe a chave pix da conta para consultar o histórico:");
        scanner.nextLine();
        var pix = scanner.nextLine();
        try {
            var sortedHistory = accountRepository.getHistory(pix);
            System.out.println("Histórico de transações para a conta com chave pix " + pix + ":");
            sortedHistory.forEach((k, v) -> {
                System.out.println(k.format(ISO_DATE_TIME));
                System.out.println(v.getFirst().transactionId());
                System.out.println(v.getFirst().description());
                System.out.println("R$" + (v.size() / 100) + "," + (v.size() % 100));
            });
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
