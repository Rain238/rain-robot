package qqrobot.module.mihoyo.genshin.bean;

import lombok.*;
//生成空参构造方法
//生成带参构造方法
public class Award {
    private String icon;
    private String name;
    private Integer cnt;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public String getIcon() {
        return this.icon;
    }

    @SuppressWarnings("all")
    public String getName() {
        return this.name;
    }

    @SuppressWarnings("all")
    public Integer getCnt() {
        return this.cnt;
    }

    @SuppressWarnings("all")
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    @SuppressWarnings("all")
    public void setName(final String name) {
        this.name = name;
    }

    @SuppressWarnings("all")
    public void setCnt(final Integer cnt) {
        this.cnt = cnt;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "Award(icon=" + this.getIcon() + ", name=" + this.getName() + ", cnt=" + this.getCnt() + ")";
    }

    @SuppressWarnings("all")
    public Award() {
    }

    @SuppressWarnings("all")
    public Award(final String icon, final String name, final Integer cnt) {
        this.icon = icon;
        this.name = name;
        this.cnt = cnt;
    }
    //</editor-fold>
}
