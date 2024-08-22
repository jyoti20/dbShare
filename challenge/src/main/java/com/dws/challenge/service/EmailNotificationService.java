package com.dws.challenge.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

  @Override
  public void notifyAboutTransfer(Account account, String transferDescription) {
    //THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
   log  .info("Sending notification to owner of {}: {}", account.getAccountId(), transferDescription);
  }

public String generateCreditNotification(final Account toAccount,  final BigDecimal amount) {
	// TODO Auto-generated method stub
	
	StringBuilder sb = new StringBuilder(toAccount.getAccountId() + " account is credited with amount: " + amount);
	sb.append(" current balance is " + toAccount.getBalance());
	
	return sb.toString();
}

public String generateDebitNotification(final Account fromAccount,  final BigDecimal amount, final BigDecimal balanceAmt) {
	// TODO Auto-generated method stub
	
	StringBuilder sb = new StringBuilder(amount + " debited from : account id : " + fromAccount.getAccountId());
	sb.append(" current balance is " + balanceAmt);
	
	return sb.toString();
}

}
