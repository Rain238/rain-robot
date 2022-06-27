package qqrobot.module.mihoyo.bean;

import java.util.List;

public class PostResult {
    private Stat stat;
    private List<String> vod_list;
    private List<String> topics;
    private int last_modify_time;
    private boolean is_user_master;
    private String recommend_type;
    private SelfOperation self_operation;
    private Forum forum;
    private boolean is_official_master;
    private Post post;
    private boolean is_block_on;
    private User user;
    private HelpSys help_sys;
    private int vote_count;
    private List<String> image_list;
    private boolean hot_reply_exist;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public Stat getStat() {
        return this.stat;
    }

    @SuppressWarnings("all")
    public List<String> getVod_list() {
        return this.vod_list;
    }

    @SuppressWarnings("all")
    public List<String> getTopics() {
        return this.topics;
    }

    @SuppressWarnings("all")
    public int getLast_modify_time() {
        return this.last_modify_time;
    }

    @SuppressWarnings("all")
    public boolean is_user_master() {
        return this.is_user_master;
    }

    @SuppressWarnings("all")
    public String getRecommend_type() {
        return this.recommend_type;
    }

    @SuppressWarnings("all")
    public SelfOperation getSelf_operation() {
        return this.self_operation;
    }

    @SuppressWarnings("all")
    public Forum getForum() {
        return this.forum;
    }

    @SuppressWarnings("all")
    public boolean is_official_master() {
        return this.is_official_master;
    }

    @SuppressWarnings("all")
    public Post getPost() {
        return this.post;
    }

    @SuppressWarnings("all")
    public boolean is_block_on() {
        return this.is_block_on;
    }

    @SuppressWarnings("all")
    public User getUser() {
        return this.user;
    }

    @SuppressWarnings("all")
    public HelpSys getHelp_sys() {
        return this.help_sys;
    }

    @SuppressWarnings("all")
    public int getVote_count() {
        return this.vote_count;
    }

    @SuppressWarnings("all")
    public List<String> getImage_list() {
        return this.image_list;
    }

    @SuppressWarnings("all")
    public boolean isHot_reply_exist() {
        return this.hot_reply_exist;
    }

    @SuppressWarnings("all")
    public void setStat(final Stat stat) {
        this.stat = stat;
    }

    @SuppressWarnings("all")
    public void setVod_list(final List<String> vod_list) {
        this.vod_list = vod_list;
    }

    @SuppressWarnings("all")
    public void setTopics(final List<String> topics) {
        this.topics = topics;
    }

    @SuppressWarnings("all")
    public void setLast_modify_time(final int last_modify_time) {
        this.last_modify_time = last_modify_time;
    }

    @SuppressWarnings("all")
    public void set_user_master(final boolean is_user_master) {
        this.is_user_master = is_user_master;
    }

    @SuppressWarnings("all")
    public void setRecommend_type(final String recommend_type) {
        this.recommend_type = recommend_type;
    }

    @SuppressWarnings("all")
    public void setSelf_operation(final SelfOperation self_operation) {
        this.self_operation = self_operation;
    }

    @SuppressWarnings("all")
    public void setForum(final Forum forum) {
        this.forum = forum;
    }

    @SuppressWarnings("all")
    public void set_official_master(final boolean is_official_master) {
        this.is_official_master = is_official_master;
    }

    @SuppressWarnings("all")
    public void setPost(final Post post) {
        this.post = post;
    }

    @SuppressWarnings("all")
    public void set_block_on(final boolean is_block_on) {
        this.is_block_on = is_block_on;
    }

    @SuppressWarnings("all")
    public void setUser(final User user) {
        this.user = user;
    }

    @SuppressWarnings("all")
    public void setHelp_sys(final HelpSys help_sys) {
        this.help_sys = help_sys;
    }

    @SuppressWarnings("all")
    public void setVote_count(final int vote_count) {
        this.vote_count = vote_count;
    }

    @SuppressWarnings("all")
    public void setImage_list(final List<String> image_list) {
        this.image_list = image_list;
    }

    @SuppressWarnings("all")
    public void setHot_reply_exist(final boolean hot_reply_exist) {
        this.hot_reply_exist = hot_reply_exist;
    }
    //</editor-fold>
}
