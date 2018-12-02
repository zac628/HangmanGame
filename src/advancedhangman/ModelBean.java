import java.io.Serializable;

public class ModelBean implements Serializable {

    private DatabaseBean db;

    public ModelBean(){
        db = new DatabaseBean();
    }

}
