项目目录结构介绍：

─xxx
  ├─base        //基类目录，存放各个组件的基类封装
  ├─bean        //实体目录，存放数据库、网络交互数据实体类
  │  └─db           //数据库实体类
  ├─business    //业务范围
  │  ├─action   //业务逻辑处理目录，存放XxxAction.java，实现与服务端网络交互处理的控制器类
  │  ├─extra    //业务相关额外目录，存放与业务处理相关额外的帮助类文件
  │  ├─interf   //业务交互接口目录，存放XxxView.java,主要是action与ui直接连接的桥梁接口
  │  └─ui       //业务视图展示目录，存放activity,framgment等视图处理组件
  ├─comm        //通用目录，用于存放配置类、常量池、接口池等
  ├─event       //事件目录，用于存放全局使用的otto事件
  ├─interf      //接口目录，用于存放全局使用的接口文件
  ├─manager     //管理器目录，用于存放管理器，比如网络交互管理器，缓存管理器等等
  │  ├─cache        //缓存管理器
  │  ├─db           //数据库管理器
  │  │  └─impl          //数据库具体业务对应的管理器的实现
  │  └─net          //网络管理器
  ├─utils       //工具类集合目录
  └─widget      //自定义控件目录


部分相关工具集及管理器使用说明：

AndroidUtilsCore:部分工具集
    在Application中初始化，Splash获取基本权限后调用initAfterCheckPermissions初始化项目存储目录、异常捕获处理、设备信息提取；
    含有部分常用的工具类方法可以进行使用。

CacheManager:缓存管理器
    可以进行缓存多张常见数据类型的数据具体参考类文件方法。

EventManager:事件管理器
    通过事件发出post,在接收页面通过注解@Subscribe接收事件，实现跨页面事件通知、数据传递等效果。

NetManager:网络交互管理器
    可以进行get、post、download、upload的网络交互管理器

SplashAuth：运行时权限授权工具
    针对运行时权限授权，通过调研SplashAuthUI的launch方法，在onActivityResult中处理结果，实例参考SplashActivity.java

SmartRefreshLayout:刷新组件
    项目用到的下拉刷新、加载更多组件

JsonManager:Json序列化和反序列化管理器
    基于fastjson的工具类

SystemBarTintManager:系统Bar控制器
    用于控制状态栏和导航栏颜色、透明度等，已在基类activity中进行了处理，具体页面亦可自行处理

ThreadManager:线程池管理器
    创建管理线程池

ImgLoader:图片加载工具类
    基于Glide的加载网络图片护着本地图片工具

Logg:日志打印工具类
    代买中log打印工具类