package at.haraldbernhard.joggingcoachandroid;

/**
 * Created by Harald Bernhard on 28.06.2016.
 */
public class User {

    String username;
    int age, size, weight;

    public User (String username, int age, int size, int weight){
        this.username = username;
        this.size = size;
        this.age = age;
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public int getSize() {
        return size;
    }

    public int getWeight() {
        return weight;
    }
}
