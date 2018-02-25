package cn.yiya.shiji.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import cn.yiya.shiji.R;

/**
 * Created by tomyang on 2015/9/21.
 */
public class AreaSelectDialog extends Dialog {
    private Context context;
    private Display display;
    private MyAreaSelect areaSelect;
    private OnCitySaveListener mListener;
    private Button btn_cancel,btn_save;

    public AreaSelectDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AreaSelectDialog builder(){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_areaselect_dialog, null);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        //mListView = (ListView) view.findViewById(R.id.lv_select);
        areaSelect = (MyAreaSelect) view.findViewById(R.id.myAreaSelect);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AreaSelectDialog.this.cancel();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onSave(areaSelect.getSelectCity(),AreaSelectDialog.this);
                }
            }
        });
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public interface OnCitySaveListener{
        public void onSave(final String[] text, AreaSelectDialog dialog);
    }

    public AreaSelectDialog setOnSaveListener(OnCitySaveListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public AreaSelectDialog setCityString(String city){
        areaSelect.setCityAddress(city);
        return this;
    }
}
