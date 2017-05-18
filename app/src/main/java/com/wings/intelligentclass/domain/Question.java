package com.wings.intelligentclass.domain;

/**
 * Created by wing on 2017/5/18.
 */
/*
* create table question(
 id varchar(40) primary key,
 title varchar(160) not null,
 create_time varchar(80),
 content varchar(250),
 clazz_id varchar(40),
 constraint clazz_que_FK foreign key(clazz_id) references clazz(id)
 );
*
* */
public class Question {
    private String id;
    private String title;
    private String create_time;
    private String content;
    private String clazz_id;

    public Question(String title, String content, String clazz_id, String create_time) {
        this.title = title;
        this.create_time = create_time;
        this.content = content;
        this.clazz_id = clazz_id;
    }

    public Question() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClazz_id() {
        return clazz_id;
    }

    public void setClazz_id(String clazz_id) {
        this.clazz_id = clazz_id;
    }
}
