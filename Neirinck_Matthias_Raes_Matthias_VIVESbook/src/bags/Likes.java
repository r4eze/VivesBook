package bags;


import datatype.LikeType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katrien.Deleu
 */
public class Likes {
    private String accountlogin;
    private Integer postid;
    private LikeType type;

    public Likes() {
    }

    public String getAccountlogin() {
        return accountlogin;
    }

    public void setAccountlogin(String accountlogin) {
        this.accountlogin = accountlogin;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public LikeType getType() {
        return type;
    }

    public void setType(LikeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Likes{" + "accountlogin=" + accountlogin + ", postid=" + postid + ", type=" + type + '}';
    }
    
    
    
}
