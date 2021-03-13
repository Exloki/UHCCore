package me.ofearr.uhccore.Enchants;

public class RomanNumeralUtil {

    public static String integerToNumeral(int level) {

        int[] intValues = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanLiterals = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

        StringBuilder numerals = new StringBuilder();

        for(int i=0; i< intValues.length; i++) {
            while(level >= intValues[i]) {
                level -= intValues[i];
                numerals.append(romanLiterals[i]);
            }
        }

        return numerals.toString();
    }
}
