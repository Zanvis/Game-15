package vistula.ap.l10_piwowarski_62024_game15;

import java.util.Random;

public class ArrayPermuatation {
    public static int[] shuffleFisherYeats(int[] oldArray) {
        Random rndap = new Random();
        int cap;
        int[] newArrayap = oldArray;
        for (int j, i = oldArray.length-1; i>0; i--){
            j = rndap.nextInt(i+1);
            cap = newArrayap[j];
            newArrayap[j] = newArrayap[i];
            newArrayap[i] = cap;
        }
        return newArrayap;
    }
}
