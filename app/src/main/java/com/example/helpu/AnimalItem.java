package com.example.helpu;

public class AnimalItem {
    private String iconDrawable;
    private String contentStr;
    private String titleStr;
    private String idStr;//객체 마다 고유아이디 때문에 추가함
    private String uidStr; //수정 시 해당 글의 고유값이 변경되면 안되므로
    private String nameStr;

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setIcon(String icon) {

        iconDrawable = icon;
    }

    public void setContent(String content) {

        contentStr = content;
    }

    public String  getIcon() {

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

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }
}
