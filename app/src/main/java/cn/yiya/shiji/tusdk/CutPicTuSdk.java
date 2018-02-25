package cn.yiya.shiji.tusdk;

import android.app.Activity;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumMultipleComponent;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutOption;
import org.lasque.tusdk.modules.components.TuSdkComponent;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdk.modules.components.edit.TuEditActionType;

import java.io.File;

import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by chenjian on 2015/12/10.
 */
public class CutPicTuSdk extends BaseTuSdk implements TuEditTurnAndCutFragment.TuEditTurnAndCutFragmentDelegate {

    private Activity mActivity;
    private OnImageHandleListerner mListener;
    private boolean bHanlder = false;
    private int nWidth;

    private boolean bCancel;
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 640;
    private static final int NON_SQUARE_WIDTH = 960;

    // 选取照片后裁剪，然后进行渲染处理，这里图片按照正方形裁剪
    public void openTuSdkCutPic(Activity activity, OnImageHandleListerner listerner) {
        this.mListener = listerner;
        nWidth = DEFAULT_WIDTH;
        showMode(activity);
    }

    // 选取照片后裁剪，不渲染处理,这里单独对身份证按照3:2的比例裁剪
    public void openTuSdkSimpleHand(Activity activity, OnImageHandleListerner listerner) {
        this.mListener = listerner;
        nWidth = NON_SQUARE_WIDTH;
        showMode(activity);
    }

    public void openTuSdkCutPic(Activity activity, boolean bHanlder, OnImageHandleListerner listerner) {
        openTuSdkCutPic(activity, listerner);
        this.bHanlder = bHanlder;
    }

    public void openTuSdkCutPicCollocation(Activity activity, boolean bHanlder, OnImageHandleListerner listerner) {
        openTuSdkCutPicCollocation(activity, listerner);
        this.bHanlder = bHanlder;
    }

    public void openTuSdkCutPicCollocation(Activity activity, OnImageHandleListerner listerner) {
        this.mListener = listerner;
        nWidth = 420;
        showMode(activity);
    }

    public boolean getCancel() {
        return bCancel;
    }

    @Override
    public void showMode(Activity activity) {
        if (activity == null)
            return;
        mActivity = activity;
        bCancel = true;
        componentHelper = new TuSdkHelperComponent(activity);
        // 开启相册选择照片
        TuAlbumMultipleComponent comp = TuSdkGeeV1.albumMultipleCommponent(activity, new TuSdkComponent.TuSdkComponentDelegate() {
            @Override
            public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment) {
                openTuEditTurnAndCut(result, error, lastFragment);
            }

        });

