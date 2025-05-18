# ProtectMyApp保活应用

## 项目简介
ProtectMyApp是一个旨在提高Android工控应用在工控设备上的存活率的项目。通过一系列保活技术，帮助应用在用户未主动关闭的情况下尽可能长时间地运行用户app。
本项目专门为安卓工控设备开发，工控机需要root权限

## 项目统计

[![GitHub Stars](https://img.shields.io/github/stars/super-jarvis/ProtectMyApp?style=social)](https://github.com/super-jarvis/ProtectMyApp)
[![GitHub Forks](https://img.shields.io/github/forks/super-jarvis/ProtectMyApp?style=social)](https://github.com/super-jarvis/ProtectMyApp)

## 功能模块TODO清单

- [ ] 基础框架的搭建，权限清单以及权限申请
- [ ] 实现安卓启动器以及相关功能
- [ ] 完成前台服务模块的实现
- [ ] 实现系统事件监听和重启机制
- [ ] 优化与其他组件的协作确保主进程不被杀死
- [ ] 集成并完善WorkManager定时任务保活功能
- [ ] 开发和完善基础MQTT模块
- [ ] 设计和实现多个Webhooks功能
- [ ] 提供详细的设置界面调整保活参数，指定保活的app
- [ ] 可配置自动集成安装Rustdesk服务实现远程控制能力

## 第三方开源库清单

- [x] UtilCodex: 一个包含各种Android工具类的实用库
- [ ] Retrofit: 类型安全的HTTP客户端
- [ ] OkHttp: 一个高效的HTTP客户端
- [ ] Glide: 快速、高效的图片加载库
- [ ] Room: 持久化库，提供SQLite抽象层
- [ ] LiveData: 可观察的数据存储器
- [ ] ViewModel: 存储和管理与UI相关的数据
- [ ] Navigation Component: 简化应用内的导航逻辑
- [ ] MQTT Android Service: 提供MQTT协议支持

## 使用指南
- 启动应用后，它会在后台自动开始执行保活策略。
- 可以通过设置界面调整保活参数。
- 查看日志输出了解当前保活状态。

## 贡献指南
欢迎任何形式的贡献！

1. Fork该项目并创建你的特性分支（git checkout -b feature/yourFeature）。
2. 提交你的更改（git commit -m 'Add some feature'）。
3. 推送至分支（git push origin feature/yourFeature）。
4. 创建一个新的Pull Request。

## 许可证
本项目使用GPL3许可证。详情请查看LICENSE文件。
