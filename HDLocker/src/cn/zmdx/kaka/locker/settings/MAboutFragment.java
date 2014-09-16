
package cn.zmdx.kaka.locker.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.zmdx.kaka.locker.R;
import cn.zmdx.kaka.locker.utils.BaseInfoHelper;
import cn.zmdx.kaka.locker.utils.HDBConfig;
import cn.zmdx.kaka.locker.utils.HDBReflectionUtils;

public class MAboutFragment extends Fragment {
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.setting_about_us, container, false);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
