package com.epam.brest.dao.dto;

import com.epam.brest.dao.dto.CategoryDtoDao;
import com.epam.brest.model.dto.CategoryDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDtoDaoJdbc implements CategoryDtoDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CategoryDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<CategoryDto> findAll() {

        return null;
    }
}
