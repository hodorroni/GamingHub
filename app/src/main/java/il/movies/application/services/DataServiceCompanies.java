package il.movies.application.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import il.movies.application.models.GamesArray;
import il.movies.application.models.State;

public class DataServiceCompanies {

    public interface OnAllGamesLoadedListener {
        void onAllGamesLoaded(ArrayList<State> allGames);
    }

    public static void getAllCompanies(OnAllGamesLoadedListener listener, Context context) {
        //makes a single thread to work on the background -> async task
        //when the fetcthing is over we are passing the Array to the fragment that implements that interface and overrides the onAllGamesLoaded
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                //fetching the data from the api
                ArrayList<State> allGames = fetchDataFromApi(context);
                if (listener != null) {
                    //passing the data fetched to the listener, in this case our fragment.
                    listener.onAllGamesLoaded(allGames);
                }
            }
        });
    }




    private static ArrayList<State> arrState = new ArrayList<>();

    private static ArrayList<State> fetchDataFromApi(Context context){
        arrState.clear();
        String sURL = "https://api.rawg.io/api/publishers?key=1982bdab49df44ea9d76e98db0eb3d64&page_size=140";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int i=0;
        try{
            while(i<=3){
                //converting the string to URL
                URL url = new URL(sURL);

                //connecting to the url we described above
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                //converting the long string from the website to json
                JsonParser jp = new JsonParser();

                //getting the content from (the json) and parsing it to java objects
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonElement next = root.getAsJsonObject().get("next");

                JsonElement resultsElement =root.getAsJsonObject().get("results");
                JsonArray results = resultsElement.getAsJsonArray();

                for(JsonElement je:results){
                    JsonObject obj = je.getAsJsonObject();
                    JsonElement nameE = obj.get("name");
                    JsonElement id= obj.get("id");
                    JsonElement imageE = obj.get("image_background");
                    JsonElement gamesCountE = obj.get("games_count");
                    JsonElement games = obj.get("games");
                    ArrayList<GamesArray> arrGames = new ArrayList<>();
                    if(games!=null){
                        JsonArray gamesArray = games.getAsJsonArray();
                        for(JsonElement j : gamesArray){
                            JsonObject gamesObject = j.getAsJsonObject();
                            JsonElement idGame = gamesObject.get("id");
                            JsonElement nameGameE = gamesObject.get("name");
                            int idCurrentGame = idGame.getAsInt();
                            String nameGame = nameGameE.toString().replace("\"","");
                            arrGames.add(new GamesArray(idCurrentGame,nameGame));
                        }

                        String name = nameE.toString().replace("\"","");
                        int guid = id.getAsInt();
                        String image = imageE.toString().replace("\"","");
                        int gamesCount = gamesCountE.getAsInt();
                        if(name!=null  && image!=null ){
                            arrState.add(new State(name,guid,image,arrGames,gamesCount));
                        }
                    }
                }
                if(next.equals("null")){
                    return arrState;
                }

                sURL = next.toString().replace("\"","");
                i++;
            }
        }
        catch (MalformedURLException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Error loading companies", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Error loading companies", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return arrState;
    }
}
