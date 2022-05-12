package com.nttdata.wallet.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import com.nttdata.purchaserequest.model.PurchaseRequestKafka;
import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.service.WalletService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PurchaseWalletConsumer {
	@Value("${api.kafka-uri.account-topic-respose}")
	String accountWalletTopicResponse;
	@Autowired
	KafkaTemplate<String, PurchaseRequestKafka> kafkaTemplate;
	
	@Autowired
	WalletService walletService;
	@KafkaListener(topics = "${api.kafka-uri.account-wallet-topic}", groupId = "group_id")
	public void purchaseWalletConsumer(PurchaseRequestKafka purchaseRequestKafka) {
		log.info("Mensaje recivido[purchaseWalletConsumer]:" + purchaseRequestKafka.toString());
		Wallet walletFind=new Wallet();
		walletFind.setPhone_number(purchaseRequestKafka.getCustomerOrigin().getPhoneNumber());
		walletFind.setTypeDocument(purchaseRequestKafka.getCustomerOrigin().getTypeDocument());
		walletFind.setDocumentNumber(purchaseRequestKafka.getCustomerOrigin().getDocumentNumber());
		walletFind.setEmail_address(purchaseRequestKafka.getCustomerOrigin().getEmailAddress());
		Wallet wallet=this.walletService.findByOne(walletFind).blockOptional().orElse(null);
		if(wallet!=null) {
			purchaseRequestKafka.setIdWallet(wallet.getIdWallet());
			log.info("Enviando kafka de wallet:"+accountWalletTopicResponse+"-->"+purchaseRequestKafka.toString());
			kafkaTemplate.send(accountWalletTopicResponse,purchaseRequestKafka);
		}
		 
	}
}
