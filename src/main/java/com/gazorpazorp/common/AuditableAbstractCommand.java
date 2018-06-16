package com.gazorpazorp.common;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AuditableAbstractCommand {

	@TargetAggregateIdentifier
	private String targetId;
	private AuditEntry auditEntry;
	
}
