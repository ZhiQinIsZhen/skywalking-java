package org.apache.skywalking.apm.plugin.threadpool.wrapper;


import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

import java.util.concurrent.Callable;

/**
 * ContextManager.activeSpan().log(e);
 */
public class SwCallableWrapper implements Callable {


    private Callable callable;

    private ContextSnapshot contextSnapshot;

    public SwCallableWrapper(Callable callable, ContextSnapshot contextSnapshot) {
        this.callable = callable;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public Object call() throws Exception {
        AbstractSpan span = ContextManager.createLocalSpan(getOperationName());
        span.setComponent(ComponentsDefine.JDK_THREADING);
        ContextManager.continued(contextSnapshot);
        try {
            return callable.call();
        } finally {
            ContextManager.stopSpan();
        }
    }

    private String getOperationName() {
        return "SwCallableWrapper/" + Thread.currentThread().getName();
    }
}
