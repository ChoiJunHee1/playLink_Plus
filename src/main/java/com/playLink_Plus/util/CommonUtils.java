package com.playLink_Plus.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.playLink_Plus.common.Consts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Slf4j
public class CommonUtils {
    public static boolean isValidBusinessNumber(String businessNumber) {
        String refinedBusinessNumber = StringUtils.remove(businessNumber, "-");

        String[] individualNumbers = refinedBusinessNumber.split("");
        if (individualNumbers.length != 10) return false;

        int[] ints = new int[10];
        for (int i = 0; i < individualNumbers.length; i++) {
            ints[i] = Integer.valueOf(individualNumbers[i]);
        }

        int sum = 0;
        int[] indexes = new int[]{1, 3, 7, 1, 3, 7, 1, 3};
        for (int i = 0; i < 8; i++) {
            sum += ints[i] * indexes[i];
        }

        int num = ints[8] * 5;
        sum += (num / 10) + (num % 10);
        sum = 10 - (sum % 10);

        if (sum == 10) return 0 == ints[9];
        else return sum == ints[9];
    }

    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String objectToString(Object source) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(source);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static boolean isXMDService(String service) {
        return StringUtils.containsAny(service, Consts.INNER_SERVICE);
    }


}
