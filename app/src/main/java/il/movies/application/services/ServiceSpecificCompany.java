package il.movies.application.services;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import il.movies.application.models.GamesArray;
import il.movies.application.models.SpecificCompanyModel;
import il.movies.application.models.State;

public class ServiceSpecificCompany {

    private static SpecificCompanyModel myModel = new SpecificCompanyModel();

    public static SpecificCompanyModel specificCompany(String idCompany, Context context){
        String sURL = "https://api.rawg.io/api/publishers/"+idCompany+"?key=1982bdab49df44ea9d76e98db0eb3d64";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            //converting the string to URL
            URL url = new URL(sURL);

            //connecting to the url we described above
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //converting the long string from the website to json
            JsonParser jp = new JsonParser();

            //getting the content from (the json) and parsing it to java objects
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            JsonObject resultsElement = root.getAsJsonObject();
            JsonElement nameE = resultsElement.get("name");

            JsonElement imageE = resultsElement.get("image_background");
            JsonElement descriptionE = resultsElement.get("description");
            JsonElement gameCountE = resultsElement.get("games_count");
            String name = nameE.toString().replace("\"","");
            String image = imageE.toString().replace("\"","");
            String description = descriptionE.toString().replace("\"","");
            int game_count = gameCountE.getAsInt();

            //SpecificCompanyModel newSpec = new SpecificCompanyModel(name,image,description);
            myModel.setName(name);
            myModel.setImage_background(image);
            myModel.setDescription(description);
            myModel.setGame_count(game_count);
        }
        catch (MalformedURLException e) {
            Toast.makeText(context,"Error loading description",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context,"Error loading description",Toast.LENGTH_SHORT).show();
        }
        return myModel;
    }
}
