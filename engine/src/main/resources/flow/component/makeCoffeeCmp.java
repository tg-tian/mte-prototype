import com.yomahub.liteflow.slot.DefaultContext;
import com.yomahub.liteflow.spi.holder.ContextAwareHolder;
import com.yomahub.liteflow.script.body.JaninoCommonScriptBody;
import com.yomahub.liteflow.script.ScriptExecuteWrap;

public class makeCoffeeCmp implements JaninoCommonScriptBody{
    public Void body(ScriptExecuteWrap wrap){
        System.out.println("making coffee...");

        return null;
    }
}
