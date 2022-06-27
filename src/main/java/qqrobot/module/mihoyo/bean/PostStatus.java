package qqrobot.module.mihoyo.bean;

public class PostStatus {
    private boolean is_official;
    private boolean is_good;
    private boolean is_top;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public boolean is_official() {
        return this.is_official;
    }

    @SuppressWarnings("all")
    public boolean is_good() {
        return this.is_good;
    }

    @SuppressWarnings("all")
    public boolean is_top() {
        return this.is_top;
    }

    @SuppressWarnings("all")
    public void set_official(final boolean is_official) {
        this.is_official = is_official;
    }

    @SuppressWarnings("all")
    public void set_good(final boolean is_good) {
        this.is_good = is_good;
    }

    @SuppressWarnings("all")
    public void set_top(final boolean is_top) {
        this.is_top = is_top;
    }
    //</editor-fold>
}
