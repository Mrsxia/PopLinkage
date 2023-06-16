package com.summer.popLinkage;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BcolumnAdapter extends BaseQuickAdapter<JsonBean.DataBean.ResultBean.ListBean, BaseViewHolder> {

    private BcolumnAdapter.OnCallBackData<JsonBean.DataBean.ResultBean.ListBean> onCallBackData;

    public BcolumnAdapter(int layoutResId, @Nullable List<JsonBean.DataBean.ResultBean.ListBean> resultList) {
        super(layoutResId, resultList);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonBean.DataBean.ResultBean.ListBean item) {
        helper.addOnClickListener(R.id.item_layout);
        helper.setText(R.id.tv_name, item.getName() + "");
        onCallBackData.convertView(helper, item);
    }

    public void setOnCallBackData(BcolumnAdapter.OnCallBackData<JsonBean.DataBean.ResultBean.ListBean> onCallBackData) {
        this.onCallBackData = onCallBackData;
    }

    /**
     * OnCallBackData 是一个泛型接口，表示在回调中传递数据的类型是 T。该接口包含一个 convertView() 方法，用于将数据绑定到 ViewHolder 中。
     * <p>
     * 其中，BaseViewHolder 是一个通用的 ViewHolder 类，用于存储 ItemView 中的 View 控件，从而避免在每次滚动列表时都重新查找控件。OnCallBackData 接口将数据绑定到 ViewHolder 中，可以通过 ViewHolder 中的控件来更新 ItemView 的内容。
     * <p>
     * 该接口的作用是将数据和视图分离，避免在 Activity 或 Fragment 中写过多的 UI 逻辑，同时也方便数据的管理和更新。通过实现该接口，可以将数据的获取和数据的展示分为两个部分，提高代码的可读性和可维护性。
     *
     * @param <T>
     */
    public interface OnCallBackData<T> {
        void convertView(BaseViewHolder holder, T item);
    }
}
