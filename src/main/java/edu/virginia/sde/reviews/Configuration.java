package edu.virginia.sde.reviews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class Configuration {
    public static final String configurationFilename = "config.json";

    private URL usersURL;

    private URL reviewsURL;

    private String databaseFilename;

    public Configuration() { }

    public URL getUsersURL() {
         if (usersURL == null) {
            parseJsonConfigFile();
        }
        return usersURL;
    }

    public URL getReviewsURL() {
        if (reviewsURL == null) {
            parseJsonConfigFile();
        }
        return reviewsURL;
    }

    public String getDatabaseFilename() {
        if (databaseFilename == null) {
            parseJsonConfigFile();
        }
        return databaseFilename;
    }

    /**
     * Parse the JSON file config.json to set all three of the fields:
     *  usersURL, reviewsURL, databaseFilename
     *  First two only if we think useful to be able to load in data (maybe for testing)
     */
    private void parseJsonConfigFile() {

        try (InputStream inputStream = Objects.requireNonNull(Configuration.class.getResourceAsStream(configurationFilename));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = bufferedReader.readLine();
            String jsonString = "";
            while (line != null) {
                jsonString += line;
                line = bufferedReader.readLine();
            }
            //TODO - Parse Config file (also create config file)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
