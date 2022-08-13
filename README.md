# Chestnut-Dream(栗梦Bot)

基于[Simple-Robot](https://github.com/ForteScarlet/simpler-robot/tree/v2-dev)机器人项目

- [栗梦手册](https://www.yuque.com/docs/share/b3b3c9c8-843f-457c-b1e7-eb89cfbb407e)
- [点击入群](https://jq.qq.com/?_wv=1027&k=y1JjrjyL)

# 环境准备

1. 熟悉`SpringBoot`框架
2. 环境需求`Java11` `MySql`

# 使用教程

1. 克隆本项目到本地`git clone https://github.com/Rain238/rain-robot.git`
2. 打开`\src\main\resources\`文件夹里面有个`application.yml`配置文件
3. `\sql`文件夹下有本项目数据库结构文件
   | 配置名 | 描述 |
   | ---- | ---- |
   | port | 项目端口 |
   | url | 数据库地址 |   
   | username | 数据库用户名 |   
   | password | 数据库密码 |   
   | bots | 机器人账号,`账号:密码`多个用`,`隔开 |   
   | protocol | 登陆协议建议使用`IPAD`协议 |

# 关于其他配置

- 配置文件`\src\main\resources\`下的`application.yml`中有写

# 更新记录

### 2022/8/13 \[v2.0.1]

- [x] 当前版本: v2.0.1
- [x] 新增进群验证功能
- [x] 新增退出机器人所在群将无法使用功能以及私聊
- [x] 退出机器人所在群/删除机器人好友将删除所有绑定数据

### 2022/7/29 \[v2.0.0]

- [x] 重构了日志打印
- [x] 将配置文件打包在Jar包外面
- [x] 抽离了某些需要定时修改的数据到配置文件内

### 2022/7/21 \[v1.0.9]

- [x] 优化自动签到
- [x] 新增将日志记录到日志文件
- [x] 增加竖屏涩图
- [x] 横屏涩图
- [x] 兽耳涩图
- [x] 星空涩图

### 2022/7/15 \[v1.0.8]

- [x] 新增原神角色信息查询
- [x] 新增原神武器信息查询

### 2022/7/8 \[v1.0.7]

- [x] 优化去水印方式
- [x] 同时支持PC和手机端抖音分享

### 2022/7/3 \[v1.0.6]

- [x] 新增抖音去水印
- [x] 修复logo显示错误
- [x] 优化整体结构待扩展更多功能

### 2022/6/26 \[v1.0.5]

- [x] 新增启动Logo
- [x] 修复了一些奇奇怪怪的小问题

### 2022/6/20 \[v1.0.4]

- [x] 优化代码逻辑
- [x] 重构了所有功能
- [x] 支持多Bot登陆
- [x] 支持在线修改推送Bot
- [x] 解决多Bot群内发送重复内容问题
- [x] 支持在线重启程序

### 2022/6/13 \[v1.0.3]

- [x] 优化代码逻辑
- [x] 修复Cookie失效不提示的情况
- [x] 添加Cookie失效自动解绑功能
- [x] 无需手动解绑直接重新绑定即可

### 2022/6/6 \[v1.0.2]

- [x] 待增加原神体力溢出提醒
- [x] 新增P站id搜索作者作品
- [x] 新增P站图片详细内容
- [x] 新增返回指定P站图片数量

### 2022/5/30 \[v1.0.1]

- [x] 优化代码逻辑
- [x] 去除随机图片
- [x] 新增原神自动签到
- [x] 新增米游社自动签到

### 2022/5/23 \[v1.0.0]

- [x] 支持群内禁言
- [x] ~~支持随机图片~~
- [x] 支持瑟图 - [Lolicon Api](https://api.lolicon.app/#/setu)
- [x] 支持自动签到
- [x] 支持原神福利签到
- [x] 支持获取每日米游币
- [x] 支持米游社社区签到
- [x] 支持米游社频道签到:崩坏二/崩坏三/原神/未定/大墅野

## 效果图
![image](https://tva1.sinaimg.cn/large/0076Bbuvly8h55aojpaxuj30hb0aota9.jpg)
![image](https://tva2.sinaimg.cn/large/0076Bbuvly8h55ap5yhqcj30q40fbjt1.jpg)
![image](https://tva4.sinaimg.cn/large/007JVHaxly8h2khrap55lj30qf0ocn07.jpg)
![image](https://tva4.sinaimg.cn/large/007JVHaxly8h2khw8htnaj30r60dgabu.jpg)
![image](https://tva2.sinaimg.cn/large/007JVHaxly8h2khxuqwmlj30ip0mtwh3.jpg)

