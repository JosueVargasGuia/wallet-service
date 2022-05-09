package com.nttdata.wallet.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.wallet.entity.AssociatedWallet;
import com.nttdata.wallet.entity.CardType;
import com.nttdata.wallet.entity.TypeDocument;
import com.nttdata.wallet.entity.Wallet;

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
public class CustomerWallet implements Serializable{
	private Long idWallet;
	private Long idCustomer;
	private TypeDocument typeDocument;
	private String documentNumber;
	private String email_address;
	private String imeiPhone;
	private String phone_number;
	private String firstname;
	private String lastname;
}
