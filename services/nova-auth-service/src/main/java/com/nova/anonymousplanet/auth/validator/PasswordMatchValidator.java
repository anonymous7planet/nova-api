package com.nova.anonymousplanet.auth.validator;

import com.nova.anonymousplanet.auth.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.common.validator
 * fileName : PasswordMatchValidator
 * author : Jinhong Min
 * date : 2025-04-30
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-04-30      Jinhong Min      최초 생성
 * ==============================================
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            // Reflection으로 필드 접근
            Field passwordField = value.getClass().getDeclaredField("password");
            Field confirmField = value.getClass().getDeclaredField("passwordConfirm");

            passwordField.setAccessible(true);
            confirmField.setAccessible(true);

            String password = (String) passwordField.get(value);
            String confirm = (String) confirmField.get(value);

            boolean matched = password != null && password.equals(confirm);
            if (!matched) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
                        .addPropertyNode("passwordConfirm")
                        .addConstraintViolation();
            }

            return matched;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}