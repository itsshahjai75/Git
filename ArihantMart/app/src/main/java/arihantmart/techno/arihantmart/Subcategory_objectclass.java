package arihantmart.techno.arihantmart;

/**
 * Created by Jay on 29/04/2016.
 */
public class Subcategory_objectclass


    {
        String name;
        int id;

        public Subcategory_objectclass(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}



