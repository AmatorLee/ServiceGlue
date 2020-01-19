# ServiceGlue
基于[ByteX](https://github.com/bytedance/ByteX)开发的服务发现框架

# 背景
在规模较大的项目中，组件化进程不断推进或完善，各个模块实现层不再相互依赖，访问、调用实现层参数、方法则不能直接联通，因此需要注入公共的工具类
1. 静态注入
  新建一个base模块，依赖所有接口层及实现层，然后静态注入
2. 动态注入
  利用app构建过程中，class-》dex前的transform进行字节码注入
  
ServiceGlue采用第二种方式，动态注入，构造实现类实例 

# 使用
接口层：对需要对外暴露的接口标注`@ServiceInterface`注解
```
@ServiceInterface
public interface ITestInterface {


    void log();

}
```
实现层：对实现接口层的类标注`@ServiceImpl`注解
```
@ServiceImpl
public class TestImpl implements ITestInterface {
    @Override
    public void log() {
        Log.e(TestImpl.class.getSimpleName(),"try log");
    }
}
```
如果没有对外暴露public的构造方法，如单例，则对此方法标注`@InstanceProvider`注解
```
@ServiceImpl
public class TestImpl1 implements ITestInterface1 {

    private TestImpl1() {

    }

    @InstanceProvider
    public static TestImpl1 getInstance() {
        return new TestImpl1();
    }

    @Override
    public void log() {
        Log.e(TestImpl1.class.getSimpleName(), "try log");
    }
}
```
再使用地方进行调用即可
```
 ServiceGlue.getService(ITestInterface1.class)
                .log();
  ```
  
  # 优势
  1. 无需关心注入过程，使用简单
  2. 无APT，缩短编译耗时
  
  # 在项目中引入
  1. 在Project的build.gradle文件中添加
  ```
  //maven依赖
allprojects {
    repositories {
        maven {
            url 'https://dl.bintray.com/liguangquan/maven'
        }
    }
}

dependencies {
      classpath 'me.amator.sdk:serviceGlue-plugin:${last_version}'
    }
```
2. 在Application的build.gradle、library的buildgradle添加
```
apply plugin: 'service-glue'

ServiceGlue {
    enable true
    enableInDebug true
}

dependencise{
  implementation 'me.amator.sdk:serviceGlue-plugin:${last_version}'
}


```

# 版本更新
### last_version=0.0.1  tag: 服务发现框架
      
  
  # TODO
  1. 增量编译
  2. 支持带参数构造函数
  3. 兼容插件开发
  4. 懒加载
  
  # 感谢
  [byteX](https://github.com/bytedance/ByteX)
