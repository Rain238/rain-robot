package qqrobot.module.mihoyo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import qqrobot.module.mihoyo.bean.PostResult;
import qqrobot.util.DateTimeUtils;
import qqrobot.util.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

/**
 * &#064;Author  Light rain
 * &#064;Date  2022/5/20 12:08
 */
public class MiHoYoSignMiHoYo extends MiHoYoAbstractSign {
    @SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MiHoYoSignMiHoYo.class);
    private MiHoYoConfig.Hub hub;
    private final String stuid;
    private final String stoken;
    private final SecureRandom random = new SecureRandom();
    //浏览帖子数
    private static final int VIEW_NUM = 10;
    //点赞帖子数
    private static final int UP_VOTE_NUM = 10;
    //分享帖子数
    private static final int SHARE_NUM = 3;
    private final CountDownLatch countDownLatch = new CountDownLatch(3);
    //线程
    private final ExecutorService pool;

    /**
     * 构造器注入参数
     *
     * @param cookie   cookie
     * @param hub      签到类型
     * @param stuid    米游社通行证id
     * @param stoken   服务器令牌
     * @param executor 线程池
     */
    public MiHoYoSignMiHoYo(String cookie, MiHoYoConfig.Hub hub, String stuid, String stoken, ThreadPoolExecutor executor) {
        //将cookie赋值给父类
        super(cookie);
        //签到执行类型
        this.hub = hub;
        //社区通行证id
        this.stuid = stuid;
        //服务器令牌
        this.stoken = stoken;
        //设置类型
        setClientType("2");
        //设置版本号
        setAppVersion("2.8.0");
        //设置设置校验码
        setSalt("dmq2p7ka6nsu0d3ev6nex4k1ndzrnfiy");
        //设置线程池
        this.pool = executor;
    }

    public Object doSign() throws Exception {
        log.info("{}社区签到任务开始", hub.getName());
        String sign = (String) sign();
        if (sign.contains("登录失效")) {
            return "米游社Cookie失效\n请重新绑定米游社";
        }
        List<PostResult> genShinHomePosts = getGenShinHomePosts();
        List<PostResult> homePosts = getPosts();
        genShinHomePosts.addAll(homePosts);
        log.info("{}获取社区帖子数: {}", hub.getName(), genShinHomePosts.size());
        //执行任务
        Future<Integer> vpf = pool.submit(createTask(this, "viewPost", VIEW_NUM, genShinHomePosts));
        Future<Integer> spf = pool.submit(createTask(this, "sharePost", SHARE_NUM, genShinHomePosts));
        Future<Integer> upf = pool.submit(createTask(this, "upVotePost", UP_VOTE_NUM, genShinHomePosts));
        //打印日志
        log.info("浏览帖子,成功: {},失败：{}", vpf.get(), VIEW_NUM - vpf.get());
        log.info("点赞帖子,成功: {},失败：{}", upf.get(), UP_VOTE_NUM - upf.get());
        log.info("分享帖子,成功: {},失败：{}", spf.get(), SHARE_NUM - spf.get());
//        pool.shutdown();  会导致阻塞
        log.info("{}社区签到任务完成", hub.getName());
        String executionTime = DateTimeUtils.convertTimest(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        String msg = "执行时间: %s\n社区签到: %s\n获取社区帖子数: %s\n浏览帖子: %s,点赞帖子: %s,分享帖子: %s\n已领取今日米游币";
        return String.format(msg, executionTime, sign, genShinHomePosts.size(), vpf.get(), upf.get(), spf.get());
    }

    public Callable<Integer> createTask(Object obj, String methodName, int num, List<PostResult> posts) {
        return () -> {
            try {
                return doTask(obj, obj.getClass().getDeclaredMethod(methodName, PostResult.class), num, posts);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return 0;
        };
    }

    public int doTask(Object obj, Method method, int num, List<PostResult> posts) {
        countDownLatch.countDown();
        int sc = 0;
        // 保证每个浏览(点赞，分享)的帖子不重复
        HashSet<Object> set = new HashSet<>(num);
        for (int i = 0; i < num; i++) {
            int index = 0;
            while (set.contains(index)) {
                index = random.nextInt(posts.size());
            }
            set.add(index);
            try {
                method.invoke(obj, posts.get(index));
                sc++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sc;
    }

    /**
     * 社区签到
     */
    public Object sign() {
        JSONObject signResult = HttpUtils.doPost(String.format(MiHoYoConfig.HUB_SIGN_URL, hub.getForumId()), getHeaders(), null);
        if ("OK".equals(signResult.get("message")) || "重复".equals(signResult.get("message"))) {
            log.info("社区签到: {}", signResult.get("message"));
        } else {
            log.info("社区签到失败: {}", signResult.get("message"));
        }
        return signResult.get("message");
    }

    /**
     * 游戏频道
     *
     * @throws Exception
     */
    public List<PostResult> getGenShinHomePosts() throws Exception {
        return getPosts(String.format(MiHoYoConfig.HUB_LIST1_URL, hub.getForumId()));
    }

    /**
     * 讨论区
     *
     * @throws Exception
     */
    public List<PostResult> getPosts() throws Exception {
        return getPosts(String.format(MiHoYoConfig.HUB_LIST2_URL, hub.getId()));
    }

    /**
     * 获取帖子
     *
     * @throws Exception
     */
    public List<PostResult> getPosts(String url) throws Exception {
        JSONObject result = HttpUtils.doGet(url, getHeaders());
        if ("OK".equals(result.get("message"))) {
            JSONArray jsonArray = result.getJSONObject("data").getJSONArray("list");
            return JSON.parseObject(JSON.toJSONString(jsonArray), new TypeReference<List<PostResult>>() {
            });
        } else {
            throw new Exception("帖子数为空，请查配置并更新！！！");
        }
    }

    /**
     * 看帖
     *
     * @param post
     */
    public boolean viewPost(PostResult post) {
        Map<String, Object> data = new HashMap<>();
        data.put("post_id", post.getPost().getPost_id());
        data.put("is_cancel", false);
        JSONObject result = HttpUtils.doGet(String.format(MiHoYoConfig.HUB_VIEW_URL, hub.getForumId()), getHeaders(), data);
        return "OK".equals(result.get("message"));
    }

    /**
     * 点赞
     *
     * @param post
     */
    public boolean upVotePost(PostResult post) {
        Map<String, Object> data = new HashMap<>();
        data.put("post_id", post.getPost().getPost_id());
        data.put("is_cancel", false);
        JSONObject result = HttpUtils.doPost(MiHoYoConfig.HUB_VOTE_URL, getHeaders(), data);
        return "OK".equals(result.get("message"));
    }

    /**
     * 分享
     *
     * @param post
     */
    public boolean sharePost(PostResult post) {
        JSONObject result = HttpUtils.doGet(String.format(MiHoYoConfig.HUB_SHARE_URL, hub.getForumId()), getHeaders());
        return "OK".equals(result.get("message"));
    }

    /**
     * 获取 stoken
     *
     * @throws URISyntaxException
     */
    public String getCookieToken() throws Exception {
        JSONObject result = HttpUtils.doGet(String.format(MiHoYoConfig.HUB_COOKIE2_URL, getCookieByName("login_ticket"), getCookieByName("account_id")), getHeaders());
        if (!"OK".equals(result.get("message"))) {
            log.info("login_ticket已失效,请重新登录获取");
            throw new Exception("login_ticket已失效,请重新登录获取");
        }
        return (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("token");
    }

    public String getCookieByName(String name) {
        String[] split = cookie.split(";");
        for (String s : split) {
            String h = s.trim();
            if (h.startsWith(name)) {
                return h.substring(h.indexOf('=') + 1);
            }
        }
        return null;
    }

    @Override
    public Header[] getHeaders() {
        return new HeaderBuilder.Builder().add("x-rpc-client_type", getClientType()).add("x-rpc-app_version", getAppVersion()).add("x-rpc-sys_version", "10").add("x-rpc-channel", "miyousheluodi").add("x-rpc-device_id", UUID.randomUUID().toString().replace("-", "").toLowerCase()).add("x-rpc-device_name", "Xiaomi Redmi Note 4").add("Referer", "https://app.mihoyo.com").add("Content-Type", "application/json").add("Host", "bbs-api.mihoyo.com").add("Connection", "Keep-Alive").add("Accept-Encoding", "gzip").add("User-Agent", "okhttp/4.8.0").add("x-rpc-device_model", "Redmi Note 4").add("isLogin", "true").add("DS", getDS()).add("cookie", "stuid=" + stuid + ";stoken=" + stoken + ";").build();
    }

    public void reSetHub(MiHoYoConfig.Hub hub) {
        this.hub = hub;
    }
}
