package com.nttdata.wallet.service.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.model.Card;
import com.nttdata.wallet.model.CardResponse;
import com.nttdata.wallet.model.CardWallet;
import com.nttdata.wallet.model.CustomerWallet;
import com.nttdata.wallet.model.WalletResponse;
import com.nttdata.wallet.repository.WalletRepository;
import com.nttdata.wallet.service.WalletService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class WalletServiceImpl implements WalletService {
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	KafkaTemplate<String, CustomerWallet> kafkaTemplate;
	@Autowired
	KafkaTemplate<String, CardWallet> kafkaTemplateCard;
	@Value("${api.kafka-uri.customer-topic}")
	String customerTopic;
	@Value("${api.kafka-uri.card-topic}")
	String cardTopic;

	@Override
	public Flux<Wallet> findAll() {
		// TODO Auto-generated method stub
		return walletRepository.findAll();
	}

	@Override
	public Mono<Wallet> findById(Long idWallet) {
		// TODO Auto-generated method stub
		return walletRepository.findById(idWallet);
	}

	@Override
	public Mono<Wallet> save(Wallet wallet) {
		// TODO Auto-generated method stub
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idWallet;
		if (count != null) {
			if (count <= 0) {
				idWallet = Long.valueOf(0);
			} else {
				idWallet = this.findAll().collect(Collectors.maxBy(Comparator.comparing(Wallet::getIdWallet)))
						.blockOptional().get().get().getIdWallet();
			}

		} else {
			idWallet = Long.valueOf(0);

		}
		wallet.setCreationDate(Calendar.getInstance().getTime());
		wallet.setIdWallet(idWallet + 1);
		return walletRepository.insert(wallet);
	}

	@Override
	public Mono<Wallet> update(Wallet wallet) {
		// TODO Auto-generated method stub
		wallet.setDateModified(Calendar.getInstance().getTime());
		return walletRepository.save(wallet);
	}

	@Override
	public Mono<Void> delete(Long idWallet) {
		// TODO Auto-generated method stub
		return walletRepository.deleteById(idWallet);
	}

	/**
	 * Methodo que se encarga de registrar un monedero
	 * <h2>Validaciones</h2>
	 * <ul>
	 * <li>Validar por numero telefonico</li>
	 * </ul>
	 */
	@Override
	public Mono<WalletResponse> registerWallet(Wallet wallet) {

		WalletResponse response = new WalletResponse();
		response.setMensaje(new HashMap<String, Object>());
		Wallet walletFind = new Wallet();
		walletFind.setPhone_number(wallet.getPhone_number());
		walletFind = this.walletRepository.findOne(Example.of(walletFind)).blockOptional().orElse(null);
		if (walletFind == null) {
			return this.save(wallet).map(e -> {
				CustomerWallet customerWallet = new CustomerWallet();
				customerWallet.setTypeDocument(wallet.getTypeDocument());
				customerWallet.setDocumentNumber(wallet.getDocumentNumber());
				customerWallet.setEmail_address(wallet.getEmail_address());
				customerWallet.setImeiPhone(wallet.getImeiPhone());
				customerWallet.setPhone_number(wallet.getPhone_number());
				customerWallet.setIdWallet(e.getIdWallet());
				log.info("Send kafka:" + customerTopic + " -->" + customerWallet);
				this.kafkaTemplate.send(customerTopic, customerWallet);
				response.setWallet(e);
				response.getMensaje().put("status", "success");
				return response;
			});

		} else {
			response.setWallet(walletFind);
			response.getMensaje().put("status", "error");
			response.getMensaje().put("mensaje", "El numero de telefono ingresado ya se encuentra registrado");
			return Mono.just(response);
		}

	}

	@Override
	public Mono<CardResponse> associateYourWallet(CardWallet cardWallet) {

		CardResponse cardResponse = new CardResponse();
		cardResponse.setWallet(cardWallet.getWallet());
		cardResponse.setCardNumber(cardWallet.getCard().getCardNumber());
		cardResponse.setMensaje(new HashMap<String, Object>());
		Wallet wallet = this.findById(cardWallet.getWallet().getIdWallet()).blockOptional().orElse(null);
		if (wallet != null && cardWallet.getCard().getCardNumber() != null) {
			cardResponse.getMensaje().put("status", "success");
			cardResponse.getMensaje().put("mensaje", "Procesando asociacion de tarjeta");
			log.info("Send kafka:" + cardTopic + " -->" + cardWallet);
			kafkaTemplateCard.send(cardTopic, cardWallet);
		} else {
			cardResponse.getMensaje().put("status", "error");
			if (wallet == null) {
				cardResponse.getMensaje().put("mensajeWallet", "El codigo del monedero no existe.");
			}
			if (cardWallet.getCard().getCardNumber() == null) {
				cardResponse.getMensaje().put("mensajeCardNumber", "Ingrese el nro de la tarjeta.");
			}

		}
		return Mono.just(cardResponse);
	}
}
