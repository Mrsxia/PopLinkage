package com.summer.popLinkage;


import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class GeneAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public GeneAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public GeneAdapter(@Nullable List<T> data) {
        super(data);
    }

    public GeneAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }


    private OnCallBackData<T> onCallBackData;

    @Override
    protected void convert(BaseViewHolder holder, T item) {
        if (onCallBackData != null) {
            onCallBackData.convertView(holder, item);
        }
    }

    public void setOnCallBackData(OnCallBackData<T> onCallBackData) {
        this.onCallBackData = onCallBackData;
    }

    public interface OnCallBackData<T> {
        void convertView(BaseViewHolder holder, T item);
    }

}