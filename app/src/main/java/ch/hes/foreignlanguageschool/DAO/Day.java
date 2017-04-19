package ch.hes.foreignlanguageschool.DAO;

/**
 * Created by patrickclivaz on 11.04.17.
 */

public class Day {

    private int id;
    private String name;

    public Day(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Day() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
