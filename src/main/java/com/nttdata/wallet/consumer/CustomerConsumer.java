package com.nttdata.wallet.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.model.CustomerWallet;
import com.nttdata.wallet.service.WalletService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomerConsumer {
	@Value("${api.kafka-uri.customer-topic-respose}")
	String customerTopicSave;
	@Autowired
	WalletService walletService;
	
	@KafkaListener(topics = "${api.kafka-uri.customer-topic-respose}", groupId = "group_id")
	public void customerConsumer(CustomerWallet customerWallet) {
		log.info("customerConsumer["+customerTopicSave+"]:" + customerWallet.toString());		
		Wallet wallet=this.walletService.findById(customerWallet.getIdWallet()).blockOptional().get();
		wallet.setIdCustomer(customerWallet.getIdCustomer());
		this.walletService.update(wallet).subscribe();
		log.info("customerConsumer[Save]:" + wallet);
	}
}
