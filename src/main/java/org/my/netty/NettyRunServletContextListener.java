package org.my.netty;

import java.net.InetSocketAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.my.netty.config.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.netty.channel.ChannelFuture;

@WebListener
public class NettyRunServletContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(NettyRunServletContextListener.class);

    @Value("${netty.port}")
    private int port;

    @Value("${netty.url}")
    private String url;

    @Autowired
    private NettyConfig nettyConfig;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("====== springboot netty destroy ======");
        nettyConfig.destroy();
        System.out.println("---test contextDestroyed method---");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
        try {
            InetSocketAddress address = new InetSocketAddress(url, port);
            ChannelFuture future = nettyConfig.run(address);
            logger.info("====== springboot netty start ======");
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    logger.info("---nettyConfig destroy---");
                    nettyConfig.destroy();
                }

            });
            future.channel().closeFuture().syncUninterruptibly();
        } catch (Exception e) {
            logger.error("---springboot netty server start error : ", e.getMessage() + "---");
        }
    }

}
