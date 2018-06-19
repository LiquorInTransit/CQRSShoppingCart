package com.gazorpazorp.configuration;

import java.io.Serializable;
import java.util.Arrays;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.SnapshotTrigger;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;

public class EventTypeSnapshotTriggerDefinition implements SnapshotTriggerDefinition {
	
	private final Snapshotter snapshotter;
	private final Class[] eventTypes;
	
	public EventTypeSnapshotTriggerDefinition (Snapshotter snapshotter, Class...eventTypes) {
		this.snapshotter = snapshotter;
		this.eventTypes = eventTypes;
	}

	@Override
	public SnapshotTrigger prepareTrigger(Class<?> aggregateType) {
		return new EventTypeSnapshotTrigger(snapshotter, aggregateType, eventTypes);
	}
	@Override
	public SnapshotTrigger reconfigure(Class<?> aggregateType, SnapshotTrigger trigger) {
		if (trigger instanceof EventTypeSnapshotTrigger) {
			((EventTypeSnapshotTrigger)trigger).setSnapshotter(snapshotter);
			return trigger;
		}
		return new EventTypeSnapshotTrigger(snapshotter, aggregateType, eventTypes);
	}

	private static class EventTypeSnapshotTrigger implements SnapshotTrigger, Serializable {
		private final Class<?> aggregateType;
		private final Class[] eventTypes;
		
		private transient Snapshotter snapshotter;
		
		public EventTypeSnapshotTrigger (Snapshotter snapshotter, Class<?> aggregateType, Class...eventTypes) {
			this.snapshotter = snapshotter;
			this.aggregateType = aggregateType;
			this.eventTypes = eventTypes;
		}
		
		@Override
		public void eventHandled(EventMessage<?> msg) {
			for (Class clazz : Arrays.asList(eventTypes)) {
				if (clazz.getName().equals(msg.getPayloadType().getName()) && msg instanceof DomainEventMessage) {
					if (CurrentUnitOfWork.isStarted()) {
						CurrentUnitOfWork.get().onPrepareCommit(
								u -> scheduleSnapshot((DomainEventMessage) msg));
					} else {
						scheduleSnapshot((DomainEventMessage) msg);
					}
					break;
				}
			}
		}
		
		protected void scheduleSnapshot (DomainEventMessage msg) {
			snapshotter.scheduleSnapshot(aggregateType, msg.getAggregateIdentifier());
		}
		
		@Override
		public void initializationFinished() {
		}
		
		public void setSnapshotter (Snapshotter snapshotter) {
			this.snapshotter = snapshotter;
		}
	}
}
