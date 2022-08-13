package qqrobot.module.astringent.picture.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
/**
 * &#064;Author  RainRain
 * &#064;Data  2022/5/24 15:38
 */
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
