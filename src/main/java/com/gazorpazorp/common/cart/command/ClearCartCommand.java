package com.gazorpazorp.common.cart.command;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractCommand;

public class ClearCartCommand extends AuditableAbstractCommand {

	public ClearCartCommand(String targetId, AuditEntry auditEntry) {
		super(targetId, auditEntry);
	}
}