        comp.componentOption().albumListOption().setPhotoColumnNumber(4);
        // 在组件执行完成后自动关闭组件
        comp.setAutoDismissWhenCompleted(false).showComponent();
    }

    /**
     * 开启图片编辑组件 (裁剪)
     *
     * @param result       返回结果
     * @param error        异常信息
     * @param lastFragment 最后显示的控制器
     */
    private void openTuEditTurnAndCut(final TuSdkResult result, Error error, TuFragment lastFragment) {
        if (result == null || error != null) return;

        // 组件选项配置
        // @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditTurnAndCutOption.html
        TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();

        // 是否开启滤镜支持 (默认: 关闭)
        option.setEnableFilters(true);
        // 开启用户滤镜历史记录
        option.setEnableFiltersHistory(true);
        // 开启在线滤镜
        option.setEnableOnlineFilter(true);

        // 显示滤镜标题视图
        option.setDisplayFiltersSubtitles(true);

        // 需要裁剪的长宽
        option.setCutSize(new TuSdkSize(nWidth, DEFAULT_HEIGHT));

        // 是否在控制器结束后自动删除临时文件
        option.setAutoRemoveTemp(true);

        // 是否保存到相册
        option.setSaveToAlbum(true);

        // 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
//        option.setShowResultPreview(true);
        // 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
        // option.setRenderFilterThumb(true);

        TuEditTurnAndCutFragment fragment = option.fragment();

        // 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
        fragment.setImageSqlInfo(result.imageSqlInfo);
        fragment.setImage(result.image);
        fragment.setTempFilePath(result.imageFile);

        fragment.setDelegate(this);

        // 如果lastFragment不存在，您可以使用如下方法开启fragment
        if (lastFragment == null) {
            this.componentHelper.presentModalNavigationActivity(fragment);
        } else {
            // 开启图片编辑组件 (裁剪)
            lastFragment.pushFragment(fragment);
        }
    }

    /**
     * 图片编辑完成
     *
     * @param tuEditTurnAndCutFragment 旋转和裁剪视图控制器
     * @param tuSdkResult              旋转和裁剪视图控制器处理结果
     */
    @Override
    public void onTuEditTurnAndCutFragmentEdited(TuEditTurnAndCutFragment tuEditTurnAndCutFragment, TuSdkResult tuSdkResult) {
        if (!tuEditTurnAndCutFragment.isShowResultPreview()) {
            tuEditTurnAndCutFragment.hubDismissRightNow();
            tuEditTurnAndCutFragment.dismissActivityWithAnim();
        }
        if (!bHanlder) {
            if (mListener != null) {
                File img = new File(tuSdkResult.imageSqlInfo.path);
                mListener.onFinished(img);
            }
        } else {
            openEditMultiple(tuSdkResult, null, null);
        }


    }

    /**
     * 图片编辑完成 (异步方法)
     *
     * @param tuEditTurnAndCutFragment 旋转和裁剪视图控制器
     * @param tuSdkResult              旋转和裁剪视图控制器处理结果
     * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
     */
    @Override
    public boolean onTuEditTurnAndCutFragmentEditedAsync(TuEditTurnAndCutFragment tuEditTurnAndCutFragment, TuSdkResult tuSdkResult) {
        TLog.d("onTuEditTurnAndCutFragmentEditedAsync: %s", tuSdkResult);
        return false;
    }

    @Override
    public void onComponentError(TuFragment tuFragment, TuSdkResult tuSdkResult, Error error) {
        TLog.d("onComponentError: fragment - %s, result - %s, error - %s", tuFragment, tuSdkResult, error);
    }

    /**
     * 开启多功能图片编辑
     */
    private void openEditMultiple(final TuSdkResult result, Error error, TuFragment lastFragment) {
        if (result == null || error != null) return;

        // 组件委托
        TuSdkComponent.TuSdkComponentDelegate delegate = new TuSdkComponent.TuSdkComponentDelegate() {
            @Override
            public void onComponentFinished(TuSdkResult sResult, Error error, TuFragment lastFragment) {

                SimpleUtils.deleteCropImg(mActivity, result.imageSqlInfo.path);

                TLog.d("onEditMultipleComponentReaded: %s | %s", sResult, error);
                bCancel = false;
                if (mListener != null) {
                    File img = new File(sResult.imageSqlInfo.path);
                    mListener.onFinished(img);
                }
            }
        };

        // 组件选项配置
        TuEditMultipleComponent component = null;

        if (lastFragment == null) {
            component = TuSdkGeeV1.editMultipleCommponent(this.componentHelper.activity(), delegate);
        } else {
            component = TuSdkGeeV1.editMultipleCommponent(lastFragment, delegate);
        }

        component.componentOption().editMultipleOption().disableModule(TuEditActionType.TypeCuter);

        // 设置图片
        component.setImage(result.image)
                // 设置系统照片
                .setImageSqlInfo(result.imageSqlInfo)
                // 设置临时文件
                .setTempFilePath(result.imageFile)
                // 在组件执行完成后自动关闭组件
                .setAutoDismissWhenCompleted(true)
                // 开启组件
                .showComponent();
    }

    public interface OnImageHandleListerner {
        public void onFinished(File imgeFile);
    }

}
