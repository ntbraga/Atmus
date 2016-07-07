package atmus.atmus.adapters;

import atmus.atmus.token.Token;
import atmus.atmus.token.TokenManager;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ntbra on 03/07/2016.
 */
public class Account extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String account;
    private String key;

    public Account(){}

    public Account(String name, String account, String key){
        this.name = name;
        this.account = account;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
