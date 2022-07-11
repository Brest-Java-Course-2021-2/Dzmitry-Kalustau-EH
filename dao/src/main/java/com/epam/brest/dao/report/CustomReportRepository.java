package com.epam.brest.dao.report;

import com.epam.brest.model.dto.ReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomReportRepository {

    @Autowired
    MongoOperations mongoOperation;

    public ReportDto saveOrUpdate(ReportDto reportDto) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("6291e05599f911029f33c695"));

        ReportDto reportDto1 = mongoOperation.findOne(query, ReportDto.class);
        if (reportDto1 == null) {
            reportDto1 = new ReportDto();
        }

        System.out.println("reportDto - " + reportDto1);

        //modify and update with save()
        reportDto1.setDateFrom(reportDto.getDateFrom());
        reportDto1.setDateTo(reportDto.getDateTo());
        reportDto1.setExpensesList(reportDto.getExpensesList());
        reportDto1.setTotalName(reportDto.getTotalName());
        reportDto1.setTotalExpense(reportDto.getTotalExpense());

        mongoOperation.save(reportDto1);

        //get the updated object again
        ReportDto reportDto2 = mongoOperation.findOne(query, ReportDto.class);

        System.out.println("reportDto2 - " + reportDto2);
        return reportDto2;
    }
}
