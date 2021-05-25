package com.example.weacher_app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.FileUtils;
import android.util.Xml;;

import androidx.core.app.NotificationCompat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MyApplication extends Application {
    private ArrayList<String> mListOfCities;
    private Weather mWeather;
    private String SelectedCity = "Нижний Тагил";
    final String FILE_CITY_NAME = "city_data.xml";
    private FilesUtils mFilesUtils;
    private int mNotificationId = 1;

    public ArrayList<String> getListCities() {
        return mListOfCities;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setSelectedCityFromTheList(String newCity) {
        SelectedCity = newCity;
    }

    public String getSelectedCityFromTheList() {
        return SelectedCity;
    }

    public void saveCityDom() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element cities = doc.createElement("city");
            for (int i = 0; i < mListOfCities.size(); ++i) {
                Element item = doc.createElement("iten");
                item.setTextContent(mListOfCities.get(i));
                cities.appendChild(item);
            }

            doc.appendChild(cities);
            mFilesUtils.domDocumentToFile(doc, FILE_CITY_NAME);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadCityDom() {
        Document doc = mFilesUtils.domDocFromFile(FILE_CITY_NAME);
        if (doc == null)
            return;
        NodeList city = doc.getElementsByTagName("city");
        if (city == null)
            return;
        int count = city.getLength();
        if (count == 0)
            return;
        Node cityNode = city.item(0);
        if (cityNode.getNodeType() != Node.ELEMENT_NODE)
            return;


        Element cityElement = (Element) cityNode;
        NodeList items = cityElement.getElementsByTagName("item");
        if (items == null)
            return;

        for (int i = 0; i < items.getLength(); i++) {
            Node itemNode = items.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                String cityString = itemElement.getTextContent();
                if (cityString != null)
                    mListOfCities.add(cityString);
            }
        }
    }

    public void notification(String msg) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                "weather_channel")
                .setContentTitle("Погода")
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null)
            notificationManager.notify(mNotificationId++, notificationBuilder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFilesUtils = new FilesUtils();
        mFilesUtils.setContext(this);

        loadCityDom();

        mListOfCities = new ArrayList<String>();
        mListOfCities.add("Нижний Тагил");
        mListOfCities.add("Москва");
        mListOfCities.add("Екатеринбург");
        mListOfCities.add("Казань");
        mListOfCities.add("Воронеж");
        mListOfCities.add("Новосибирск");
        mListOfCities.add("Челябинск");
        mListOfCities.add("Омск");

        mWeather = new Weather(this, SelectedCity,
                "d330221ce2dd6f070fc00118289bf0f8",
                "ru", 60000);
    }
}
