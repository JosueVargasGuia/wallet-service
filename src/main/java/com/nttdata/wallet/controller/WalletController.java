package com.nttdata.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.model.Card;
import com.nttdata.wallet.model.CardResponse;
import com.nttdata.wallet.model.CardWallet;
import com.nttdata.wallet.model.WalletResponse;
import com.nttdata.wallet.service.WalletService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
	@Autowired
	WalletService walletService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Wallet> findAll() {
		return walletService.findAll();

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<Wallet>> save(@RequestBody Wallet wallet) {
		return walletService.save(wallet).map(_wallet -> ResponseEntity.ok().body(_wallet)).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		});
	}

	@GetMapping("/{idWallet}")
	public Mono<ResponseEntity<Wallet>> findById(@PathVariable(name = "idWallet") Long idWallet) {
		return walletService.findById(idWallet).map(wallet -> ResponseEntity.ok().body(wallet)).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PutMapping
	public Mono<ResponseEntity<Wallet>> update(@RequestBody Wallet wallet) {
		Mono<Wallet> mono = walletService.findById(wallet.getIdWallet()).flatMap(objWallet -> {
			return walletService.update(wallet);
		});
		return mono.map(_wallet -> {
			return ResponseEntity.ok().body(_wallet);
		}).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@DeleteMapping("/{idWallet}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable(name = "idWallet") Long idWallet) {
		Wallet wallet = walletService.findById(idWallet).blockOptional().orElse(null);
		if (wallet != null) {
			return walletService.delete(idWallet).map(r -> ResponseEntity.ok().<Void>build());
		} else {
			return Mono.just(ResponseEntity.noContent().build());
		}
	}

	@PostMapping("/registerWallet")
	public Mono<ResponseEntity<WalletResponse>> registerWallet(@RequestBody Wallet wallet) {
		return walletService.registerWallet(wallet).map(_wallet -> ResponseEntity.ok().body(_wallet))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}

	@PostMapping("/associateYourWallet")
	public Mono<ResponseEntity<CardResponse>> associateYourWallet(@RequestBody CardWallet cardWallet) {
		return walletService.associateYourWallet(cardWallet).map(_cardWallet -> ResponseEntity.ok().body(_cardWallet))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}
}
