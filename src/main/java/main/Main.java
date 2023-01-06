package main;

import config.SpringConfig;
import entity.AccountEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.AccountService;

public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context= new AnnotationConfigApplicationContext(SpringConfig.class);
        AccountService accountService= (AccountService) context.getBean("accountService");
//        accountService.transferMoney(1,2,100);

//        accountService.listAllAccount();
        accountService.updateAccount(1);
    }
}
