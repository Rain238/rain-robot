package qqrobot.module.mihoyo.genshin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import qqrobot.module.mihoyo.MiHoYoAbstractSign;
import qqrobot.module.mihoyo.MiHoYoConfig;
import qqrobot.module.mihoyo.genshin.bean.Award;
import qqrobot.util.HttpUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原神签到实现类
 *
 * &#064;Author  Light rain
 * &#064;Date  2022/5/20 2:34
 */
public class GenShinSignMiHoYo extends MiHoYoAbstractSign {
    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GenShinSignMiHoYo.class);
    //</editor-fold>

    public GenShinSignMiHoYo(String cookie, String uid) {
        //将cookie/uid赋值给父类
        super(cookie, uid);
        //设置类型
        setClientType("5");
        //设置版本号
        setAppVersion("2.3.0");
        //设置校验码
        setSalt("h8w582wxwgqvahcdkpvdhbh2w9casgfl");
        //设置服务器id
        setRegion(getRegion());
    }

    /**
     * 米游社原神福利签到
     *
     * @return message
     */
    public Object sign() {
        Map<String, Object> data = new HashMap<>();
        data.put("act_id", MiHoYoConfig.ACT_ID);
        data.put("region", this.region);
        data.put("uid", this.uid);
        JSONObject signResult = HttpUtils.doPost(MiHoYoConfig.SIGN_URL, getHeaders(), data);
        if (signResult.getInteger("retcode") == 0) {
            log.info("原神签到福利成功：{}", signResult.get("message"));
        } else {
            log.info("原神签到福利签到失败：{}", signResult.get("message"));
        }
        return signResult.get("message");
    }

    /**
     * 获取uid和昵称
     *
     * @return nickname
     */
    public String getName() {
        try {
            JSONObject result = HttpUtils.doGet(MiHoYoConfig.ROLE_URL, getBasicHeaders());
            String uid = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("game_uid");
            String nickname = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("nickname");
            log.info("获取用户UID：{}", uid);
            log.info("当前用户名称：{}", nickname);
            return nickname;
        } catch (Exception e) {
            return "";
        }
    }

    //获取原神服务器id 官服：cn_gf01天空岛/B服：cn_qd01世界树
    public String getRegion() {
        try {
            JSONObject result = HttpUtils.doGet(MiHoYoConfig.ROLE_URL, getBasicHeaders());
            return (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("region");
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * 获取今天奖励详情
     *
     * @param day 天数
     * @return List<Award>
     */
    public Award getAwardInfo(int day) {
        JSONObject awardResult = HttpUtils.doGet(MiHoYoConfig.AWARD_URL, getHeaders());
        JSONArray jsonArray = awardResult.getJSONObject("data").getJSONArray("awards");
        List<Award> awards = JSON.parseObject(JSON.toJSONString(jsonArray), new TypeReference<>() {
        });
        return awards.get(day - 1);
    }

    /**
     * 社区签到并查询当天奖励
     *
     * @return %s月已签到%s天
     * 已获取%s%s
     */
    public String hubSign() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("act_id", MiHoYoConfig.ACT_ID);
            data.put("region", this.region);
            data.put("uid", this.uid);
            JSONObject signInfoResult = HttpUtils.doGet(MiHoYoConfig.INFO_URL, getHeaders(), data);
            LocalDateTime time = LocalDateTime.now();
            Boolean isSign = signInfoResult.getJSONObject("data").getBoolean("is_sign");
            Integer totalSignDay = signInfoResult.getJSONObject("data").getInteger("total_sign_day");
            int day = isSign ? totalSignDay : totalSignDay + 1;
            Award award = getAwardInfo(day);
            log.info("{}月已签到{}天", time.getMonth().getValue(), totalSignDay);
            log.info("{}签到获取{}{}", signInfoResult.getJSONObject("data").get("today"), award.getCnt(), award.getName());
            return String.format("%s月已签到%s天\n已获取%s%s", time.getMonth().getValue(), totalSignDay, award.getCnt(), award.getName());
        } catch (Exception e) {
            return "";
        }
    }

}

