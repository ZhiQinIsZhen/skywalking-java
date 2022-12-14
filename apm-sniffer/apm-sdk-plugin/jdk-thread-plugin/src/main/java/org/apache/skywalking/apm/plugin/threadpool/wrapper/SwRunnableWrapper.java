package org.apache.skywalking.apm.plugin.threadpool.wrapper;


import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

public class SwRunnableWrapper implements Runnable {


    private Runnable runnable;

    private ContextSnapshot contextSnapshot;

    public SwRunnableWrapper(Runnable runnable, ContextSnapshot contextSnapshot) {
        this.runnable = runnable;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public void run() {
        AbstractSpan span = ContextManager.createLocalSpan(getOperationName());
        span.setComponent(ComponentsDefine.JDK_THREADING);
        ContextManager.continued(contextSnapshot);
        try {
            runnable.run();
        } finally {
            ContextManager.stopSpan();
        }
    }

    private String getOperationName() {
        return "SwRunnableWrapper/" + Thread.currentThread().getName();
    }
}
