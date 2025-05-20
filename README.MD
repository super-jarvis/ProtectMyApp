# ProtectMyApp 保护我的应用

## 项目简介

- ProtectMyApp是一个旨在提高Android工控应用在工控设备上的存活率的项目。通过一系列保活技术，帮助应用在用户未主动关闭的情况下尽可能长时间地运行用户app。
- 本项目专门为安卓工控设备开发，工控机需要root权限。
- 本项目使用Jetpack Compose作为UI框架，使用Kotlin作为主要编程语言，使用MVI架构设计，一些逻辑图省事位置较随意，直接使用object。

## 项目统计

[![GitHub Stars](https://img.shields.io/github/stars/super-jarvis/ProtectMyApp?style=social)](https://github.com/super-jarvis/ProtectMyApp)
[![GitHub Forks](https://img.shields.io/github/forks/super-jarvis/ProtectMyApp?style=social)](https://github.com/super-jarvis/ProtectMyApp)

## 功能模块TODO清单

- 主界面
    - [x] 应用列表页
    - [x] 打开应用
    - [x] 设置保活应用
    - [x] 浮动按钮组
    - [x] 启动、暂停保活
    - [x] 打开设置
- 设置界面
    - [ ] Mqtt配置，是否启用、服务器地址、端口、用户名、密码、认证方式
    - [ ] Webhooks配置，是否启用、webhook地址、测试按钮、测试日志
    - [ ] Rustdesk配置，是否启用、是否自动更新、配置自建服务器信息
- 主要功能
    - [x] 定时检测保活应用，大概1分钟一次，如果存活则返回，如果不存活则启动app
    - [x] 监听package，变更后刷新应用列表
    - [x] 下载应用并安装
    - [x] 延迟初始化
    - [x] 默认启动器判断与设置跳转
    - [ ] 自定义广播，应用间通信
        - [ ] 下载并安装app
        - [ ] 安装app
        - [ ] 卸载app
        - [ ] 设置保活应用的packageName
        - [ ] 设置保活软件的版本更新检查地址，需要返回最新的版本号和下载地址
    - [ ] http服务（需不需要再考虑一下）
    - [ ] mqtt连接
        - [ ] 连接成功后自动订阅topic,topic是设备号
        - [ ] 使用遗嘱消息，发送topic为DEVICE_OFFLINE，内容为设备号
    - [ ] rustdesk安装
        - [ ] 自动下载最新版的rustdesk，包括自动更新（在设置里配置成可选功能）
        - [ ] 自动安装并配置到指定自建服务器（前提是配置了自建服务器信息）
        - [ ] 如果安装了rustdesk，则在设备上线的相关数据推送中添加rustdesk的id
        - [ ] 配置自建服务器信息，包括服务器地址、端口、用户名、密码
        - [ ] 卸载重装rustdesk
    - [ ] webhooks推送
        - [ ] 设备启动，使用json的形式传参数，参数包括设备时间、设备编号、ip地址、mac地址、room版本、内部存储空间、外部存储空、内存空间、cpu型号、cpu核心数量
        - [ ] 保活软件进入后台或者前台
    - [ ] 自我更新功能，无感更新
    - [ ] U盘检测，在U盘指定目录下放入apk文件，自动安装或更新软件，如app目录下，如果只有一个apk默认就安装这个，如果有多个，则按照时间顺序取最新的一个，通过index.txt指定需要安装的apk

## 引用第三方库清单，感谢开源大佬

- [UtilCodex 一个包含各种Android工具类的实用库，虽然最近都不维护了，但是目前还是够用的](https://github.com/Blankj/AndroidUtilCode)
- [Retrofit](https://github.com/square/retrofit)
- [OkHttp](https://github.com/square/okhttp)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [MQTT Android Service](https://github.com/eclipse/paho.mqtt.android)
- [Fuel](https://github.com/kittinunf/fuel)
- [RustDesk](https://github.com/rustdesk/rustdesk)

## 使用指南

- 安装方式可以参考以下两种：
    1. 手动下载安装应用
    2. 集成到自己的应用中，新装应用是自动下载安装本应用
- 启动启动器，授予所需的权限
- 应用列表勾选当前需要保活的应用

## 贡献指南

欢迎任何形式的贡献，欢迎大佬们指正错误，欢迎提issue。

1. Fork该项目并创建你的特性分支（git checkout -b feature/yourFeature）。
2. 提交你的更改（git commit -m 'Add some feature'）。
3. 推送至分支（git push origin feature/yourFeature）。
4. 创建一个新的Pull Request。

## 许可证

本项目使用GPL3许可证。详情请查看LICENSE文件。

## 捐赠

如果这个项目对您有帮助，请点个⭐️鼓励一下，如果您有钞能力请考虑捐赠以支持我继续维护和开发。您的支持将鼓励我继续开发新的功能和优化代码。
![感谢您的慷慨捐助](docs/img_zfb.jpg)