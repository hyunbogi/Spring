package learning.pointcut;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PointcutTest {
    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException {
        final String expression =
                "execution(public int learning.pointcut.Target.minus(int,int) throws java.lang.RuntimeException)";

        // Target.minus()
        pointcutMatches(expression, true, Target.class, "minus", int.class, int.class);

        // Target.plus()
        pointcutMatches(expression, false, Target.class, "plus", int.class, int.class);

        // Bean.method()
        pointcutMatches(expression, false, Bean.class, "method");
    }

    private void pointcutMatches(String expression,
                                 boolean expected,
                                 Class<?> clazz,
                                 String methodName,
                                 Class<?>... args) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(
                pointcut.getClassFilter().matches(clazz)
                        && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null),
                is(expected)
        );
    }
}
