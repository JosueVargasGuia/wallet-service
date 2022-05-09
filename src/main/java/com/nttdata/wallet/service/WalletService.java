
package com.nttdata.wallet.service;

import com.nttdata.wallet.entity.Wallet;
import com.nttdata.wallet.model.Card;
import com.nttdata.wallet.model.CardResponse;
import com.nttdata.wallet.model.CardWallet;
import com.nttdata.wallet.model.WalletResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jvargagu Implementacion de crud para wallet
 */
public interface WalletService {
	/** Retorna todos los wallet registrados */
	Flux<Wallet> findAll();

	/** Busqueda de un wallet por idWallet */
	Mono<Wallet> findById(Long idWallet);

	/** Registra un nuevo wallet */
	Mono<Wallet> save(Wallet wallet);

	/** Actualiza un wallet */
	Mono<Wallet> update(Wallet wallet);

	/** Eliminacion fisica de un wallet */
	Mono<Void> delete(Long idWallet);

	/** Methodo que se encarga de registrar un monedero */
	Mono<WalletResponse> registerWallet(Wallet wallet);

	/** Methodo que se encarga de asociar una cuenta bancaria al monedero */
	Mono<CardResponse> associateYourWallet(CardWallet cardWallet);

}
