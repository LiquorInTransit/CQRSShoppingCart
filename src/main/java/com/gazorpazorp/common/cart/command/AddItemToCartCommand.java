package com.gazorpazorp.common.cart.command;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractCommand;

import lombok.Value;

@Value
public class AddItemToCartCommand extends AuditableAbstractCommand {
	
	String productId;
	int qty;

	public AddItemToCartCommand(String targetId, String productid, int qty, AuditEntry auditEntry) {
		super(targetId, auditEntry);
		this.productId = productid;
		this.qty = qty;
	}
}
