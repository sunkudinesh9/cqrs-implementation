package com.epam.axon.library.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.axon.library.aggregate.Library;
import com.epam.axon.library.queries.GetLibraryQuery;

@Service
public class LibraryProjector {
	private final Repository<Library> libraryRepository;
	@Autowired
	private final EventStore eventStore;

	public LibraryProjector(Repository<Library> libraryRepository, EventStore eventStore) {
		this.libraryRepository = libraryRepository;
		this.eventStore = eventStore;
	}

	@QueryHandler
	public Library getLibrary(GetLibraryQuery query) throws InterruptedException, ExecutionException {
		CompletableFuture<Library> future = new CompletableFuture<>();
		libraryRepository.load("" + query.getLibraryId()).execute(future::complete);
		return future.get();
	}

	public List<Object> listEventsForLibrary(String libratyId) {
		return this.eventStore.readEvents(libratyId).asStream().map(Message::getPayload).collect(Collectors.toList());
	}

}
