package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.Repositories.CardRepository;

public class CardUtils {

    public static int extracted(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String zeroAdder(int num) {

        String result;
        if (num > 999){result = "" + num; return result;}
        if (num < 10){result = "000" + num; return result;}
        if (num < 100){result = "00" + num; return result;}
        if (num < 1000){result = "0" + num; return result;}

        return "0000";
    }

    public static String getString(CardRepository cardRepository) {
        String number = "";
        int x = 0;

        for (int i = 0;i<4; i++){
            if(i == 3) {x = 1;}
            number = number + zeroAdder(extracted(x, 9999));
            if(i != 3) {number = number + "-"; }
        }

        x = 0;

        while(cardRepository.findByNumber(number).orElse(null) != null){
            for (int i = 0;i<4; i++){
                if(i == 3) {x = 1;}
                number = number + zeroAdder(extracted(x, 9999));
                if(i != 3) {number = number + "-"; }
            }
        }
        return number;
    }

}
