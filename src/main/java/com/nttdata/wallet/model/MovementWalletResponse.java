package com.nttdata.wallet.model;

import java.util.Map;

import com.nttdata.wallet.entity.TypeDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MovementWalletResponse {
	private Long idMovementWallet;
	private Long idOriginWallet;
	private String originPhoneNumber;
	private Long idDestinyWallet;
	private String destinyPhoneNumber;
	private TypeMovement typeMovement;
	private Double amount;
	private Long idCard;
	private Long originIdBankAccount;
	private Long destinyIdBankAccount;
	private Map<String, Object> mensaje;
}
