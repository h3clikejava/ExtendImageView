# ExtendImageView
auto extand ImageView for Android

可以自动扩展的ImageView，主要用于界面跳转的时候图片自动扩展
支持任意大小任意位置的扩展
最小SDK为Android 3.1

 ![image](https://github.com/h3clikejava/ExtendImageView/blob/master/xxx.gif)
 
 Code 引入
 
 ``` compile 'h3c.extendimageview:library:0.1' ```
 
 使用示例
 
```
    iv.setPosition(location[0], location[1] - mActionBarSize - statusBarHeight); // 设定初始位置
    iv.setImageBitmap(bmp);
    iv.setDuration(5000);// 动画时长
//  iv.start(new Rect(150, 250, 750, 850));// 指定位置
    iv.start(null);// 全屏 ```
