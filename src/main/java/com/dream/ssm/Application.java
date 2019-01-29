package com.dream.ssm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        /**
         * 程序的其他地方使用了Netty，这里指redis。这影响在实例化传输客户端之前初始化处理器的数量。
         * 实例化传输客户端时，我们尝试初始化处理器的数量。 由于在其他地方使用Netty，
         * 因此已经初始化并且Netty会对此进行防范，因此首次实例化会因看到的非法状态异常而失败。
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(Application.class, args);
    }

}

