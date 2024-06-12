# LightRouter

[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/network/members)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/LightRouter.svg)](https://github.com/FlyJingFish/LightRouter/blob/master/LICENSE)


#### 当前项目是结合和 [AndroidAOP](https://github.com/FlyJingFish/AndroidAOP) 和 [ModuleCommunication](https://github.com/FlyJingFish/ModuleCommunication)来使用的,主打一个轻量易用 ，本文不具体介绍这两个框架其他的用法，旨在介绍如何利用这两个框架配置出 类似于 ARouter 等框架的用法，如有需要点击下方链接可查看

- [AndroidAOP 面向切面编程](https://github.com/FlyJingFish/AndroidAOP)
- [ModuleCommunication 模块通信](https://github.com/FlyJingFish/ModuleCommunication)

#### 这两者结合使用，可具备 Router 的以下几个功能

- 基础的 Activity 跳转，Fragment 的获取
- 可获取封装好的跳转 Activity 的 Intent，你想怎么跳就怎么跳
- 支持异步跳转 Activity
- 支持无上下文跳转 Activity
- 通过 Url 打开页面
- 跳转页面中间的拦截器
- 解耦 各个 module 的 `伪Application` 
- 暴露服务
- 自定义降级策略
  
#### 优势所在

- 面向切面编程，探索 [AndroidAOP](https://github.com/FlyJingFish/AndroidAOP) 的**更多功能**，让你代码简洁高级，为你项目赋予高级能力
- 探索 [ModuleCommunication](https://github.com/FlyJingFish/ModuleCommunication) 的**更多功能**，让 module 之间的通信变得就像在一个 module 一样
- 结合两个库的功能，可实现纯静态代码，而不用反射。市面上的几乎没有不用反射的 Router 库吧🤔
- 够轻量，虽然轻量，但主要的Router功能基本都有了。不要看到轻量就觉得可能不好用、bug多。需要知道不一定代码多就好用，而且那么多代码你不一定都用得到

## 使用步骤

**在开始之前可以给项目一个Star吗？非常感谢，你的支持是我唯一的动力。欢迎Star和Issues!**

### 一、引入插件

1、在 **项目根目录** 的 ```build.gradle``` 里依赖插件

新版本写法

```gradle
buildscript {
    dependencies {
        //必须项 👇
        classpath 'io.github.FlyJingFish.ModuleCommunication:module-communication-plugin:1.2.5'
    }
}
plugins {
    //必须项 👇下边版本号根据你项目的 Kotlin 版本决定👇
    id 'com.google.devtools.ksp' version '1.8.10-1.0.9' apply false
    //必须项 👇需要配合 AndroidAOP 使用
    id 'io.github.FlyJingFish.AndroidAop.android-aop' version '1.8.4' apply true
}
```

或老版本写法

```gradle
buildscript {
    dependencies {
        //必须项 👇
        classpath 'io.github.FlyJingFish.ModuleCommunication:module-communication-plugin:1.2.5'
        //必须项 👇
        classpath 'io.github.FlyJingFish.AndroidAop:android-aop-plugin:1.8.4'
    }
}
plugins {
    //必须项 👇下边版本号根据你项目的 Kotlin 版本决定👇
    id 'com.google.devtools.ksp' version '1.8.10-1.0.9' apply false
}
// 👇加上这句自动为所有module“预”配置debugMode
apply plugin: "android.aop"
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
    implementation 'io.github.FlyJingFish.AndroidAop:android-aop-core:1.8.4'
    implementation 'io.github.FlyJingFish.AndroidAop:android-aop-annotation:1.8.4'
    
    //使用路径的方式跳转则必须添加（使用通信module的则不加也可以）
    implementation 'io.github.FlyJingFish.ModuleCommunication:module-communication-route:1.2.5'
    
    //这一项在你配置 communication.export 时就已经自动引入，如没有配置则需引入
    implementation 'io.github.FlyJingFish.ModuleCommunication:module-communication-annotation:1.2.5'
    
    //使用拦截器（可选项）
    implementation 'io.github.FlyJingFish.ModuleCommunication:module-communication-intercept:1.2.5'
}
```

> 你可能会说了，不是说了轻量吗，怎么要加这么多依赖库？你可以数一下每个依赖库里边的代码，总的代码量是没多少的，更何况他的功能远不止一个 Router 功能哦～

### 开始使用

路由主要用到下边两个注解

- @Route 路由页面

- @RouteParams 路由页面参数

#### 示例

- 一、配置页面

```kotlin
// activity 
@Route("/user/UserActivity")
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
@Route("/user/UserFragment")
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
ModuleRoute.builder("/user/UserActivity")
    .putValue("params1","lalla")
    .putValue("params2",user)
    .go(this)

//加上监听
ModuleRoute.builder("user/UserActivity")
    .putValue("params1","lalla")
    .putValue("params2",null as Serializable?)
    //想要自己处理跳转 context.startActivity()
    .setOnGoActivity(object : OnGoActivity{
        override fun onGo(context: Context, intent: Intent):Boolean {
            //返回true 代表自己处理跳转，返回false 代表自己不处理
            return false
        }
    })
    .go(this,object :OnNavigationBack{
        override fun onResult(result: NavigationResult) {
            //主要是用于跳转是否成功，返回结果是失败就是找不到页面，也就不会回调上边的 OnGoActivity 
        }
    })

//在自己的 module 下也可使用帮助类，帮助类也可跨模块调用需要使用 ModuleCommunication 的通信功能
`LibUser$$Router`.goUser_UserActivity(this,"hahah",user)

```

获取 Fragment

```kotlin
//在 module-communication-route 可以使用路径新建fragment对象，找不到类就会返回 null
val instance : Any = ModuleRoute.builder("/user/UserFragment")
    .putValue("params1","lalla")
    .putValue("params2",user)
    .go()
instance?.let {
    val fragment : Fragment = instance as Fragment
}

//在自己的 module 下也可使用帮助类，帮助类也可跨模块调用需要使用 ModuleCommunication 的通信功能
val fragment : Fragment = `LibUser$$Router`.newUser_UserFragment("lalala",user) as Fragment

```

**按模块去加载页面，减少初始化**

```kotlin
// builder 第一个参数传 module 名，这个名来自于帮助类 `LibUser$$Router` 的前半截 
ModuleRoute.builder("LibUser","/user/UserActivity")
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

#### 二、通过URL跳转

```kotlin
class SchemeDistributionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        uri?.let {
            ModuleRoute.builder(it)?.go()
        }
        finish()
    }
}
```

在 AndroidManifest.xml 中配置

```xml
<activity android:name=".SchemeDistributionActivity"
    android:exported="true">
    <!-- Scheme -->
    <intent-filter>
        <data
            android:host="test.flyjingfish.com"
            android:scheme="lightrouter"/>

        <action android:name="android.intent.action.VIEW"/>

        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
    </intent-filter>
</activity>
```

> ModuleRoute 会自动解析 uri ，会根据您当初设置的各项参数去解析跳转页面的参数，并且不限制类型，除了8种基本数据类型和String类型之外，其他的对象类型你只需要把对象数据转化为Json数据即可，例如

```
<script type="text/javascript">
    function onClick() {
        window.location.href =
            "lightrouter://test.flyjingfish.com/user/DetailActivity?age=10&name=hahahaha&aChar=a&user={\"id\":\"1111\",\"name\":\"哈哈😄\"}"
    }
</script>
```

#### 三、拦截器的使用

- 定义拦截器

```kotlin

class UserIntercept : RouterIntercept {
    //跳转页面时会先进入这里
    override fun onIntercept(point: InterceptPoint) {
        Log.e("onIntercept","--UserIntercept--${point.path},params = ${point.paramsMap},byPath = ${point.byPath}")
        //调用下边这句才会进入下一个拦截器或跳转页面，支持异步获取网络数据后再调用
        point.proceed()
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

- 动态增加拦截器

> 上边的初始化默认用的是 AndroidAOP 的收集直接继承类（并且不是抽象类的），我们想要动态添加拦截器，就不可直接实现 `RouterIntercept` 接口了，需要在中间加一层，避开这个规则，代码如下：

```kotlin
//定一个中间的 interface 类（或 abstract class），不可用 class，
//否则这个类会被认为是固有拦截器（即使你动态的创建对象也一样的）
interface IgnoreIntercept:RouterIntercept {
}

//动态添加就用 IgnoreIntercept
val intercept = object :IgnoreIntercept{
    override fun onIntercept(point: InterceptPoint) {
        point.proceed()
    }

    override fun order(): Int {
        return 2
    }

}
//动态添加拦截器
RouterInterceptManager.addIntercept(intercept)
//动态删除拦截器
RouterInterceptManager.removeIntercept(intercept)
```

#### 四、为每个 module 配置 `伪Application`

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

#### 五、暴露服务

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

    fun onCreate(application: Application){
        
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

#### 六、自定义降级策略


- 定义降级策略（支持多个）

```kotlin

class UserLost : RouterLost{
    //跳转页面时找不到页面会进入这里
    override fun onLost(lostPoint: LostPoint) {
        Log.e("onLost","--UserLost--")
        //调用下边这句才会进入下一个降级策略类，不调用表示消耗掉此次事件，支持异步获取网络数据后再调用
        lostPoint.proceed()
    }
    //返回序号，存在多个降级策略类时会按照这个顺序依次进入
    override fun order(): Int {
        return 2
    }
}
```
- 初始化降级策略
```kotlin
object CollectApp {
    private val allRouterLost = mutableSetOf<RouterLost>()

    /**
     * 这一步才可以收集到所有的降级处理器
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectRouterLost(sub: RouterLost){
        Log.e("CollectIntercept","collectRouterLost=$sub")
        allRouterLost.add(sub)
    }

    fun onCreate(application: Application){
        Log.e("CollectIntercept","getAllRouterIntercept-size=${allRouterIntercept.size}")
        //设置全部降级策略类
        RouterLostManager.addAllRouterLost(allRouterLost)
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

#### 七、集中配置初始化

> 你可以看到上边所有功能都写了要初始化，其实这些初始化都可以放在一个类里边去，这样便于集中管理。另外说明一点，这样写完之后，之后你再加任何 `@Route`、`拦截器`、`伪Application`、`非反射服务`、`降级策略`，都是自动生效的

```kotlin
object CollectApp {
    private val allRouterIntercept = mutableSetOf<RouterIntercept>()
    private val allIApplication = mutableSetOf<IApplication>()
    private val allRouteClazz = mutableSetOf<BaseRouterClass>()
    private val allRouterLost = mutableSetOf<RouterLost>()

    /**
     * 这一步才可以收集到所有的拦截器
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectIntercept(sub: RouterIntercept){
        Log.e("CollectIntercept","collectIntercept=$sub")
        allRouterIntercept.add(sub)
    }

    /**
     * 这一步才可以收集到所有的路由路径信息
     */

    @AndroidAopCollectMethod
    @JvmStatic
    fun collectRouterClass(sub: BaseRouterClass){
        Log.e("CollectIntercept","collectRouterClass=$sub")
        allRouteClazz.add(sub)
    }

    /**
     * 收集所有的 module 的 IApplication 类
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectIApplication(sub: IApplication){
        Log.e("CollectIntercept","collectIApplication=$sub")
        allIApplication.add(sub)
    }

    /**
     * 设置此项是为了 ImplementClassUtils 不用反射
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectBindClass(sub: BindClass<*>){
        Log.e("CollectIntercept","collectBindClass=$sub")
        ImplementClassUtils.addBindClass(sub)
    }

    /**
     * 这一步才可以收集到所有的降级处理器
     */
    @AndroidAopCollectMethod
    @JvmStatic
    fun collectRouterLost(sub: RouterLost){
        Log.e("CollectIntercept","collectRouterLost=$sub")
        allRouterLost.add(sub)
    }

    fun onCreate(application: Application){
        Log.e("CollectIntercept","getAllRouterIntercept-size=${allRouterIntercept.size}")
        //设置全部的拦截器让其起作用
        RouterInterceptManager.addAllIntercept(allRouterIntercept)
        //设置全部路由路径信息，这样ModuleRoute才可以起作用
        ModuleRoute.autoAddAllRouteClass(allRouteClazz)
        //循环调用各个 module 的 IApplication.onCreate
        allIApplication.forEach {
            it.onCreate(application)
        }
        //设置这一项跳转页面可以不需要上下文
        ModuleRoute.setApplication(application)
        //设置全部降级策略类
        RouterLostManager.addAllRouterLost(allRouterLost)
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


### 混淆规则

混淆规则遵循以下两个库的混淆规则：

- [ModuleCommunication](https://github.com/FlyJingFish/ModuleCommunication?tab=readme-ov-file#%E6%B7%B7%E6%B7%86%E8%A7%84%E5%88%99)
- [AndroidAOP](https://github.com/FlyJingFish/AndroidAOP?tab=readme-ov-file#%E6%B7%B7%E6%B7%86%E8%A7%84%E5%88%99)


### 最后推荐我写的另外一些库

- [OpenImage 轻松实现在应用内点击小图查看大图的动画放大效果](https://github.com/FlyJingFish/OpenImage)

- [AndroidAOP 是专属于 Android 端 Aop 框架，只需一个注解就可以请求权限、切换线程、禁止多点、监测生命周期等等，没有使用 AspectJ，也可以定制出属于你的 Aop 代码](https://github.com/FlyJingFish/AndroidAOP)

- [ShapeImageView 支持显示任意图形，只有你想不到没有它做不到](https://github.com/FlyJingFish/ShapeImageView)

- [FormatTextViewLib 支持部分文本设置加粗、斜体、大小、下划线、删除线，下划线支持自定义距离、颜色、线的宽度；支持添加网络或本地图片](https://github.com/FlyJingFish/FormatTextViewLib)

- [主页查看更多开源库](https://github.com/FlyJingFish)

