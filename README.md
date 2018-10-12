# Rotate3dView
此Rotate3dView是为了WordFun移动端而准备的。

### 效果图：

![image](https://github.com/BenjaminFF/Rotate3dView/blob/master/Rotate3d.gif )

### 怎么使用：

该库是发布到JitPack上的。
在root build.gradle中加入maven { url 'https://jitpack.io' }。
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
然后加入依赖：
```
dependencies {
	        implementation 'com.github.BenjaminFF:Rotate3dView:1.2'
	}
```

为了丰富前后页的内容，和实现视图和数据分离，我**模仿了RecyclerView的Adapter实现**。所以要先创建一个Adaper,实现以下方法：

> public abstract FrontVH onCreateFrontViewHolder(ViewGroup parent);

> public abstract void onBindFrontViewHolder(FrontVH holder);

>public abstract BackVH onCreateBackViewHolder(ViewGroup parent);

>public abstract void onBindBackViewHolder(BackVH holder);

具体细节请参考这篇文章：
[https://blog.csdn.net/BenjaminFFF/article/details/82828289](https://blog.csdn.net/BenjaminFFF/article/details/82828289)


### 方法
- 可以用setDuration()设置旋转时间。
- 加入了onRotateEnd()监听卡片旋转结束的事件。

### 注意事项：
- 需要在Rotate3dView的父布局上加入android:clipChildren="false"才可以使旋转overflow。
- 利用requestDisallowInterceptTouchEvent()这个方法处理了与父组件的滑动冲突。
- 目前只能左右滑，上下滑动后续实现。

### LICENSE
```
MIT License

Copyright (c) 2018 BenjaminFF

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

