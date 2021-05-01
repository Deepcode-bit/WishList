package com.dqpi.wishList.model;

public class Sticker {
    private String name;
    private int unicode;

    public void setUnicode(int unicode) {
        this.unicode = unicode;
    }

    public Sticker(String name, int codeNum) {
        this.name = name;
        this.unicode = codeNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCode(){
        return String.valueOf(unicode);
    }

    public String getUnicode() {
        return new String(Character.toChars(unicode));
    }
}
