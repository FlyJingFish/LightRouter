# LightRouter

[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/network/members)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/blob/master/LICENSE)


#### 当前项目是结合和 [AndroidAOP](https://github.com/FlyJingFish/AndroidAOP) 和 [ModuleCommunication](https://github.com/FlyJingFish/ModuleCommunication)来使用的 ，本文不具体介绍这两个框架其他的用法，旨在介绍如何利用这两个框架配置出 类似于 ARouter 等框架的用法，如有需要点击下方链接可查看

- [AndroidAOP 面向切面编程](https://github.com/FlyJingFish/AndroidAOP)
- [ModuleCommunication 模块通信](https://github.com/FlyJingFish/ModuleCommunication)

#### 这两者结合使用，可具备 Router 的以下几个功能

- 基础的 Activity 跳转，Fragment 的获取
- 跳转页面中间的拦截器
- 解耦 各个 module 的 `伪Application` 
- 暴露服务

## 使用步骤

**在开始之前可以给项目一个Star吗？非常感谢，你的支持是我唯一的动力。欢迎Star和Issues!**

### 一、引入插件

1、在 **项目根目录** 的 ```build.gradle``` 里依赖插件

```gradle
buildscript {
    dependencies {
        //必须项 👇
        classpath 'io.github.FlyJingFish.ModuleCommunication:module-communication-plugin:1.1.6'
    }
}
plugins {
    //必须项 👇下边版本号根据你项目的 Kotlin 版本决定👇
    id 'com.google.devtools.ksp' version '1.8.10-1.0.9' apply false
    //必须项 👇需要配合 AndroidAOP 使用
    id "io.github.FlyJingFish.AndroidAop.android-aop" version "1.8.2" apply true
}
```

[Kotlin 和 KSP Github 的匹配版本号列表](https://github.com/google/ksp/releases)


2、在 **app** 的 ```build.gradle``` 添加

```gradle
//必须项 👇
plugins {
    ...
    id 'android.aop'//最好放在最后一行
}
```

3、在**根目录**的 `gradle.properties` 添加如下设置

> 对于当前路由项目来说 `debugMode` 就够用了,debugMode 打包速度快，如果您还用到 AndroidAOP 的其他功能，[点此看具体用法](https://github.com/FlyJingFish/AndroidAOP)

```
androidAop.debugMode=true 
```

4、在 **使用路由的module** 的 ```build.gradle``` 里依赖插件

```gradle
plugins {
    id 'communication.export'
}
```

5、引入依赖库

```gradle
dependencies {
    //必须项 👇AndroidAOP 提供支持
    implementation 'io.github.FlyJingFish.AndroidAop:android-aop-core:1.8.2'
    implementation 'io.github.FlyJingFish.AndroidAop:android-aop-annotation:1.8.2'
    
    //使用路径的方式跳转则必须添加（使用通信module的则不加也可以）
    implementation 'io.github.FlyJingFish.ModuleCommunication:module-communication-route:1.1.6'
    
    //这一项在你配置 communication.export 时就已经自动引入，如没有配置则需引入
    implementation 'io.github.FlyJingFish.ModuleCommunication:module-communication-annotation:1.1.6'
    
    //使用拦截器（可选项）
    implementation 'io.github.FlyJingFish.ModuleCommunication:module-communication-intercept:1.1.6'
}
```

### 开始使用

路由主要用到下边两个注解

- @Route 路由页面

- @RouteParams 路由页面参数

#### 示例

- 一、配置页面

```kotlin
// activity 
@Route("user/UserActivity")
class UserActivity : AppCompatActivity() {

    @delegate:RouteParams("params1")
    val params1 :String ? by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra("params1")
    }

    @delegate:RouteParams("params2")
    val params2 :User ? by lazy(LazyThreadSafetyMode.NONE) {
        intent.getSerializableExtra("params2") as User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        Log.e("UserActivity","params1=$params1,params2=$params2")
    }
}

//fragment
@Route("user/UserFragment")
class UserFragment : Fragment() {
    @delegate:RouteParams("params1")
    val params1 :String ? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString("params1")
    }

    @delegate:RouteParams("params2")
    val params2 :User ? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getSerializable("params2") as User
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActivityUserBinding.inflate(inflater,container,false)
        Log.e("UserFragment","params1=$params1,params2=$params2")
        return binding.root
    }

}
```

- 在其他 module 调用

跳转 Activity 

```kotlin
//在 module-communication-route 可以使用路径跳转
ModuleRoute.builder("user/UserActivity")
    .putValue("params1","lalla")
    .putValue("params2",user)
    .go(this)

//在自己的 module 下也可使用帮助类，帮助类也可跨模块调用需要使用 ModuleCommunication 的通信功能
`LibUser$$Router`.goUser_UserActivity(this,"hahah",user)

```

获取 Fragment

```kotlin
//在 module-communication-route 可以使用路径拿到 class ，反射新建fragment对象
val clazz = ModuleRoute.builder("user/UserFragment")
    .getClassByPath()
val fragment : Fragment = clazz?.getDeclaredConstructor()?.newInstance() as Fragment

//在自己的 module 下也可使用帮助类，帮助类也可跨模块调用需要使用 ModuleCommunication 的通信功能
val fragment : Fragment = `LibUser$$Router`.newInstanceForUser_UserFragment("lalala",user) as Fragment

```

**按模块去加载页面，减少初始化**

```kotlin
// builder 第一个参数传 module 名，这个名来自于帮助类 `LibUser$$Router` 的前半截 
ModuleRoute.builder("LibUser","user/UserActivity")
    .putValue("params1","lalla")
    .putValue("params2",user)
    .go(this)

```

- 想要让 `ModuleRoute` 起作用，还需配置以下信息

```kotlin

object CollectApp {
    private val allRouteClazz = mutableSetOf<BaseRouterClass>()

    /**
     * 这一步才可以收集到所有的路由路径信息
     */

    @AndroidAopCollectMethod
    @JvmStatic
    fun collectRouterClass(sub: BaseRouterClass){
        Log.e("CollectIntercept","collectRouterClass=$sub")
        allRouteClazz.add(sub)
    }


    fun onCreate(application: Application){
        Log.e("CollectIntercept","getAllRouterIntercept-size=${allRouterIntercept.size}")
        //设置全部路由路径信息，这样ModuleRoute才可以起作用
        ModuleRoute.autoAddAllRouteClass(allRouteClazz)
    }
}
//初始化
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //一键初始化所有需要的信息
        CollectApp.onCreate(this)
    }
}

```

#### 二、拦截器的使用

- 定义拦截器
```kotlin

class UserIntercept : RouterIntercept {
    //跳转页面时会先进入这里
    override fun onIntercept(proceed: Proceed) {
        Log.e("onIntercept","--UserIntercept--${proceed.path},params = ${proceed.paramsMap},byPath = ${proceed.byPath}")
        proceed.proceed()
    }
    //返回序号，存在多个拦截器时会按照这个顺序依次进入拦截器
    override fun order(): Int {
        return 2
    }
}
```
- 初始化拦截器
```kotlin
object CollectApp {
    private val allRouterIntercept = mutableSetOf<RouterIntercept>()

    /**
     * 这一步才可以收集到所有的拦截器
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectIntercept(sub: RouterIntercept){
        Log.e("CollectIntercept","collectIntercept=$sub")
        allRouterIntercept.add(sub)
    }

    fun onCreate(application: Application){
        Log.e("CollectIntercept","getAllRouterIntercept-size=${allRouterIntercept.size}")
        //设置全部的拦截器让其起作用
        RouterInterceptManager.addAllIntercept(allRouterIntercept)
    }
}
//初始化
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //一键初始化所有需要的信息
        CollectApp.onCreate(this)
    }
}

```

#### 三、为每个 module 配置 `伪Application`

```kotlin
// 自己定义一个 IApplication 
interface IApplication {
    fun onCreate(application: Application)
}
// 在各自 module 下实现这个接口
class UserApplication:IApplication {
    override fun onCreate(application: Application) {
        Log.e("IApplication",""+UserApplication::class.java.name)
    }
}
object CollectApp {
    private val allIApplication = mutableSetOf<IApplication>()

    /**
     * 收集所有的 module 的 IApplication 类
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectIApplication(sub: IApplication){
        Log.e("CollectIntercept","collectIApplication=$sub")
        allIApplication.add(sub)
    }

    fun onCreate(application: Application){
        Log.e("CollectIntercept","getAllRouterIntercept-size=${allRouterIntercept.size}")
        //循环调用各个 module 的 IApplication.onCreate
        allIApplication.forEach {
            it.onCreate(application)
        }
    }
}
//初始化
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //一键初始化所有需要的信息
        CollectApp.onCreate(this)
    }
}
```

#### 四、暴露服务

- 定义接口

```kotlin
@ExposeInterface
interface LoginHelper {
    fun getLogin():Login
}
```

- 定义实现类

```kotlin
@ImplementClass(LoginHelper::class)
class LoginHelperImpl:LoginHelper {
    override fun getLogin(): Login {
        return Login("username:1111")
    }
}
```

- 使用
```kotlin
val loginHelper = ImplementClassUtils.getSingleInstance<LoginHelper>(LoginHelper::class)
val login = loginHelper?.getLogin()
```

> 按照其他多数路由框架的设计思路, `LoginHelper` 是放在 公共 module 的，`LoginHelperImpl` 则是放在提供服务的 module ，但 [ModuleCommunication](https://github.com/FlyJingFish/ModuleCommunication)，可以将`LoginHelper`也放在提供服务的 module 下，有兴趣可以去看看

- 不用`反射`获取到服务

```kotlin
object CollectApp {
    /**
     * 收集所有的 module 的 @ExposeInterface 共享实现类
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectBindClass(sub: BindClass<*>){
        Log.e("CollectIntercept","collectBindClass=$sub")
        ImplementClassUtils.addBindClass(sub)
    }
}
```

### 混淆规则

混淆规则遵循以下两个库的混淆规则：

- [ModuleCommunication](https://github.com/FlyJingFish/ModuleCommunication?tab=readme-ov-file#%E6%B7%B7%E6%B7%86%E8%A7%84%E5%88%99)
- [AndroidAOP](https://github.com/FlyJingFish/AndroidAOP?tab=readme-ov-file#%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)


### 最后推荐我写的另外一些库

- [OpenImage 轻松实现在应用内点击小图查看大图的动画放大效果](https://github.com/FlyJingFish/OpenImage)

- [AndroidAOP 是专属于 Android 端 Aop 框架，只需一个注解就可以请求权限、切换线程、禁止多点、监测生命周期等等，没有使用 AspectJ，也可以定制出属于你的 Aop 代码](https://github.com/FlyJingFish/AndroidAOP)

- [ShapeImageView 支持显示任意图形，只有你想不到没有它做不到](https://github.com/FlyJingFish/ShapeImageView)

- [FormatTextViewLib 支持部分文本设置加粗、斜体、大小、下划线、删除线，下划线支持自定义距离、颜色、线的宽度；支持添加网络或本地图片](https://github.com/FlyJingFish/FormatTextViewLib)

- [主页查看更多开源库](https://github.com/FlyJingFish)

