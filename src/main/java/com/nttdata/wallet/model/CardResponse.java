package com.nttdata.wallet.model;

import java.util.Map;

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
public class CardResponse {
	private Wallet wallet;
	private String cardNumber;
	private Map<String, Object> mensaje;
}
