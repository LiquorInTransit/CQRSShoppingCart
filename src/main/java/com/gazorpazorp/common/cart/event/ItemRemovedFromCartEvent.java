package com.gazorpazorp.common.cart.event;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractEvent;

import lombok.Value;

@Value
public class ItemRemovedFromCartEvent extends AuditableAbstractEvent {

	String productId;
	int qty;
	
	public ItemRemovedFromCartEvent(String aggregateIdentifier, String productId, int qty, AuditEntry auditEntry) {
		super(aggregateIdentifier, auditEntry);
		this.productId = productId;
		this.qty = qty;
	}

}
