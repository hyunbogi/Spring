package learning;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamicProxyTest {
    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Taeyeon"), is("Hello Taeyeon"));
        assertThat(hello.sayHi("Taeyeon"), is("Hi Taeyeon"));
        assertThat(hello.sayThankYou("Taeyeon"), is("Thank You Taeyeon"));

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Taeyeon"), is("HELLO TAEYEON"));
        assertThat(proxiedHello.sayHi("Taeyeon"), is("HI TAEYEON"));
        assertThat(proxiedHello.sayThankYou("Taeyeon"), is("THANK YOU TAEYEON"));
    }

    @Test
    public void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat(proxiedHello.sayHello("Taeyeon"), is("HELLO TAEYEON"));
        assertThat(proxiedHello.sayHi("Taeyeon"), is("HI TAEYEON"));
        assertThat(proxiedHello.sayThankYou("Taeyeon"), is("THANK YOU TAEYEON"));
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Taeyeon"), is("HELLO TAEYEON"));
        assertThat(proxiedHello.sayHi("Taeyeon"), is("HI TAEYEON"));
        assertThat(proxiedHello.sayThankYou("Taeyeon"), is("THANK YOU TAEYEON"));
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Taeyeon"), is("HELLO TAEYEON"));
        assertThat(proxiedHello.sayHi("Taeyeon"), is("HI TAEYEON"));
        assertThat(proxiedHello.sayThankYou("Taeyeon"), is("Thank You Taeyeon"));
    }

    interface Hello {
        String sayHello(String name);

        String sayHi(String name);

        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello {
        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank You " + name;
        }
    }

    static class HelloUppercase implements Hello {
        private Hello hello;

        public HelloUppercase(Hello hello) {
            this.hello = hello;
        }

        @Override
        public String sayHello(String name) {
            return hello.sayHello(name).toUpperCase();
        }

        @Override
        public String sayHi(String name) {
            return hello.sayHi(name).toUpperCase();
        }

        @Override
        public String sayThankYou(String name) {
            return hello.sayThankYou(name).toUpperCase();
        }
    }

    static class UppercaseHandler implements InvocationHandler {
        private Object target;

        public UppercaseHandler(Hello target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object ret = method.invoke(target, args);
            if (ret instanceof String && method.getName().startsWith("say")) {
                return ((String) ret).toUpperCase();
            }
            return ret;
        }
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
