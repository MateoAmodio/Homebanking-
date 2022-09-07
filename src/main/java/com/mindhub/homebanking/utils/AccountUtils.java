package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.Repositories.AccountRepository;

public class AccountUtils {

    public AccountUtils() {}

    public static String getString(int min, int max, AccountRepository accountRepository) {
        int num = (int) ((Math.random() * (max - min)) + min);
        String number = "VIN-" + num;

        while(accountRepository.findByNumber(number) != null){

            num = (int) ((Math.random() * (max - min)) + min);
            number = "VIN-" + num;
        }
        return number;
    }



}
