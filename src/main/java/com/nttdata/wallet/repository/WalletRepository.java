package com.nttdata.wallet.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.wallet.entity.Wallet;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<Wallet,Long>{

}
