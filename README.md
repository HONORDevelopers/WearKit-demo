# 荣耀 Wear Kit 接入示例 Demo 代码

[![licenses Apache](https://img.shields.io/badge/license-Apache-blue)](https://gitee.com/link?target=http%3A%2F%2Fwww.apache.org%2Flicenses%2FLICENSE-2.0) [![Java Language](https://img.shields.io/badge/language-java-green.svg)](https://gitee.com/link?target=https%3A%2F%2Fwww.java.com%2Fen%2F)

## 目录
* [荣耀 Wear Kit 接入示例 Demo 代码](#荣耀-wear-kit-接入示例-demo-代码)
  * [目录](#目录)
  * [简介](#简介)
  * [环境要求](#环境要求)
  * [硬件要求](#硬件要求)
  * [开发准备](#开发准备)
  * [demo 说明](#demo 说明)
  * [安装](#安装)
  * [技术支持](#技术支持)
  * [授权许可](#授权许可)

## 简介
本示例代码中，你将使用已创建的代码工程来调用穿戴服务（Wear Kit）的接口。通过该工程，你将：

1. 了解适配 Wear Kit 具体如何配置 AndroidManifest。
2. 了解适配 Wear Kit 之后如何在手机侧给穿戴设备发送模板化通知、图片、音乐、文件，获取穿戴设备状态、佩戴用户状态，
创造更多的应用功能。

更多内容，可以登陆[荣耀开发者服务平台](https://developer.honor.com/cn/)，点击管理中心 - 开放能力 -  API库 - 穿戴服务进行了解

## 环境要求
推荐使用的安卓 targetSdk 版本为 31 及以上，JDK 版本为 1.8.211 及以上。

## 硬件要求
安装有 Windows 10/Windows 7 操作系统的计算机（台式机或者笔记本） 带USB数据线的荣耀手机，用于业务调试。

## 开发准备
1. 注册荣耀帐号，成为荣耀开发者。
2. 创建应用。
3. 构建本示例代码，需要先把它导入安卓集成开发环境（Android Studio 的版本为 2021.2.1 及以上）。
    然后从[荣耀开发者服务平台](https://developer.honor.com/cn/)获取应用ID（AppId）， 并添加到 AndroidManifest 中：

  ~~~
  <meta-data
  android:name="com.hihonor.mcs.client.appid"
  android:value="xxxx" />
  ~~~
  xxxx 为应用ID。
  另外，需要生成签名证书指纹并将证书文件添加到项目中，然后将配置添加到build.gradle。 详细信息，请参见[集成指南配置签名](https://developer.honor.com/cn/docs/11006/guides/intergrate#%E9%85%8D%E7%BD%AE%E7%AD%BE%E5%90%8D)。

## demo 说明

### 穿戴服务连接状态及版本控制接口使用

* 注册监听应用与穿戴服务的连接状态
* 取消注册应用与穿戴服务的连接状态
* 主动断开应用与穿戴服务的连接
* 获取服务端 API level
* 获取客户端 API level

您可结合[荣耀开发者平台接入指导](https://developer.honor.com/cn/docs/11006/guides/code-guide/service-connect-state) 和 demo 代码 [ClientAcitivity.java](app/src/main/java/com/hihonor/wearkitextdemo/ClientActivity.java) 来了解接口如何使用

### 用户权限接口使用

* 查询单个权限是否被用户授权
* 查询多个权限是否被用户授权
* 请求用户授权

您可结合[荣耀开发者平台接入指导](https://developer.honor.com/cn/docs/11006/guides/code-guide/request-permission) 和 demo 代码 [PermissionActivity.java](app/src/main/java/com/hihonor/wearkitextdemo/PermissionActivity.java) 来了解接口如何使用

### 设备信息接口使用

* 查询当前是否有可用穿戴设备
* 查询当前设备的可用内存
* 获取支持 Wear Kit 能力的穿戴设备列表
* 获取支持应用间消息通信能力的穿戴设备列表

您可结合[荣耀开发者平台接入指导](https://developer.honor.com/cn/docs/11006/guides/code-guide/device-info) 和 demo 代码 [DevicesActivity.java](app/src/main/java/com/hihonor/wearkitextdemo/DevicesActivity.java) 来了解接口如何使用

### 模板化通知接口使用

- 发送模板化通知

您可结合[荣耀开发者平台接入指导](https://developer.honor.com/cn/docs/11006/guides/code-guide/notify) 和 demo 代码 [NotifyActivity.java](app/src/main/java/com/hihonor/wearkitextdemo/NotifyActivity.java) 来了解接口如何使用

### 状态监听能力接口使用

* 查询穿戴设备状态
* 订阅单个状态
* 订阅多个状态
* 取消订阅状态变化

您可结合[荣耀开发者平台接入指导](https://developer.honor.com/cn/docs/11006/guides/code-guide/monitor-device-status) 和 demo代码 [MonitorActivity.java](app/src/main/java/com/hihonor/wearkitextdemo/MonitorActivity.java)来了解接口如何使用

### 设备间消息通信能力接口使用

* 手机侧应用检测穿戴设备侧应用是否安装
* 手机侧应用获取穿戴设备侧应用的版本号
* 手机侧应用检测穿戴设备侧对应的应用是否在线
* 手机侧应用发送点对点消息或文件到穿戴设备侧应用
* 取消发送文件
* 接收穿戴设备侧应用发过来的点对点消息或文件
* 取消接收穿戴设备侧应用发过来的点对点消息或文件

您可结合[荣耀开发者平台接入指导](https://developer.honor.com/cn/docs/11006/guides/code-guide/p2p) 和 demo代码 [P2pActivity.java](app/src/main/java/com/hihonor/wearkitextdemo/P2pActivity.java)来了解接口如何使用

## 安装
* 方法1：在 Android Studio 中进行代码的编译构建。构建 APK 完成后，将 APK 安装到手机上，并调试 APK。
* 方法2：在 Android Studio 中生成 APK。使用 ADB（Android Debug Bridge）工具通过
adb install {YourPath/YourApp.apk} 命令将 APK 安装到手机，并调试 APK。
（注意：因为 Wear Kit 的服务端会进行应用的签名校验，所以直接编译本 Demo 进行安装会存在签名不一致校验失败的情况）

## 技术支持
如果您对该示例代码还处于评估阶段，可在[荣耀开发者社区](https://gitee.com/link?target=https%3A%2F%2Fdeveloper.hihonor.com%2Fcn%2Fforum%2F%3Fnavation%3Ddh11614886576872095748%252F1)获取关于 Wear Kit 的最新讯息，并与其他开发者交流见解。

如果您对使用该示例代码有疑问，请尝试：

* 开发过程遇到问题上[Stack Overflow](https://gitee.com/link?target=https%3A%2F%2Fstackoverflow.com%2Fquestions%2Ftagged%2Fhonor-developer-services%3Ftab%3DVotes)，在 honor-developer-services 标签下提问，
有荣耀研发专家在线一对一解决您的问题。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://gitee.com/link?target=https%3A%2F%2Fgithub.com%2FHONORDevelopers%2FHandover-demo%2Fissues)，也欢迎您提交[Pull Request](https://gitee.com/link?target=https%3A%2F%2Fgithub.com%2FHONORDevelopers%2FHandover-demo%2Fpulls)。

## 授权许可
该示例代码经过[Apache 2.0授权许可](https://gitee.com/link?target=http%3A%2F%2Fwww.apache.org%2Flicenses%2FLICENSE-2.0)。