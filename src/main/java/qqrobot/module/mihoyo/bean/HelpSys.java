package qqrobot.module.mihoyo.bean;

import java.util.List;

public class HelpSys {
    private int answer_num;
    private List<String> top_n;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public int getAnswer_num() {
        return this.answer_num;
    }

    @SuppressWarnings("all")
    public List<String> getTop_n() {
        return this.top_n;
    }

    @SuppressWarnings("all")
    public void setAnswer_num(final int answer_num) {
        this.answer_num = answer_num;
    }

    @SuppressWarnings("all")
    public void setTop_n(final List<String> top_n) {
        this.top_n = top_n;
    }
    //</editor-fold>
}
