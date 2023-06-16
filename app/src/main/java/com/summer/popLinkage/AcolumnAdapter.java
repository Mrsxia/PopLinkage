package com.summer.popLinkage;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AcolumnAdapter extends BaseQuickAdapter<JsonBean.DataBean.ResultBean, BaseViewHolder> {
    private int position;


    public AcolumnAdapter(int layoutResId, @Nullable List<JsonBean.DataBean.ResultBean> resultList) {
        super(layoutResId, resultList);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonBean.DataBean.ResultBean item) {
        helper.setTextColor(R.id.tv_class_name, helper.getLayoutPosition() == position ? Color.parseColor("#00d8a0") : Color.parseColor("#363636"));
        helper.setText(R.id.tv_class_name, item.getName());
    }

    public void setSelection(int pos) {
        this.position = pos;
        notifyDataSetChanged();
    }

}
