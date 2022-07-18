package qqrobot.mark;

import lombok.SneakyThrows;
import qqrobot.util.LogoFileUtils;

public class LoadMicroMark {
    @SneakyThrows
    public void logo() {
        final LogoFileUtils logo = new LogoFileUtils();
        System.out.print(logo.loadLogo());
        System.out.println("v1.0.8");
        System.out.println(logo.tips());
    }
}
