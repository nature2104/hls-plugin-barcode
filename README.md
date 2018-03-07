# cordova-plugin-barcode 二维码扫描Cordova插件

This plugin implements barcode scanner on Cordova 4.0

## Supported Cordova Platforms

* Android 4.0.0 or above
* iOS 7.0.0 or above

## Usage
cordova plugin add https://github.com/nature2104/hls-plugin-barcode.git

## JS 
``` js
com.hls.plugins.barcode.startScan({
      message:{
        barTitle: "扫描二维码",//扫码页面标题
        explainFlag: false,
        title: "",
        tipScan: "对准发票二维码进行扫码",
        inputFlag: false,
        lightFlag: true,
        tipInput: "手工输入",
        tipLightOn: "打开照亮",
        tipLightOff: "关闭照亮",
        tipLoading: "正在处理中... ",
        tipNetworkError: "当前网络不可用, 请检查网络设置"
      },
      operationType:'SCAN',
      requests:[]
    },function(success){
      alert(JSON.stringify(success));
    }, function(error){
      alert(JSON.stringify(error));
    });;
```





