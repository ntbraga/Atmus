/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmus.atmus.token;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 *
 * @author ntbra
 */
public interface Token{
    public String genToken();
    public String getSmallToken();
    public long getTimeWindow();
    public void setTimeWindow(long timeWindow);
    public int getReaming();
    public String getTokenKey();
}
