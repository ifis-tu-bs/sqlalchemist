/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tu_bs.cs.ifis.sqlgame.xmlparse;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.tu_bs.cs.ifis.sqlgame.exception.MyParserExceptionHandler;
import de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Class XMLSyntaxCheck.
 * 
 * Check the syntax of a xml-file.
 * 
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class XMLSyntaxCheck {
    
    /**
     * Config to load dynamic paths.
     */
    private final Config conf = ConfigFactory.load();
    
    /**
     * Method ckeckxml.
     * 
     * Checks if the given parameter s (e.g. tasks.xml) is a valid XML-file
     * regarding XML-schema tasks.xsd.
     * 
     * @param s name of the xml-file you want to validate (like "tasks.xml")
     * @param isFile boolean true if the String is a file name,
     *        false if the String is a file string
     * @throws de.tu_bs.cs.ifis.sqlgame.exception.MySQLAlchemistException throwing MySQLAlchemistException
     */
    public void checkxml(String s, boolean isFile) throws MySQLAlchemistException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

            factory.setSchema(schemaFactory.newSchema(
                //source of xml-schema-file
                new Source[] {new StreamSource(conf.getString("input.xmlValidatePath"))})
            );

            SAXParser parser = factory.newSAXParser();

            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(new MyParserExceptionHandler());
            if(isFile){
                reader.parse(
                    //source of xml-file
                    new InputSource(this.conf.getString("input.xmlPath") + s)
                );
            } else {
                reader.parse(
                //source of xml-file
                new InputSource(new StringReader(s))
                );
            }
            //If this line is executed, the file is valid.
            System.out.println("Datei ist valide.");
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new MySQLAlchemistException("Fehler beim checken der XML Syntax.", e);
        }
    }
}
