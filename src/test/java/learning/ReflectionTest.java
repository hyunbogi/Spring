package learning;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";

        // length()
        assertThat(name.length(), is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat(lengthMethod.invoke(name), is(6));

        // charAt()
        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat(charAtMethod.invoke(name, 0), is('S'));
    }

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

    public class UppercaseHandler implements InvocationHandler {
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
}
