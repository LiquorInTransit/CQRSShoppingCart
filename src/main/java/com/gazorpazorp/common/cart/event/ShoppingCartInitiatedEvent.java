package com.gazorpazorp.common.cart.event;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractEvent;

import lombok.Getter;

@Getter
public class ShoppingCartInitiatedEvent extends AuditableAbstractEvent {
	
	public ShoppingCartInitiatedEvent (String aggregateIdentifier, AuditEntry auditEntry) {
		super(aggregateIdentifier, auditEntry);
	}
	
	
}
