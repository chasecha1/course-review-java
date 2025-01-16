package edu.virginia.sde.reviews;

public class User {

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //TODO: getter
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public boolean equals(Object o){
        User otherUser = (User) o;
        if(this.username.equals(otherUser.getUsername()) &&
                this.password.equals(otherUser.getPassword())){
            return true;
        }
        return false;
    }
}
