package com.example.weacher_app;

import android.content.Context;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FilesUtils {
    private Context mContext;

    void setContext(Context newContext){
        mContext = newContext;
    }

    /**
     * Сохранить докумнт в файл
     * @param doc - Дом документ
     * @param fileName - имя файла
     */
    void domDocumentToFile(Document doc, String fileName){
        try{
            FileOutputStream file = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(file));
        }
        catch(TransformerException | FileNotFoundException exception){
            exception.printStackTrace();
        }
    }

    /**
     * Создать XML-докумнт из файла
     * @param fileName - имя файла
     * @return - документ ДОМ
     */
    Document domDocFromFile(String fileName){
        try{
            InputStream file = mContext.openFileInput(fileName);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void textToFile(String text, String fileName) {
        try{
            FileOutputStream outputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);

            byte[] bytes = text.getBytes();

            outputStream.write(bytes);
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    XmlPullParser saxFromFile(String fileName){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            XmlPullParser parser = factory.newPullParser();

            InputStream file = mContext.openFileInput(fileName);

            InputStreamReader reader = new InputStreamReader(file);

            parser.setInput(reader);
            return parser;
        }
        catch (XmlPullParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
