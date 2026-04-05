package com.friend.hollow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动入口类。
 *
 * <p>你可以把它理解为整个应用的“大门”：
 * 运行 main 方法后，Spring Boot 会自动完成以下工作：</p>
 * <ul>
 *     <li>启动内置 Web 服务器（默认 8080 端口）</li>
 *     <li>扫描 com.friend.hollow 包及其子包中的组件（如 Controller）</li>
 *     <li>自动加载配置和模板引擎</li>
 * </ul>
 *
 * <p>可修改点：
 * 如果你希望调整扫描范围，可移动本类所在包路径（通常不建议）。</p>
 */
@SpringBootApplication
public class HollowApplication {

    /**
     * 程序主入口。
     *
     * @param args 启动参数（一般无需手动传）
     */
    public static void main(String[] args) {
        SpringApplication.run(HollowApplication.class, args);
    }
}
