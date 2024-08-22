package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  
  
  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  private  EmailNotificationService emailNotificationService;
  
  @Autowired
  public void setEmailNotificationService(EmailNotificationService emailNotificationService) {
      this.emailNotificationService = emailNotificationService;
  }
  
  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  @Transactional(rollbackFor = Exception.class)
  public void transferMoney(String fromAccountId, String toAccountId, BigDecimal amount) throws Exception {
   
      
   synchronized (this) {
	   
	   try {

	    	Account fromAccount = getAccount(fromAccountId);
	    	Account toAccount =  getAccount(toAccountId);
      
	    	BigDecimal balanceAmt = fromAccount.getBalance().subtract(amount);
      
      try {
          Thread.sleep(50);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      
      int  val = balanceAmt.compareTo(BigDecimal.ZERO) ;
      if (val == -1) {
        throw new Exception("Error in processing transfer - we do not support overdrafts");
      }
      else
      {
    	  fromAccount.setBalance(balanceAmt);
    	  accountsRepository.updateAccount(fromAccount); 
    	  
    	  toAccount.setBalance(toAccount.getBalance().add(amount));
          accountsRepository.updateAccount(toAccount);
          
          String descriptionDebit = emailNotificationService.generateCreditNotification(toAccount, amount);
          emailNotificationService.notifyAboutTransfer(toAccount, descriptionDebit );
          
          String descriptionCredit = emailNotificationService.generateDebitNotification(fromAccount, amount, balanceAmt);
          emailNotificationService.notifyAboutTransfer(fromAccount, descriptionCredit );
      }

     } catch (Exception e) {
      log.error("Error occurred while transferring money ...");
      throw e;
    }
    
    }
  }
}
