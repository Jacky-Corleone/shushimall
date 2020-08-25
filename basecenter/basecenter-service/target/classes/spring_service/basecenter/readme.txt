***********dubbo用户指南http://itindex.net/detail/49353-dubbo

	<!-- 向多个注册中心注册 -->
	<!-- <dubbo:serviceinterface =
		com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService"
		registry="hangzhouRegistry,qingdaoRegistry" /> -->


服务分组 ==============================
(+) (#) 
当一个接口有多种实现时，可以用group区分。 
<dubbo:servicegroup="feedback"interface="com.xxx.IndexService"/> 
<dubbo:servicegroup="member"interface="com.xxx.IndexService"/> 
<dubbo:referenceid="feedbackIndexService"group="feedback"interface="com.xxx.IndexService"/> 
<dubbo:referenceid="memberIndexService"group="member"interface="com.xxx.IndexService"/> 
任意组：(2.2.0以上版本支持，总是只调一个可用组的实现) 

<dubbo:referenceid="barService"interface="com.foo.BarService"group="*"/>


多版本 ==============================
(+) (#) 
当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。 
在低压力时间段，先升级一半提供者为新版本 
再将所有消费者升级为新版本 
然后将剩下的一半提供者升级为新版本 
<dubbo:serviceinterface="com.foo.BarService"version="1.0.0"/> 
<dubbo:serviceinterface="com.foo.BarService"version="2.0.0"/> 
<dubbo:referenceid="barService"interface="com.foo.BarService"version="1.0.0"/> 
<dubbo:referenceid="barService"interface="com.foo.BarService"version="2.0.0"/> 
不区分版本：(2.2.0以上版本支持) 

<dubbo:referenceid="barService"interface="com.foo.BarService"version="*"/>


分组聚合 ==============================
(+) (#) 
按组合并返回结果，比如菜单服务，接口一样，但有多种实现，用group区分，现在消费方需从每种group中调用一次返回结果，合并结果返回，这样就可以实现聚合菜单项。 
从2.1.0版本开始支持 

代码参见：https://github.com/alibaba/dubbo/tree/master/dubbo-test/dubbo-test-examples/src/main/java/com/alibaba/dubbo/examples/merge 

配置如：(搜索所有分组) 

<dubbo:referenceinterface="com.xxx.MenuService"group="*"merger="true"/> 
或：(合并指定分组) 

<dubbo:referenceinterface="com.xxx.MenuService"group="aaa,bbb"merger="true"/> 
或：(指定方法合并结果，其它未指定的方法，将只调用一个Group) 

<dubbo:referenceinterface="com.xxx.MenuService"group="*"> 
    <dubbo:methodname="getMenuItems"merger="true"/> 
</dubbo:service> 
或：(某个方法不合并结果，其它都合并结果) 

<dubbo:referenceinterface="com.xxx.MenuService"group="*"merger="true"> 
    <dubbo:methodname="getMenuItems"merger="false"/> 
</dubbo:service> 
或：(指定合并策略，缺省根据返回值类型自动匹配，如果同一类型有两个合并器时，需指定合并器的名称) 
参见：[合并结果扩展] 

<dubbo:referenceinterface="com.xxx.MenuService"group="*"> 
    <dubbo:methodname="getMenuItems"merger="mymerge"/> 
</dubbo:service> 
或：(指定合并方法，将调用返回结果的指定方法进行合并，合并方法的参数类型必须是返回结果类型本身) 

<dubbo:referenceinterface="com.xxx.MenuService"group="*"> 
    <dubbo:methodname="getMenuItems"merger=".addAll"/> 
</dubbo:service>


结果缓存 ==============================
(+) (#) 
结果缓存，用于加速热门数据的访问速度，Dubbo提供声明式缓存，以减少用户加缓存的工作量。 
2.1.0以上版本支持 
示例代码：https://github.com/alibaba/dubbo/tree/master/dubbo-test/dubbo-test-examples/src/main/java/com/alibaba/dubbo/examples/cache 

lru 基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。 
threadlocal 当前线程缓存，比如一个页面渲染，用到很多portal，每个portal都要去查用户信息，通过线程缓存，可以减少这种多余访问。 
jcache 与JSR107集成，可以桥接各种缓存实现。 
缓存类型可扩展，参见：CacheFactory扩展点 

配置如： 

<dubbo:referenceinterface="com.foo.BarService"cache="lru"/> 
或： 

<dubbo:referenceinterface="com.foo.BarService"> 
    <dubbo:methodname="findBar"cache="lru"/> 
</dubbo:reference> 


参数回调 ==============================

事件通知 ==============================

本地存根 ==============================

本地伪装 ==============================

并发控制  ==============================
(+) (#) 
限制com.foo.BarService的每个方法，服务器端并发执行（或占用线程池线程数）不能超过10个： 

<dubbo:serviceinterface="com.foo.BarService"executes="10"/> 
限制com.foo.BarService的sayHello方法，服务器端并发执行（或占用线程池线程数）不能超过10个： 

<dubbo:serviceinterface="com.foo.BarService"> 
    <dubbo:methodname="sayHello"executes="10"/> 
</dubbo:service> 
限制com.foo.BarService的每个方法，每客户端并发执行（或占用连接的请求数）不能超过10个： 

<dubbo:serviceinterface="com.foo.BarService"actives="10"/> 
Or: 

<dubbo:referenceinterface="com.foo.BarService"actives="10"/> 
限制com.foo.BarService的sayHello方法，每客户端并发执行（或占用连接的请求数）不能超过10个： 

<dubbo:serviceinterface="com.foo.BarService"> 
    <dubbo:methodname="sayHello"actives="10"/> 
</dubbo:service> 
Or: 

<dubbo:referenceinterface="com.foo.BarService"> 
    <dubbo:methodname="sayHello"actives="10"/> 
</dubbo:service> 
如果<dubbo:service>和<dubbo:reference>都配了actives，<dubbo:reference>优先，参见：配置的覆盖策略。 

Load Balance均衡： 

配置服务的客户端的loadbalance属性为leastactive，此Loadbalance会调用并发数最小的Provider（Consumer端并发数）。 

<dubbo:referenceinterface="com.foo.BarService"loadbalance="leastactive"/> 
Or: 

<dubbo:serviceinterface="com.foo.BarService"loadbalance="leastactive"/> 


连接控制   ==============================
(+) (#) 
限制服务器端接受的连接不能超过10个：（以连接在Server上，所以配置在Provider上） 

<dubbo:providerprotocol="dubbo"accepts="10"/> 
<dubbo:protocolname="dubbo"accepts="10"/> 
限制客户端服务使用连接连接数：(如果是长连接，比如Dubbo协议，connections表示该服务对每个提供者建立的长连接数) 

<dubbo:referenceinterface="com.foo.BarService"connections="10"/> 
Or: 

<dubbo:serviceinterface="com.foo.BarService"connections="10"/> 
如果<dubbo:service>和<dubbo:reference>都配了connections，<dubbo:reference>优先，参见：配置的覆盖策略。


