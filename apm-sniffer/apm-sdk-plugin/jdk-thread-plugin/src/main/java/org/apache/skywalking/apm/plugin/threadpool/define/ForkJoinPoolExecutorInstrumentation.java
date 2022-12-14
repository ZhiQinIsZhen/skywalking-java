package org.apache.skywalking.apm.plugin.threadpool.define;


import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.StaticMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

public class ForkJoinPoolExecutorInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "java.util.concurrent.ForkJoinPool";

    private static final String INTERCEPT_EXECUTE_METHOD = "execute";

    private static final String INTERCEPT_SUBMIT_METHOD = "submit";

    private static final String INTERCEPT_EXECUTE_METHOD_HANDLE = "org.apache.skywalking.apm.plugin.threadpool.ForkJoinPoolExecuteMethodInterceptor";

    private static final String INTERCEPT_SUBIMIT_METHOD_HANDLE = "org.apache.skywalking.apm.plugin.threadpool.ForkJoinPoolSubmitMethodInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        return NameMatch.byName(ENHANCE_CLASS);
    }

    @Override
    public boolean isBootstrapInstrumentation() {
        return true;
    }


    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }


    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(INTERCEPT_EXECUTE_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPT_EXECUTE_METHOD_HANDLE;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return true;
                    }
                },
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(INTERCEPT_SUBMIT_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPT_SUBIMIT_METHOD_HANDLE;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return true;
                    }
                }
        };
    }


    @Override
    public StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return new StaticMethodsInterceptPoint[0];
    }

}
