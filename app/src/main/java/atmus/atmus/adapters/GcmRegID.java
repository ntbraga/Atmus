package atmus.atmus.adapters;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ntbra on 06/07/2016.
 */
public class GcmRegID extends RealmObject{
    @PrimaryKey
    private int id;
    private String reg_id;

    public GcmRegID(){}

    public GcmRegID(String reg_id){
        this.reg_id = reg_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }
}
