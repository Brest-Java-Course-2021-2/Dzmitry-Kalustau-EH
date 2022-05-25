package com.epam.brest.dao.report;

import com.epam.brest.model.dto.ReportDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<ReportDto, String> {
}
