package com.example.helpu;

public class ListViewItem {
    private int iconDrawable;
    private String contentStr;
    private String titleStr;
    private String idStr;//객체 마다 고유아이디 때문에 추가함
    private String uidStr;

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setIcon(int icon) {

        iconDrawable = icon;
    }

    public void setContent(String content) {

        contentStr = content;
    }

    public int getIcon() {

        return this.iconDrawable;
    }

    public String getContent() {

        return this.contentStr;
    }

    public String getTitle() {

        return this.titleStr;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getUidStr() {
        return uidStr;
    }

    public void setUidStr(String uidStr) {
        this.uidStr = uidStr;
    }
}
