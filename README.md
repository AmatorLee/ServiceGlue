# ServiceGlue
基于[ByteX](https://github.com/bytedance/ByteX)开发的服务发现框架

# 背景
在规模较大的项目中，组件化进程不断推进或完善，各个模块实现层不再相互依赖，访问、调用实现层参数、方法则不能直接联通，因此需要注入公共的工具类
1. 静态注入
  新建一个base模块，依赖所有接口层及实现层，然后静态注入
2. 动态注入
  利用app构建过程中，.class->dex前的transform进行字节码注入
  
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

如果项目中有使用插件开发,插件中的实现类通过`@PluginServiceImpl`注解
```
@PluginServiceImpl //插件实现类添加此注解
public class TestImpl2 implements ITestInterface2 {

    @Override
    public void log() {
        Log.e(TestImpl2.class.getSimpleName(),"try Log");
    }
}
```

### 注意：PluginServiceImpl会额外生成.class文件用于插件构建实例，宿主中慎用，防止影响包体积

在使用地方进行调用即可
```
 ServiceGlue.getService(ITestInterface1.class)
                .log();
  ```

如果某个类中大量使用某个服务，可以通过`@ServiceInject`标注为成员属性
```
    @ServiceInject
    private ITestInterface iTestInterface ;

    @ServiceInject
    private ITestInterface1 iTestInterface1 ;
```
编译过程中已自动完成赋值，直接使用即可
```
    iTestInterface.log();
 ```
 
 
### 注意：因宿主构建无法对插件的实现类进行注入，在宿主中调用插件的服务必须进行判空，防止空指针
  
  
# 优势
  1. 无需关心注入过程，使用简单
  2. 无APT，缩短编译耗时
  3. 无缝兼容插件开发
  4. 成员属性自动注入，使用便捷
  
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
2. 在Application、插件、library的buildgradle添加
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
```
last_version=0.0.3  changeLog：支持成员属性注入
last_version=0.0.2  changeLog：支持插件开发 
last_version=0.0.1  changeLog: 服务发现框架
```
      
  
# TODO
  1. ~兼容插件开发~
  2. ~成员属性注入~
  3. 增量编译
  
# 感谢
  [byteX](https://github.com/bytedance/ByteX)
