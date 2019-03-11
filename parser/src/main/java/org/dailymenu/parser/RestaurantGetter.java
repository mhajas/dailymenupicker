package org.dailymenu.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;

public abstract class RestaurantGetter implements ClassParserProvider {
    private static final long CACHE_TIMEOUT = 5 * 60 * 1000; // in milis
    protected SoftReference<String> menuHtml;
    protected Date timeOfRetrieval;

    protected abstract String getUrl();

    public String parseString() {
        try {
            if (isOldCache() || menuHtml == null || menuHtml.get() == null) {
                if (menuHtml != null) {
                    menuHtml.clear();
                }
                menuHtml = new SoftReference<>(getFreshMenuHTML());
                timeOfRetrieval = new Date();
            }
            return menuHtml.get();
        } catch (IOException e) {
            System.out.println("Not able to get FreshMenuHTML for " + getUrl() + ": " + e.getMessage());
            return "Cannot connect to the server";
        } catch (ParseException e) {
            System.out.println("Not able to parse FreshMenuHTML for " + getUrl() + ": " + e.getMessage());
            return "Cannot parse the menu";
        }
    }

    protected boolean isOldCache() {
        if (timeOfRetrieval == null) {
            return true;
        }
        Date now = new Date();
        return (now.getTime() - timeOfRetrieval.getTime() >= CACHE_TIMEOUT);
    }

    protected boolean stripImages() {
        return true;
    }

    protected String stripImages(String html) {
        return html.replaceAll("<img[^>]*>", " ");
    }

    protected String getFreshMenuHTML() throws IOException, ParseException {
        StringBuffer sb = new StringBuffer();
        URLConnection connection = getConnection();
        BufferedReader is = new BufferedReader(new InputStreamReader((InputStream) connection.getContent(), getCharset()));
        String line;
        while ((line = is.readLine()) != null) {
            sb.append(line);
        }
        return stripImages() ? stripImages(sb.toString()) : sb.toString();
    }

    protected URLConnection getConnection(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        connection.setReadTimeout(20000);
        connection.setConnectTimeout(20000);
        return connection;
    }

    protected URLConnection getConnection() throws IOException {
        return getConnection(getUrl());
    }

    protected String getCharset() {
        return "UTF-8";
    }

}
