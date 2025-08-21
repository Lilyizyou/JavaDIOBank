package com.dio.repository;

import com.dio.exceptions.AccountNotFoundException;
import com.dio.exceptions.PixInUseException;
import com.dio.model.AccountWallet;
import com.dio.model.MoneyAudit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.dio.repository.CommonsRepository.checkFundsTransaction;
import static java.util.stream.Stream.*;

public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds){
        if (!accounts.isEmpty()) {
            var pixInUse = accounts.stream().flatMap(account -> account.getPix().stream()).toList();
            for (var pixToCheck : pix) {
                if (pixInUse.contains(pixToCheck)) {
                    throw new PixInUseException("O pix " + pix + " já está em uso.");
                }
            }
        }
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount){
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "Depósito realizado com sucesso!");
    }

    public long withdraw(final String pix, final long amount){
        var source = findByPix(pix);
        checkFundsTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(sourcePix);
        checkFundsTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "Pix enviado de '" + sourcePix + "' para '" + targetPix + "'";
        target.addMoney(source.reduceMoney(amount), source.getService(), message);

    }

    public AccountWallet findByPix(final String pix){
        return accounts.stream().filter(account -> account.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("A conta com essa chave pix não existe ou foi encerrada."));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }

    public Map<LocalDateTime, List<MoneyAudit>> getHistory(String pix) {
        var account = findByPix(pix);
        // Stream of account transactions
        var accountTransactions = account.getFinancialTransactions().stream();

        // Stream of all investment transactions
        var investmentTransactions = account.getInvestmentWallets().stream()
                .flatMap(wallet -> wallet.getFinancialTransactions().stream());

        // Combine both streams and group by LocalDateTime
        return concat(accountTransactions, investmentTransactions)
                .collect(Collectors.groupingBy(
                        t -> t.createdAt().toLocalDateTime(),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }
}
