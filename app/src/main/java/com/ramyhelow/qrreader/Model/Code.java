package com.ramyhelow.qrreader.Model;

public class Code {
    public static final String DB_NAME = "CODE_DB";
    public static final String TABLE_NAME = " CODE_TABLE ";
    public static final String COL_ID = "CODE_ID";
    public static final String COL_TIME = "CODE_TIME";
    public static final String COL_TEXT = "CODE_TEXT";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TIME + " TEXT, " + COL_TEXT + " TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;


    private String id;
    private String time;
    private String text;

    public Code() {
    }

    public Code(String time, String text) {
        this.time = time;
        this.text = text;
    }

    public Code(String id, String time, String text) {
        this.id = id;
        this.time = time;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
