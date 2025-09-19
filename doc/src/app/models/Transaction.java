package app.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private UUID accountId;
    private LocalDateTime dateTransaction;
    private BigDecimal montant;

    public  Transaction(UUID id, UUID accountId, LocalDateTime dateTransaction, BigDecimal montant){
        this.id = id;
        this.accountId = accountId;
        this.dateTransaction = dateTransaction;
        this.montant = montant;
    }

    public  void setId(UUID id){
        this.id = id;
    }

    public  void setAccountId(UUID accountId){
        this.accountId = accountId;
    }

    public  void setDateTransaction(LocalDateTime dateTransaction){
        this.dateTransaction = dateTransaction;
    }

    public  void setMontant(BigDecimal montant){
        this.montant = montant;
    }

    public  UUID getId(){
        return id;
    }
    public UUID getAccountId() {
        return accountId;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public BigDecimal getMontant() {
        return montant;
    }
}
