package com.nttdata.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.nttdata.wallet.controller.WalletController;
import com.nttdata.wallet.entity.AssociatedWallet;
import com.nttdata.wallet.entity.CardType;
import com.nttdata.wallet.entity.TypeDocument;
import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.service.WalletService;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Log4j2
@ExtendWith(SpringExtension.class)
@WebFluxTest(WalletController.class)
class WalletServiceApplicationTests {
	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private WalletService walletService;

	@Test
	void saveControllerTest() {
		Wallet nWallet = new Wallet(Long.valueOf(1), Long.valueOf(5), null, null, TypeDocument.dni, null, null,
				"941451130", null, "Cartera asociada a la cuenta Nro:4840-3484-5876-3756",
				AssociatedWallet.AssociatedCard, null, null, CardType.debitCard, Long.valueOf(3), null, null);
		var walletMono = Mono.just(nWallet);
		when(walletService.save(nWallet)).thenReturn(walletMono);
		webTestClient.post().uri("/api/v1/wallet").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(walletMono, Wallet.class).exchange().expectStatus()
				.isCreated();
	}

	@Test
	void findAllControllerTest() {
		Wallet nWallet1 = new Wallet(Long.valueOf(1), Long.valueOf(5), null, null, TypeDocument.dni, null, null,
				"941451130", null, "Cartera asociada a la cuenta Nro:4840-3484-5876-3756",
				AssociatedWallet.AssociatedCard, null, null, CardType.debitCard, Long.valueOf(3), null, null);
		Wallet nWallet2 = new Wallet(Long.valueOf(2), Long.valueOf(5), null, null, TypeDocument.dni, null, null,
				"941451130", null, "Cartera asociada a la cuenta Nro:4840-3484-5876-3756",
				AssociatedWallet.AssociatedCard, null, null, CardType.debitCard, Long.valueOf(3), null, null);

		var walletFlux = Flux.just(nWallet1, nWallet2);
		when(walletService.findAll()).thenReturn(walletFlux);
		nWallet1.setIdCard(Long.valueOf(3));
		var responseBody = webTestClient.get().uri("/api/v1/wallet").exchange().expectStatus().isOk()
				.returnResult(Wallet.class).getResponseBody();
		StepVerifier.create(responseBody).expectSubscription().expectNext(nWallet1).expectNext(nWallet2)
				.verifyComplete();

		log.info(responseBody);
	}

	@Test
	void findByIdControllerTest() {
		Wallet nWallet = new Wallet(Long.valueOf(1), Long.valueOf(5), null, null, TypeDocument.dni, null, null,
				"941451130", null, "Cartera asociada a la cuenta Nro:4840-3484-5876-3756",
				AssociatedWallet.AssociatedCard, null, null, CardType.debitCard, Long.valueOf(3), null, null);
		var walletMono = Mono.just(nWallet);
		when(walletService.findById(Long.valueOf(1))).thenReturn(walletMono);
		var responseBody = webTestClient.get().uri("/api/v1/wallet/1").exchange().expectStatus().isOk()
				.returnResult(Wallet.class).getResponseBody();
		StepVerifier.create(responseBody).expectSubscription().expectNext(nWallet).verifyComplete();
	}

	/**
	 * 
	 */
	@Test
	void deleteControllerTest() {
		Wallet nWallet = new Wallet(Long.valueOf(1), Long.valueOf(5), null, null, TypeDocument.dni, null, null,
				"941451130", null, "Cartera asociada a la cuenta Nro:4840-3484-5876-3756",
				AssociatedWallet.AssociatedCard, null, null, CardType.debitCard, Long.valueOf(3), null, null);
		var walletMono = Mono.just(nWallet);
		 when(walletService.findById(Long.valueOf(1))).thenReturn(walletMono);
		given(walletService.delete(Long.valueOf(1))).willReturn(Mono.empty());
		webTestClient.delete().uri("/api/v1/wallet/1").exchange().expectStatus().isNotFound();
	}
}
