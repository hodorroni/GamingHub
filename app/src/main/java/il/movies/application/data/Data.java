package il.movies.application.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    public String id;
    public String email;
    public String pass;
    public String phone;

    public String name;
    public List<String> favorites;


    public Data(String id, String email, String pass, String phone, String name) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.name = name;
        this.favorites = new ArrayList<>(); // Initialize favorites with a placeholder value
    }

    //must be an empty Constructor for the firebase to store elements
    public Data(){}
}
