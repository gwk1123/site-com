package comm.service.handler;

import comm.ota.site.SibeSearchRequest;
import comm.utils.exception.CustomSibeException;

/**
 * Created by yangdehua on 18/2/10.
 */
public abstract class AbstractRuleHandler {


    private AbstractRuleHandler nextHandler;

    public void handleRequest(SibeSearchRequest request) throws CustomSibeException {

        doHandleRequest(request);

        // 业务代码执行成功主动调用下一个处理器处理
        if(null != nextHandler)
        {
            nextHandler.handleRequest(request);
        }
    }

    public abstract void doHandleRequest(SibeSearchRequest request) throws CustomSibeException;

    public AbstractRuleHandler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(AbstractRuleHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
