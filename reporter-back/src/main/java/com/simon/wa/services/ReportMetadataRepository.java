package com.simon.wa.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simon.wa.domain.reports.ReportingMetadata;

public interface ReportMetadataRepository extends JpaRepository<ReportingMetadata, Long> {

	Optional<ReportingMetadata> findByReportName(String reportName);
}
