package qqrobot.mark;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import qqrobot.util.LogoFileUtils;
@Component
public class LoadMicroMark {
    @SneakyThrows
    public void logo() {
        System.out.print(new LogoFileUtils().loadLogo());
        System.out.println(new LogoFileUtils().tips());
        System.out.println("ChestnutDreamBot: v2.0.1");
    }
}
