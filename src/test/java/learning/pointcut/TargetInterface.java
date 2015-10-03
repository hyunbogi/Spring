package learning.pointcut;

interface TargetInterface {
    void hello();

    void hello(String b);

    int minus(int a, int b) throws RuntimeException;

    int plus(int a, int b);
}
