package org.apache.skywalking.apm.plugin.logger.wrapper;


import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

public class SwRunnableWapper implements Runnable {


    private Runnable runnable;

    private ContextSnapshot contextSnapshot;

    public SwRunnableWapper(Runnable runnable, ContextSnapshot contextSnapshot) {
        this.runnable = runnable;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public void run() {
        AbstractSpan span = ContextManager.createLocalSpan("SwRunnableWapper");
        span.setComponent(ComponentsDefine.JDK_THREADING);
        try {
            ContextManager.continued(contextSnapshot);
            runnable.run();
        } catch (Exception e) {
            ContextManager.activeSpan().log(e);
            throw e;
        } finally {
            ContextManager.stopSpan();
        }


    }
}
