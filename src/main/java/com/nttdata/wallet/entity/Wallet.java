package com.nttdata.wallet.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Document(collection = "wallet")
public class Wallet {
	@Id
	private Long idWallet;
	private Long idCustomer;
	private String firstname;
	private String lastname;
	private TypeDocument typeDocument;
	private String documentNumber;
	private String imeiPhone;
	private String phone_number;
	private String email_address;
	private String associatedWalletMessage;
	private AssociatedWallet associatedWallet;
	private Long idCard;
	private String cardNumber;
	private CardType cardType;
	private Long idBankAccount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date creationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dateModified;
}
