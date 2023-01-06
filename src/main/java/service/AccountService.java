package service;

import entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import repository.AccountRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    private JpaTransactionManager transactionManager;

    public AccountService(JpaTransactionManager jpaTransactionManager){

        this.transactionManager=jpaTransactionManager;
    }
    public void transferMoney(int sourceAccountId, int targetAccountId, double amount){
        TransactionDefinition definition= new DefaultTransactionDefinition();
        TransactionStatus status= transactionManager.getTransaction(definition);
        try {
            AccountEntity sourceAccount= accountRepository.findById(sourceAccountId).get();
            AccountEntity targetAccount= accountRepository.findById(targetAccountId).get();
            sourceAccount.setBalance(sourceAccount.getBalance()-amount);
            targetAccount.setBalance(targetAccount.getBalance()+amount);
            if (sourceAccount.getBalance()<0){
                throw new Exception("amount to transfer more than balance");
            }
            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
    public void listAllAccount(){
        List<AccountEntity> accountEntityList= (List<AccountEntity>) accountRepository.findAll();
        for (AccountEntity acc : accountEntityList
        ) {
            System.out.println(acc.toString());
        }
    }
    @Transactional(noRollbackFor = NullPointerException.class, rollbackFor = Exception.class)
    public void updateAccount(int id) throws Exception {
        Scanner sc= new Scanner(System.in);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        AccountEntity updateAccount= accountRepository.findById(id).get();
        System.out.println("New balance: ");
        updateAccount.setBalance(sc.nextInt());
        System.out.println("New Owner name: ");
        updateAccount.setOwner_name(sc.nextLine());
        sc.nextLine();
        System.out.println("New Access time: ");
        updateAccount.setAccessTime(new Date(sc.nextLine()));
        if (updateAccount.getAccessTime().getTime() < new Date().getTime()){
            throw new Exception("asdasd");
        }
        accountRepository.save(updateAccount);

    }
}
