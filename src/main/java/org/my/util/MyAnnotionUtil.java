package org.my.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.my.annotation.Module;
import org.my.netty.bean.Message;
import org.my.netty.bean.Result;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
@DependsOn("springUtil")
public class MyAnnotionUtil {

    // 控制器类对象
    private static Map<Integer, Object> controllerClasses = new HashMap<>();
    
    static {

        System.out.println("MyAnnotionUtil static");

        // 拿到基础包之后,去得到所有Controller类
        List<Class> clzes = ClassUtil.parseAllController("org.my.netty.handler");

        // 迭代所有全限定名
        for (Class clz : clzes) {
            // 判断是否有自定义注解
            if (clz.isAnnotationPresent(Module.class)) {

                try {

                    // 获取自定义注解
                    Module annotation = (Module) clz.getAnnotation(Module.class);
                    // 获取value值
                    int value = annotation.module();

                    Object obj = controllerClasses.get(value);
                    if (obj == null) {
//                         obj = applicationContext.getBean(clz);
//                        obj = clz.newInstance();
                        obj = SpringUtil.getBean(clz);
                        controllerClasses.put(value, obj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Result process(ChannelHandlerContext ctx, Message msg) {

        try {
            Object obj = controllerClasses.get(msg.getModule());
            if (obj == null) {
                return new Result(0, "fail", msg.toString());
            }
            Method method = obj.getClass().getMethod("process", ChannelHandlerContext.class, Message.class);
            Object message = method.invoke(obj, ctx, msg);

            return (Result) message;
        } catch (Exception e) {
            return new Result(0, "fail", msg.toString());
        }
    }

}
