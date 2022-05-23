package fan_catch.task;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
//动态代理类
public class ProxyInvocationHandler implements InvocationHandler {
    private Object targert;
//被代理的接口
    public void setTargert(Object targert) {
        this.targert = targert;
    }
    //生成得到的代理类
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                targert.getClass().getInterfaces(),this);
    }
//处理代理实例，并返回结果
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(targert, args);
        return result;
    }
}
