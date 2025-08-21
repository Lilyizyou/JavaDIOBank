package com.dio.repository;

import com.dio.exceptions.AccountWithInvestmentException;
import com.dio.exceptions.InvestmentNotFoundException;
import com.dio.exceptions.WalletNotFoundException;
import com.dio.model.AccountWallet;
import com.dio.model.Investment;
import com.dio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static com.dio.repository.CommonsRepository.checkFundsTransaction;

public class InvestmentRepository {

    private final List<Investment> investments = new ArrayList<>();

    private final List<InvestmentWallet> wallets = new ArrayList<>();

    private long nextId = 0;

    public Investment create(final long tax, final long initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        if (!wallets.isEmpty()) {
            var accountsInUse = wallets.stream()
                    .map(InvestmentWallet::getAccount).toList();
            if (accountsInUse.contains(account)) {
                throw new AccountWithInvestmentException("Essa conta '" + account + "' já tem um investimento na carteira.");
            }
        }
        var investment = findById(id);
        checkFundsTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        account.getInvestmentWallets().add(wallet);
        return wallet;
    }

    public void deposit(final String pix, final long funds) {
            var wallet = findWalletByAccountPix(pix);
            wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Depósito realizado com sucesso.");
    }

    public void withdraw(final String pix, final long funds) {
        var wallet = findWalletByAccountPix(pix);
        checkFundsTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "Saque realizado com sucesso.");
        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
    }

    public void updateAmount() {
        wallets.forEach(wallet -> wallet.updateAmount(wallet.getInvestment().tax()));
    }

    public Investment findById(final long id) {
        return investments.stream()
                .filter(investment -> investment.id() == id)
                .findFirst()
                .orElseThrow(() -> new InvestmentNotFoundException("A carteira com o ID " + id + " não foi encontrada.")
                );
    }

    public InvestmentWallet findWalletByAccountPix(final String pix) {
        return wallets.stream()
                .filter(wallet -> wallet.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException("A carteira com o PIX " + pix + " não foi encontrada.")
                );
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investment> list() {
        return this.investments;
    }
}
