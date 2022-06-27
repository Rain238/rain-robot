package qqrobot.module.mihoyo.genshin.bean;

/**
 * 查询数据库中的genshin表并封装成该类对象
 */
public class GenshinSignIn {
    private String qq;
    private String uid;
    private String cookie;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public String getQq() {
        return this.qq;
    }

    @SuppressWarnings("all")
    public String getUid() {
        return this.uid;
    }

    @SuppressWarnings("all")
    public String getCookie() {
        return this.cookie;
    }

    @SuppressWarnings("all")
    public void setQq(final String qq) {
        this.qq = qq;
    }

    @SuppressWarnings("all")
    public void setUid(final String uid) {
        this.uid = uid;
    }

    @SuppressWarnings("all")
    public void setCookie(final String cookie) {
        this.cookie = cookie;
    }
    //</editor-fold>
}
