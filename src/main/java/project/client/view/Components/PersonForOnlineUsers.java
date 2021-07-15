package project.client.view.Components;

import javafx.beans.property.SimpleStringProperty;

public class PersonForOnlineUsers {
    private SimpleStringProperty nickname = new SimpleStringProperty();

    public PersonForOnlineUsers(String nickname){
        this.nickname.set(nickname);
    }

    public String getNickname() {
        return nickname.get();
    }


    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }
}
