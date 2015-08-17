package models.helper;


public class Random {
    private static java.util.Random rand = new java.util.Random();

    public static int randomInt(int min, int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }
}
