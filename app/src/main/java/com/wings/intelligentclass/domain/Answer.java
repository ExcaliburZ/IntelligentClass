package com.wings.intelligentclass.domain;

/**
 * Created by wing on 2017/5/18.
 * create table answer(
 * id varchar(40) primary key,
 * user_account varchar(40),
 * clazz_id varchar(40),
 * question_id varchar(40),
 * answer varchar(250) not null,
 * answer_time varchar(80),
 * constraint question_id_an foreign key(question_id) references question(id),
 * constraint user_account_an foreign key(user_account) references user(account),
 * constraint clazz_id_an foreign key(clazz_id) references clazz(id)
 * );
 */

public class Answer {
    private String id;
    private String user_account;
    private String clazz_id;
    private String question_id;
    private String answer;
    private String answer_time;
    private String user_name;
    private String user_number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getClazz_id() {
        return clazz_id;
    }

    public void setClazz_id(String clazz_id) {
        this.clazz_id = clazz_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }
}
