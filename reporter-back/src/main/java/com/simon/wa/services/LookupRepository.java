package com.simon.wa.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simon.wa.domain.Lookup;

public interface LookupRepository extends JpaRepository<Lookup, Long> {
	
	Optional<Lookup> findByName(String name);

}
