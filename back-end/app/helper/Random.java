package helper;


public class Random {
    private static final java.util.Random rand = new java.util.Random();

    public static int randomInt(int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max) + 1);
    }
}
