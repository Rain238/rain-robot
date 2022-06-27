package qqrobot.module.mihoyo.bean;

public class User {
    private String uid;
    private int gender;
    private String avatar_url;
    private String introduce;
    private String nickname;
    private boolean is_followed;
    private String avatar;
    private String pendant;
    private boolean is_following;
    private LevelExp level_exp;
    private Certification certification;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public String getUid() {
        return this.uid;
    }

    @SuppressWarnings("all")
    public int getGender() {
        return this.gender;
    }

    @SuppressWarnings("all")
    public String getAvatar_url() {
        return this.avatar_url;
    }

    @SuppressWarnings("all")
    public String getIntroduce() {
        return this.introduce;
    }

    @SuppressWarnings("all")
    public String getNickname() {
        return this.nickname;
    }

    @SuppressWarnings("all")
    public boolean is_followed() {
        return this.is_followed;
    }

    @SuppressWarnings("all")
    public String getAvatar() {
        return this.avatar;
    }

    @SuppressWarnings("all")
    public String getPendant() {
        return this.pendant;
    }

    @SuppressWarnings("all")
    public boolean is_following() {
        return this.is_following;
    }

    @SuppressWarnings("all")
    public LevelExp getLevel_exp() {
        return this.level_exp;
    }

    @SuppressWarnings("all")
    public Certification getCertification() {
        return this.certification;
    }

    @SuppressWarnings("all")
    public void setUid(final String uid) {
        this.uid = uid;
    }

    @SuppressWarnings("all")
    public void setGender(final int gender) {
        this.gender = gender;
    }

    @SuppressWarnings("all")
    public void setAvatar_url(final String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @SuppressWarnings("all")
    public void setIntroduce(final String introduce) {
        this.introduce = introduce;
    }

    @SuppressWarnings("all")
    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    @SuppressWarnings("all")
    public void set_followed(final boolean is_followed) {
        this.is_followed = is_followed;
    }

    @SuppressWarnings("all")
    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    @SuppressWarnings("all")
    public void setPendant(final String pendant) {
        this.pendant = pendant;
    }

    @SuppressWarnings("all")
    public void set_following(final boolean is_following) {
        this.is_following = is_following;
    }

    @SuppressWarnings("all")
    public void setLevel_exp(final LevelExp level_exp) {
        this.level_exp = level_exp;
    }

    @SuppressWarnings("all")
    public void setCertification(final Certification certification) {
        this.certification = certification;
    }
    //</editor-fold>
}
