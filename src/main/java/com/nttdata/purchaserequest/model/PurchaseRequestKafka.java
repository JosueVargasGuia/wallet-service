package com.nttdata.purchaserequest.model;
 

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
public class PurchaseRequestKafka {
	private Long idPurchaseRequest;
	private Customer customerOrigin;
	private TypeOfPayment typeOfPayment;
	private Customer customerDestiny;
	private Double amountBitcoin;
	private Double conversionRateAmount;
	private Double amountInCurrency;
	private PurchaseStatus purchaseStatus;
	private Long idWallet;
	private Long idBankAccount;
}
