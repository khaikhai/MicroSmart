package com.microsmart.tv.util;

import java.text.DecimalFormat;

public class Formant {
    public static String decimal(int num) {
        DecimalFormat df = new DecimalFormat("000000");
        return df.format(num);
    }
}
