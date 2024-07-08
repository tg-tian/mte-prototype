import com.yomahub.liteflow.slot.DefaultContext;
import com.yomahub.liteflow.spi.holder.ContextAwareHolder;
import com.yomahub.liteflow.script.body.JaninoCommonScriptBody;
import com.yomahub.liteflow.script.ScriptExecuteWrap;

public class checkCmp implements JaninoCommonScriptBody{
    public Void body(ScriptExecuteWrap wrap){
        System.out.println("checking...");

        return null;
    }
}
