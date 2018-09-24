# Rotate3dView
此Rotate3dView是为了WordFun移动端而准备的。

### 效果图：

![image](https://github.com/BenjaminFF/Rotate3dView/blob/master/Rotate3d.gif )

### 怎么使用：

该库是发布到JitPack上的。
首先怎么引用，参考这个链接：
[https://jitpack.io/#BenjaminFF/Rotate3dView/v1.0](https://jitpack.io/#BenjaminFF/Rotate3dView/v1.0)

为了丰富前后页的内容，和实现视图和数据分离，我**模仿了RecyclerView的Adapter实现**。所以要先创建一个Adaper,实现以下方法：

> public abstract FrontVH onCreateFrontViewHolder(ViewGroup parent);

> public abstract void onBindFrontViewHolder(FrontVH holder);

>public abstract BackVH onCreateBackViewHolder(ViewGroup parent);

>public abstract void onBindBackViewHolder(BackVH holder);

具体细节请参考这篇文章：
[https://blog.csdn.net/BenjaminFFF/article/details/82828289](https://blog.csdn.net/BenjaminFFF/article/details/82828289)


### 注意事项：
- 需要在Rotate3dView的父布局上加入android:clipChildren="false"才可以使旋转overflow。
- 可以用setDuration()设置旋转时间。



