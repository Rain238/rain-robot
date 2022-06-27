package qqrobot.module.astringent.picture.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class Pixiv {
    private int pid;
    private int p;
    private int uid;
    private String title;
    private String author;
    private boolean r18;
    private int width;
    private int height;
    private String[] tags;
    private String ext;
    private String uploadDate;
    private Map<String, String> urls;
}
