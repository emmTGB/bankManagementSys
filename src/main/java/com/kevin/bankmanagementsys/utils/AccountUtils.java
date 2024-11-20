package com.kevin.bankmanagementsys.utils;

public class AccountUtils {
    public static final String defaultPrefix = "123";

    public static String getMaskedAccountNumber(String accountNumber){
        return accountNumber.substring(0, 4) + "******" + accountNumber.substring(accountNumber.length() - 4);
    }

    public static String generateAccountNumber(){
        return generateAccountNumber(defaultPrefix);
    }

    public static String generateAccountNumber(String prefix){
        String accountNumber = prefix + generateRandomDigits(15);
        String checkDigit = calculateLuhnCheckDigit(accountNumber);
        return accountNumber + checkDigit;
    }

    private static String generateRandomDigits(int length){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++){
            sb.append((int)(Math.random()*10));
        }
        return sb.toString();
    }

    private static String calculateLuhnCheckDigit(String accountNumber){
        int sum = 0;
        boolean isSecond = false;

        for(int i = accountNumber.length() - 1; i >= 0; i--){
            int digit = Integer.parseInt(accountNumber.substring(i, i + 1));

            if(isSecond){
                digit *= 2;
                if(digit > 9){
                    digit -= 9;
                }
            }
            sum += digit;
            isSecond = !isSecond;
        }

        int checkDigit = sum % 10 == 0 ? 0 : 10 - (sum % 10);
        return String.valueOf(checkDigit);
    }

}
