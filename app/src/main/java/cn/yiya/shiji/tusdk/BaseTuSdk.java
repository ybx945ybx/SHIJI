package cn.yiya.shiji.tusdk;

import android.app.Activity;

import org.lasque.tusdk.modules.components.TuSdkHelperComponent;

/**
 * Created by chenjian on 2015/12/10.
 */
public abstract class BaseTuSdk {
    public TuSdkHelperComponent componentHelper;
    public abstract void showMode(Activity activity);
}
