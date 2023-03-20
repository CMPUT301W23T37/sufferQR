package com.example.sufferqr;

public class user_rank {
    private String rank;
    private String username;
    private String score;

    public user_rank(String rank, String username, String score) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
