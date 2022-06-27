package qqrobot.module.mihoyo.bean;

import java.util.Date;
import java.util.List;

public class Post {
    private int review_id;
    private List<String> images;
    private List<String> topic_ids;
    private int is_original;
    private String subject;
    private Date reply_time;
    private boolean is_interactive;
    private int view_type;
    private long created_at;
    private String content;
    private String structured_content;
    private String cover;
    private String uid;
    private int f_forum_id;
    private int is_deleted;
    private String post_id;
    private boolean is_profit;
    private PostStatus post_status;
    private int republish_authorization;
    private int max_floor;
    private List<String> structured_content_rows;
    private int game_id;
    private int view_status;
    private boolean is_in_profit;

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public int getReview_id() {
        return this.review_id;
    }

    @SuppressWarnings("all")
    public List<String> getImages() {
        return this.images;
    }

    @SuppressWarnings("all")
    public List<String> getTopic_ids() {
        return this.topic_ids;
    }

    @SuppressWarnings("all")
    public int getIs_original() {
        return this.is_original;
    }

    @SuppressWarnings("all")
    public String getSubject() {
        return this.subject;
    }

    @SuppressWarnings("all")
    public Date getReply_time() {
        return this.reply_time;
    }

    @SuppressWarnings("all")
    public boolean is_interactive() {
        return this.is_interactive;
    }

    @SuppressWarnings("all")
    public int getView_type() {
        return this.view_type;
    }

    @SuppressWarnings("all")
    public long getCreated_at() {
        return this.created_at;
    }

    @SuppressWarnings("all")
    public String getContent() {
        return this.content;
    }

    @SuppressWarnings("all")
    public String getStructured_content() {
        return this.structured_content;
    }

    @SuppressWarnings("all")
    public String getCover() {
        return this.cover;
    }

    @SuppressWarnings("all")
    public String getUid() {
        return this.uid;
    }

    @SuppressWarnings("all")
    public int getF_forum_id() {
        return this.f_forum_id;
    }

    @SuppressWarnings("all")
    public int getIs_deleted() {
        return this.is_deleted;
    }

    @SuppressWarnings("all")
    public String getPost_id() {
        return this.post_id;
    }

    @SuppressWarnings("all")
    public boolean is_profit() {
        return this.is_profit;
    }

    @SuppressWarnings("all")
    public PostStatus getPost_status() {
        return this.post_status;
    }

    @SuppressWarnings("all")
    public int getRepublish_authorization() {
        return this.republish_authorization;
    }

    @SuppressWarnings("all")
    public int getMax_floor() {
        return this.max_floor;
    }

    @SuppressWarnings("all")
    public List<String> getStructured_content_rows() {
        return this.structured_content_rows;
    }

    @SuppressWarnings("all")
    public int getGame_id() {
        return this.game_id;
    }

    @SuppressWarnings("all")
    public int getView_status() {
        return this.view_status;
    }

    @SuppressWarnings("all")
    public boolean is_in_profit() {
        return this.is_in_profit;
    }

    @SuppressWarnings("all")
    public void setReview_id(final int review_id) {
        this.review_id = review_id;
    }

    @SuppressWarnings("all")
    public void setImages(final List<String> images) {
        this.images = images;
    }

    @SuppressWarnings("all")
    public void setTopic_ids(final List<String> topic_ids) {
        this.topic_ids = topic_ids;
    }

    @SuppressWarnings("all")
    public void setIs_original(final int is_original) {
        this.is_original = is_original;
    }

    @SuppressWarnings("all")
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @SuppressWarnings("all")
    public void setReply_time(final Date reply_time) {
        this.reply_time = reply_time;
    }

    @SuppressWarnings("all")
    public void set_interactive(final boolean is_interactive) {
        this.is_interactive = is_interactive;
    }

    @SuppressWarnings("all")
    public void setView_type(final int view_type) {
        this.view_type = view_type;
    }

    @SuppressWarnings("all")
    public void setCreated_at(final long created_at) {
        this.created_at = created_at;
    }

    @SuppressWarnings("all")
    public void setContent(final String content) {
        this.content = content;
    }

    @SuppressWarnings("all")
    public void setStructured_content(final String structured_content) {
        this.structured_content = structured_content;
    }

    @SuppressWarnings("all")
    public void setCover(final String cover) {
        this.cover = cover;
    }

    @SuppressWarnings("all")
    public void setUid(final String uid) {
        this.uid = uid;
    }

    @SuppressWarnings("all")
    public void setF_forum_id(final int f_forum_id) {
        this.f_forum_id = f_forum_id;
    }

    @SuppressWarnings("all")
    public void setIs_deleted(final int is_deleted) {
        this.is_deleted = is_deleted;
    }

    @SuppressWarnings("all")
    public void setPost_id(final String post_id) {
        this.post_id = post_id;
    }

    @SuppressWarnings("all")
    public void set_profit(final boolean is_profit) {
        this.is_profit = is_profit;
    }

    @SuppressWarnings("all")
    public void setPost_status(final PostStatus post_status) {
        this.post_status = post_status;
    }

    @SuppressWarnings("all")
    public void setRepublish_authorization(final int republish_authorization) {
        this.republish_authorization = republish_authorization;
    }

    @SuppressWarnings("all")
    public void setMax_floor(final int max_floor) {
        this.max_floor = max_floor;
    }

    @SuppressWarnings("all")
    public void setStructured_content_rows(final List<String> structured_content_rows) {
        this.structured_content_rows = structured_content_rows;
    }

    @SuppressWarnings("all")
    public void setGame_id(final int game_id) {
        this.game_id = game_id;
    }

    @SuppressWarnings("all")
    public void setView_status(final int view_status) {
        this.view_status = view_status;
    }

    @SuppressWarnings("all")
    public void set_in_profit(final boolean is_in_profit) {
        this.is_in_profit = is_in_profit;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "Post(review_id=" + this.getReview_id() + ", images=" + this.getImages() + ", topic_ids=" + this.getTopic_ids() + ", is_original=" + this.getIs_original() + ", subject=" + this.getSubject() + ", reply_time=" + this.getReply_time() + ", is_interactive=" + this.is_interactive() + ", view_type=" + this.getView_type() + ", created_at=" + this.getCreated_at() + ", content=" + this.getContent() + ", structured_content=" + this.getStructured_content() + ", cover=" + this.getCover() + ", uid=" + this.getUid() + ", f_forum_id=" + this.getF_forum_id() + ", is_deleted=" + this.getIs_deleted() + ", post_id=" + this.getPost_id() + ", is_profit=" + this.is_profit() + ", post_status=" + this.getPost_status() + ", republish_authorization=" + this.getRepublish_authorization() + ", max_floor=" + this.getMax_floor() + ", structured_content_rows=" + this.getStructured_content_rows() + ", game_id=" + this.getGame_id() + ", view_status=" + this.getView_status() + ", is_in_profit=" + this.is_in_profit() + ")";
    }
    //</editor-fold>
}
