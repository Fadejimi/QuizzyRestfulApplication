package com.quizzy.app.utils;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class RestUtility {
	public static ResponseEntity<Object> getResponseEntity(long id) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}
}
