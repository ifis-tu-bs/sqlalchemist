package sqlgame.datageneration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import sqlgame.exception.MySQLAlchemistException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import org.fluttercode.datafactory.impl.DataFactory;

/**
 * Class GenerateSpecificData.
 *
 * Class to generate specific data for a column.
 *
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class GenerateSpecificData {

    /**
     * Config to load dynamic paths.
     */
    private final Config conf = ConfigFactory.load();

    /**
     * Constructor GenerateData.
     */
    public GenerateSpecificData() {
        //Nothing to do
    }

    /**
     * Method generateFirstName.
     *
     * Generates a random first name.
     *
     * @return String with the name
     */
    public String generateFirstName() {
        DataFactory df = new DataFactory();
        String result = df.getFirstName();
        return result;
    }

    /**
     * Method generateLastName.
     *
     * Generates a random last name.
     *
     * @return String with the name
     */
    public String generateLastName() {
        DataFactory df = new DataFactory();
        String result = df.getLastName();
        return result;
    }

    /**
     * Method generateFullName.
     *
     * Generates a random first and last name.
     *
     * @return String with the name
     */
    public String generateFullName() {
        DataFactory df = new DataFactory();
        String result = df.getFirstName() + " " + df.getLastName();
        return result;
    }

    /**
     * Method generateInteger.
     *
     * Generates a random integer.
     *
     * @return String with the integer
     */
    public String generateInteger() {
        Random r = new Random();
        String result = "" + r.nextInt();
        return result;
    }

    /**
     * Method generateIntegerPos.
     *
     * Generates a random positive integer.
     *
     * @return String with the integer
     */
    public String generateIntegerPos() {
        Random r = new Random();
        int i = r.nextInt();
        if(i < 0){
            i = i*(-1);
        }
        String result = "" + i;
        return result;
    }

    /**
     * Method generateDouble.
     *
     * Generates a random floating-point number.
     *
     * @return String with the number
     */
    public String generateDouble() {
        Random r = new Random();
        String result = "" + r.nextDouble();
        return result;
    }

    /**
     * Method generateString.
     *
     * Generates a random string.
     *
     * @param length the length of the string
     * @return String with the string
     */
    public String generateString(int length) {
        DataFactory df = new DataFactory();
        String result = df.getRandomChars(length);
        return result;
    }

    /**
     * Method generateWord.
     *
     * Generates a random word.
     *
     * @param length the length of the word
     * @return String with the word
     */
    public String generateWord(int length) {
        DataFactory df = new DataFactory();
        String result = df.getRandomWord(length);
        return result;
    }

    /**
     * Method generateText.
     *
     * Generates a random text.
     *
     * @param number the number of the word in the text
     * @return String with the text
     */
    public String generateText(int number) {
        DataFactory df = new DataFactory();
        String result = "";
        for(int i = 1; i < number; i++){
            result += df.getRandomWord() + " ";
        }
        result += df.getRandomWord();
        return result;
    }

    /**
     * Method generateDate.
     *
     * Generates a random date "yyyy-MM-dd".
     *
     * @return String with the date
     */
    public String generateDate() {
        DataFactory df = new DataFactory();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(df.getBirthDate());
        return date;
    }

    /**
     * Method generateBusinessName.
     *
     * Generates a random business name.
     *
     * @return String with the name
     */
    public String generateBusinessName() {
        DataFactory df = new DataFactory();
        String result = df.getBusinessName();
        return result;
    }

    /**
     * Method generateStreetName.
     *
     * Generates a random street name.
     *
     * @return String with the name
     */
    public String generateStreetName() {
        DataFactory df = new DataFactory();
        String result = df.getStreetName();
        return result;
    }

    /**
     * Method generateCity.
     *
     * Generates a random city name.
     *
     * @return String with the city
     */
    public String generateCity() {
        DataFactory df = new DataFactory();
        String result = df.getCity();
        return result;
    }

    /**
     * Method generateAdress.
     *
     * Generates a random adress.
     *
     * @return String with the adress
     */
    public String generateAdress() {
        DataFactory df = new DataFactory();
        String result = df.getAddress();
        return result;
    }

    /**
     * Method generateEmail.
     *
     * Generates a random email adress.
     *
     * @return String with the email
     */
    public String generateEmail() {
        DataFactory df = new DataFactory();
        String result = df.getEmailAddress();
        return result;
    }

    /**
     * Method generateBoolean
     *
     * Generates a random boolean.
     *
     * @return String with the boolean
     */
    public String generateBoolean() {
        Random r = new Random();
        int res = r.nextInt(2);
        String result = res > 0 ? "true" : "false";
        return result;
    }

    /**
     * Method generateMinInteger.
     *
     * Generates an integer greater than the parameter.
     *
     * @param min the minimum integer
     * @return String with the integer
     */
    public String generateMinInteger(int min) {
        DataFactory df = new DataFactory();
        String result = "" + df.getNumberBetween(min, 1000000);
        return result;
    }

    /**
     * Method generateMinDouble.
     *
     * Generates a double greater than the parameter.
     *
     * @param min the minimum double
     * @return String with the double
     */
    public String generateMinDouble(int min) {
        DataFactory df = new DataFactory();
        String result = "" + df.getNumberBetween(min, 1000000) + "." + df.getNumberBetween(0, 99);
        return result;
    }

    /**
     * Method generateMaxInteger.
     *
     * Generates an integer less than the parameter.
     *
     * @param max the maximum integer
     * @return String with the integer
     */
    public String generateMaxInteger(int max) {
        DataFactory df = new DataFactory();
        String result = "" + df.getNumberUpTo(max);
        return result;
    }

    /**
     * Method generateMaxDouble.
     *
     * Generates a double less than the parameter.
     *
     * @param max the maximum double
     * @return String with the double
     */
    public String generateMaxDouble(int max) {
        DataFactory df = new DataFactory();
        String result = "" + df.getNumberUpTo(max) + "." + df.getNumberBetween(0, 99);
        return result;
    }

    /**
     * Method generateBetweenInteger.
     *
     * Generates an integer between the parameter.
     *
     * @param min the minimum integer
     * @param max the maximum integer
     * @return String with the integer
     */
    public String generateBetweenInteger(int min, int max) {
        DataFactory df = new DataFactory();
        String result = "" + df.getNumberBetween(min, max);
        return result;
    }

    /**
     * Method generateBetweenDouble.
     *
     * Generates a double between the parameter.
     *
     * @param min the minimum double
     * @param max the maximum double
     * @return String with the double
     */
    public String generateBetweenDouble(int min, int max) {
        DataFactory df = new DataFactory();
        String result = "" + df.getNumberBetween(min, max-1) + "." + df.getNumberBetween(0, 99);
        return result;
    }

    /**
     * Method generateGaussInt.
     *
     * Generates an integer with the Gaussian distribution.
     *
     * @param median the median of the Gaussian distribution
     * @param sd the standard deviation	of the Gaussian distribution
     * @return String with the integer
     */
    public String generateGaussInt(double median, double sd) {
        Random r = new Random();
        double d =  median + r.nextGaussian() * sd;
        int i = (int) d;
        String result = "" + i;
        return result;
    }

    /**
     * Method generateGaussDouble.
     *
     * Generates a double with the Gaussian distribution.
     *
     * @param median the median of the Gaussian distribution
     * @param sd the standard deviation	of the Gaussian distribution
     * @return String with the double
     */
    public String generateGaussDouble(double median, double sd) {
        Random r = new Random();
        double d =  median + r.nextGaussian() * sd;
        String result = "" + d;
        return result;
    }

    /**
     * Method generateList.
     *
     * Generates an integer of the list.
     *
     * @param start the startelement
     * @return String with the number
     */
    public String generateList(int start) {
        String result = "" + start;
        return result;
    }

    /**
     * Method generateCustomData.
     *
     * Generates a random string with our txt-files.
     *
     * @param metaData the name of the txt-file
     * @param random the percentage of how often the value or the default is chosen
     * @param defaultValue the defaultvalue
     * @return String data that is created
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException exception
     *         thrown if there is an error executing the insert statement on db
     */
    public String generateCustomData(String metaData, int random, String defaultValue) throws MySQLAlchemistException {
        try {
            DataFactory df = new DataFactory();
            String path = this.conf.getString("input.dataGenPath");
            FileReader fr = new FileReader(path + metaData + ".txt");
            BufferedReader br = new BufferedReader(fr);
            String content = br.readLine();
            StringTokenizer st = new StringTokenizer(content, ";");
            ArrayList<String> values = new ArrayList();
            while (st.hasMoreTokens()) {
                values.add(st.nextToken());
            }
            String[] valuesStringArray = values.toArray(new String[values.size()]);
            String result;
            result = df.getItem(valuesStringArray, random, defaultValue);
            return result;
        } catch (IOException e) {
            throw new MySQLAlchemistException("Fehler beim Generieren", e);
        }

    }
}
