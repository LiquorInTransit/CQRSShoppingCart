package com.gazorpazorp.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShoppingCartRequest {
	public static enum RequestType {
		ADD,
		REMOVE,
		CLEAR
	};
	private String productId;
	private int qty;
	private RequestType type;
}
