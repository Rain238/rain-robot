package qqrobot.mark;

import lombok.SneakyThrows;
import qqrobot.util.LogoFileUtils;

public class LoadMicroMark {
    @SneakyThrows
    public void logo() {
        System.out.print(new LogoFileUtils().loadLogo());
        System.out.println("v1.0.5");
        System.out.println(LogoFileUtils.tips());
    }
}
