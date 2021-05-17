package com.epam.axon.library.events;

import lombok.Data;

@Data
public class LibraryCreatedEvent {
	//@TargetAggregateIdentifier
	private final Integer libraryId;
	private final String name;
}
