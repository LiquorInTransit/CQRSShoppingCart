package com.gazorpazorp.common.cart.command;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractCommand;

import lombok.Value;

@Value
public class RemoveItemFromCartCommand extends AuditableAbstractCommand {

	String productId;
	int qty;
	
	public RemoveItemFromCartCommand(String targetId, String productId, int qty, AuditEntry auditEntry) {
		super(targetId, auditEntry);
		this.productId = productId;
		this.qty = qty;
	}
}
