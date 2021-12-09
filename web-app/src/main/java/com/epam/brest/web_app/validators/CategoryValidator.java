package com.epam.brest.web_app.validators;

import com.epam.brest.model.Category;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;

@Component
public class CategoryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "categoryName", "categoryName.empty");
        Category category = (Category) target;

        if (StringUtils.hasLength(category.getCategoryName())
                && category.getCategoryName().length() > CATEGORY_NAME_SIZE) {
            errors.rejectValue("categoryName", "categoryName.maxSize");
        }
    }
}
