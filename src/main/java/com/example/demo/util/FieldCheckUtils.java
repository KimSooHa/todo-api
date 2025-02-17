package com.example.demo.util;

import com.example.demo.exception.CustomException;

import java.util.Collection;

import static com.example.demo.exception.ErrorCode.ERROR_TASK_NULL;
import static com.example.demo.exception.ErrorCode.ERROR_TODO_OVER_LENGTH;

public class FieldCheckUtils {

    public static void paramNullCheck(Object obj) {
        // Null 체크
        if (null == obj) {
            throw new CustomException(ERROR_TASK_NULL);
        }

        // String 체크
        if (obj.getClass().equals(String.class)) {
            String str = (String) obj;
            if (str.trim().isEmpty()) {
                throw new CustomException(ERROR_TASK_NULL);
            }
        }

        // Collection 체크
        if (obj instanceof Collection && ((Collection<?>) obj).isEmpty()) {
            throw new CustomException(ERROR_TASK_NULL);
        }
    }

    public static void overWordCntCheck(String str, int len) {
        if (len < str.length()) {
            throw new CustomException(ERROR_TODO_OVER_LENGTH);
        }
    }
}
