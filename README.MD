# ProtectMyApp 保护我的应用

## 项目简介
- ProtectMyApp是一个旨在提高Android工控应用在工控设备上的存活率的项目。通过一系列保活技术，帮助应用在用户未主动关闭的情况下尽可能长时间地运行用户app。
- 本项目专门为安卓工控设备开发，工控机需要root权限。
- 本项目使用Jetpack Compose作为UI框架，使用Kotlin作为主要编程语言，使用MVI架构设计。

## 项目统计

[![GitHub Stars](https://img.shields.io/github/stars/super-jarvis/ProtectMyApp?style=social)](https://github.com/super-jarvis/ProtectMyApp)
[![GitHub Forks](https://img.shields.io/github/forks/super-jarvis/ProtectMyApp?style=social)](https://github.com/super-jarvis/ProtectMyApp)

## 功能模块TODO清单

- [x] 基础框架的搭建，权限清单以及权限申请
- [x] 完成前台服务模块的实现，程序做成安卓启动器
- [x] 实现系统事件监听和重启机制
- [x] 优化与其他组件的协作确保主进程不被杀死
- [ ] 开发和完善基础MQTT模块
- [ ] 设计和实现多个Webhooks功能
- [ ] 提供详细的设置界面调整保活参数，指定保活的app
- [ ] 可配置自动集成安装Rustdesk服务实现远程控制能力
- [ ] 实现暂停保活功能

## 一些思路和说明
- 项目结构比较简单，使用mvi架构方式开发，一些逻辑图省事位置较随意，使用较多的object
- 想要实现多种方式的应用间通信
  - 广播机制
  - http服务机制
- 保活应用实现思路：通过监听系统时间广播android.intent.action.TIME_TICK，大概1分钟一次，收到广播后便检查当前app是否存活，如果存活则返回，如果不存活则重启app。

## 引用第三方开源库清单，感谢开源大佬

- [UtilCodex 一个包含各种Android工具类的实用库](https://github.com/Blankj/AndroidUtilCode)
- [Retrofit](https://github.com/square/retrofit)
- [OkHttp](https://github.com/square/okhttp)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [MQTT Android Service](https://github.com/eclipse/paho.mqtt.android)

## 使用指南
- 安装方式可以参考以下两种
  1. 手动下载安装应用
  2. 集成到自己的应用中，新装应用是自动下载安装本应用。
- 启动启动器，授予所需的权限。
- 应用列表勾选当前需要保活的应用。

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