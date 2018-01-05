package yuan.flood.dao.Entity;

import java.io.Serializable;

/**
 * Created by Yuan on 2017/1/5.
 */
public class User implements Serializable {
    private Long userLonID;
    private String userID;
    private String passWord;
    private String emailAddress;

    public Long getUserLonID() {
        return userLonID;
    }

    public void setUserLonID(Long userLonID) {
        this.userLonID = userLonID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
