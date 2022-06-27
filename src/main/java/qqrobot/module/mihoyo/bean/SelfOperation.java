package qqrobot.module.mihoyo.bean;

public class SelfOperation {
    private boolean is_collected;
    private int attitude;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public boolean is_collected() {
        return this.is_collected;
    }

    @SuppressWarnings("all")
    public int getAttitude() {
        return this.attitude;
    }

    @SuppressWarnings("all")
    public void set_collected(final boolean is_collected) {
        this.is_collected = is_collected;
    }

    @SuppressWarnings("all")
    public void setAttitude(final int attitude) {
        this.attitude = attitude;
    }
    //</editor-fold>
}
