//package com.niuniu.gateway.router;
//
//import cn.hutool.json.JSONUtil;
//import com.alibaba.cloud.nacos.NacosConfigManager;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.alibaba.nacos.api.exception.NacosException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import javax.annotation.PostConstruct;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.Executor;
//
//@Slf4j
//@Component
//public class DynamicRouteLoader {
//    /**
//     * 对应nacos配置文件的data-id
//     */
//    private static final String dataId = "";
//    /**
//     * 对应nacos配置文件的group
//     */
//    private static final String group = "";
//
//    private Set<String> routeIds = new HashSet<>();
//
//    private static NacosConfigManager nacosConfigManager;
//
//    private static RouteDefinitionWriter routeDefinitionWriter;
//
//    @PostConstruct // @PostConstruct方法会在类加载后执行
//    public void initRouteConfigListener() throws NacosException {
//        // 1、项目启动时先拉取一次配置，并添加配置监听器
//        String configInfo = nacosConfigManager.getConfigService()
//                .getConfigAndSignListener(dataId, group, 5000, new Listener() {
//                    @Override
//                    public Executor getExecutor() {
//                        return null;
//                    }
//
//                    @Override
//                    public void receiveConfigInfo(String s) {
//                        // 2、监听到配置变更，需要去更新路由表
//                        updateConfigInfo(s);
//                    }
//                });
//        // 3、第一次读取到配置，也需要更新到路由表
//        updateConfigInfo(configInfo);
//    }
//
//    public void updateConfigInfo(String configInfo) {
//        log.info("监听到路由信息：" + configInfo);
//        // 1、解析配置信息，转为RouteDefinition
//        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
//        // 2、先全部删除
//        for (String routeId : routeIds) {
//            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
//        }
//        routeIds.clear();
//
//        // 3、更新路由
//        for (RouteDefinition routeDefinition : routeDefinitions) {
//            // 1.1、更新路由
//            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
//
//            routeIds.add(routeDefinition.getId());
//        }
//    }
//}
