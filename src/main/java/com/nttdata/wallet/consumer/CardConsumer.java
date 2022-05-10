package com.nttdata.wallet.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.model.CardWallet;
import com.nttdata.wallet.service.WalletService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CardConsumer {
	@Autowired
	WalletService walletService;
	@KafkaListener(topics = "${api.kafka-uri.card-topic-respose}", groupId = "group_id")
	public void cardConsumer(CardWallet cardWallet) {
		log.info("cardConsumer [CardWallet]:" + cardWallet.toString());
		Wallet wallet=this.walletService.findById(cardWallet.getWallet().getIdWallet()).blockOptional().orElse(null);
		if(wallet!=null) {
			wallet.setAssociatedWalletMessage(cardWallet.getWallet().getAssociatedWalletMessage());
			wallet.setAssociatedWallet(cardWallet.getWallet().getAssociatedWallet());
			wallet.setCardType(cardWallet.getWallet().getCardType());
			wallet.setIdBankAccount(cardWallet.getWallet().getIdBankAccount());	
			this.walletService.update(wallet).subscribe();
		}else {
			log.info("Wallet no encontrado [CardWallet]:" + cardWallet.toString());
		}
		
	}
}
