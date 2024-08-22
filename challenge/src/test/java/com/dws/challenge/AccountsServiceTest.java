package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ImportResource({"classpath*:application-context.xml"})
class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  void addAccount() {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  void addAccount_failsOnDuplicateId() {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }
  }
  
  @Test
  void transferMoneyFailsOnNegativeBalance(){
	  Account toAccount = new Account("Id-123");
	  toAccount.setBalance(new BigDecimal(200));
	  this.accountsService.createAccount(toAccount);
	    
	  Account fromAccount = new Account("Id-456");
	  fromAccount.setBalance(new BigDecimal(200)); // Some way to set balance
	  this.accountsService.createAccount(fromAccount);

	  try {
		this.accountsService.transferMoney(fromAccount.getAccountId(), toAccount.getAccountId(), new BigDecimal(300) );
	} catch (Exception e) {
		e.printStackTrace();
		assertThat(e.getMessage()).isEqualTo("Error in processing transfer - we do not support overdrafts");
		
		assertThat(fromAccount.getBalance().equals(new BigDecimal(60)));
		
		assertThat(BigDecimal.valueOf(115).equals(toAccount.getBalance()));
	} 
  }
  
  @Test
  void transferMoney(){
	  Account toAccount = new Account("Id-123");
	  toAccount.setBalance(new BigDecimal(200));
	  this.accountsService.createAccount(toAccount);
	    
	  Account fromAccount = new Account("Id-456");
	  fromAccount.setBalance(new BigDecimal(200)); // Some way to set balance
	  this.accountsService.createAccount(fromAccount);

	  try {
		this.accountsService.transferMoney(fromAccount.getAccountId(), toAccount.getAccountId(), new BigDecimal(100) );
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		assertThat(e.getMessage()).isEqualTo("Error in processing transfer - we do not support overdrafts");
		
		assertThat(fromAccount.getBalance().equals(new BigDecimal(60)));
		
		assertThat(BigDecimal.valueOf(115).equals(toAccount.getBalance()));
	} 
  }
  

  
}
