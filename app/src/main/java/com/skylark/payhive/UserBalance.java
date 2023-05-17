package com.skylark.payhive;

import java.math.BigDecimal;

public class UserBalance {
    private BigDecimal totalBalance;
    private BigDecimal bitcoinBalance;
    private BigDecimal ethereumBalance;
    private BigDecimal tetherBalance;
    private BigDecimal bnbBalance;
    private BigDecimal xrpBalance;
    private BigDecimal cardanoBalance;
    private BigDecimal dogecoinBalance;
    private BigDecimal litecoinBalance;
    private BigDecimal shibainuBalance;

    public UserBalance(BigDecimal totalBalance, BigDecimal bitcoinBalance, BigDecimal ethereumBalance, BigDecimal tetherBalance, BigDecimal bnbBalance, BigDecimal xrpBalance, BigDecimal cardanoBalance, BigDecimal dogecoinBalance, BigDecimal litecoinBalance, BigDecimal shibainuBalance) {
        this.totalBalance = totalBalance;
        this.bitcoinBalance = bitcoinBalance;
        this.ethereumBalance = ethereumBalance;
        this.tetherBalance = tetherBalance;
        this.bnbBalance = bnbBalance;
        this.xrpBalance = xrpBalance;
        this.cardanoBalance = cardanoBalance;
        this.dogecoinBalance = dogecoinBalance;
        this.litecoinBalance = litecoinBalance;
        this.shibainuBalance = shibainuBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getBitcoinBalance() {
        return bitcoinBalance;
    }

    public void setBitcoinBalance(BigDecimal bitcoinBalance) {
        this.bitcoinBalance = bitcoinBalance;
    }

    public BigDecimal getEthereumBalance() {
        return ethereumBalance;
    }

    public void setEthereumBalance(BigDecimal ethereumBalance) {
        this.ethereumBalance = ethereumBalance;
    }

    public BigDecimal getTetherBalance() {
        return tetherBalance;
    }

    public void setTetherBalance(BigDecimal tetherBalance) {
        this.tetherBalance = tetherBalance;
    }

    public BigDecimal getBnbBalance() {
        return bnbBalance;
    }

    public void setBnbBalance(BigDecimal bnbBalance) {
        this.bnbBalance = bnbBalance;
    }

    public BigDecimal getXrpBalance() {
        return xrpBalance;
    }

    public void setXrpBalance(BigDecimal xrpBalance) {
        this.xrpBalance = xrpBalance;
    }

    public BigDecimal getCardanoBalance() {
        return cardanoBalance;
    }

    public void setCardanoBalance(BigDecimal cardanoBalance) {
        this.cardanoBalance = cardanoBalance;
    }

    public BigDecimal getDogecoinBalance() {
        return dogecoinBalance;
    }

    public void setDogecoinBalance(BigDecimal dogecoinBalance) {
        this.dogecoinBalance = dogecoinBalance;
    }

    public BigDecimal getLitecoinBalance() {
        return litecoinBalance;
    }

    public void setLitecoinBalance(BigDecimal litecoinBalance) {
        this.litecoinBalance = litecoinBalance;
    }

    public BigDecimal getShibainuBalance() {
        return shibainuBalance;
    }

    public void setShibainuBalance(BigDecimal shibainuBalance) {
        this.shibainuBalance = shibainuBalance;
    }
}