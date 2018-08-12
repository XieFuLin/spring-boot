package com.xfl.boot.common.utils;

import com.xfl.boot.bean.UserVo;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by XFL
 * time on 2018/8/12 22:47
 * description:
 */
public class ValidationUtils {
    /**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation constraint : constraintViolations) {
                sb.append(constraint.getInvalidValue()).append("值不正确，").append(constraint.getMessage()).append("；");
            }
            System.out.println(sb.toString());
//            throw new AppExpection(1001,sb.toString());
        }
    }

    public static void main(String[] args) {
        UserVo userVo = new UserVo();
        userVo.setName("5");
        userVo.setPhone("1235655");
        validate(userVo);
    }
}