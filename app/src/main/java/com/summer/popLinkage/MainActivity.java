package com.summer.popLinkage;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_list_default;
    private TextView ll_list_default_txt;
    private ImageView ll_list_default_icon;

    private LinearLayout ll_list_brand;
    private TextView list_brand_txt;
    private ImageView ll_list_brand_icon;

    private LinearLayout list_list_type;
    private TextView list_list_type_txt;
    private ImageView list_list_type_icon;

    private RecyclerView recyclerView;
    private GeneAdapter<String> popAdapter, popAdapter2;
    private List<String> popList, popList1, popList2;
    private PopupWindow popupWindow;
    private String currentBrand, currentType;
    private MyScrollView ui_sl_container;
    private RecyclerView assRecyclerView;
    private String jsonData;
    private AcolumnAdapter acolumnAdapter;
    private BcolumnAdapter bcolumnAdapter;
    private List<JsonBean.DataBean.ResultBean> resultList;
    private List<JsonBean.DataBean.ResultBean.ListBean> result1List;

    private int flag = 0;

    private boolean clickFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();


        String fromAssets = ToolsUtils.getFromAssets(this, "JsonData.json");
        JsonBean jsonBean = new Gson().fromJson(fromAssets, JsonBean.class);
        resultList = jsonBean.getData().getResult();

        String fromAssets1 = ToolsUtils.getFromAssets(this, "JsonData.json");
        JsonBean jsonBean1 = new Gson().fromJson(fromAssets1, JsonBean.class);
        result1List = jsonBean1.getData().getResult().get(0).getList();


        ll_list_default = findViewById(R.id.ll_list_default);
        ll_list_default_txt = findViewById(R.id.ll_list_default_txt);
        ll_list_default_icon = findViewById(R.id.ll_list_default_icon);

        ll_list_brand = findViewById(R.id.ll_list_brand);
        list_brand_txt = findViewById(R.id.list_brand_txt);
        ll_list_brand_icon = findViewById(R.id.ll_list_brand_icon);

        list_list_type = findViewById(R.id.list_list_type);
        list_list_type_txt = findViewById(R.id.list_list_type_txt);
        list_list_type_icon = findViewById(R.id.list_list_type_icon);


        View contentView = getLayoutInflater().inflate(R.layout.popwin_supplier_list, null);
        ui_sl_container = contentView.findViewById(R.id.pop_common_sl_container);
        ui_sl_container.setMaxHeight(860);
        //二级列表
        assRecyclerView = contentView.findViewById(R.id.supplier_list_lv);
        //一级列表
        recyclerView = contentView.findViewById(R.id.popwin_supplier_list_lv);

        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        //监听popupWindow关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ll_list_default_txt.setTextColor(Color.parseColor("#333333"));
                ll_list_default_icon.setImageResource(R.mipmap.screen_icon_normal);
                list_brand_txt.setTextColor(Color.parseColor("#333333"));
                ll_list_brand_icon.setImageResource(R.mipmap.screen_icon_normal);
                list_list_type_txt.setTextColor(Color.parseColor("#333333"));
                list_list_type_icon.setImageResource(R.mipmap.screen_icon_normal);
                clickFlag = true;
            }
        });
        ll_list_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickFlag) {
                    ll_list_default_txt.setTextColor(Color.parseColor("#00d8a0"));
                    ll_list_default_icon.setImageResource(R.mipmap.screen_icon_selected);
                    clickFlag = false;
                } else {
                    ll_list_default_txt.setTextColor(Color.parseColor("#333333"));
                    ll_list_default_icon.setImageResource(R.mipmap.screen_icon_normal);
                    clickFlag = true;
                }
                assRecyclerView.setVisibility(View.GONE);
                showPopupWindow0(v);
            }
        });

        ll_list_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickFlag) {
                    list_brand_txt.setTextColor(Color.parseColor("#00d8a0"));
                    ll_list_brand_icon.setImageResource(R.mipmap.screen_icon_selected);
                    clickFlag = false;
                } else {
                    list_brand_txt.setTextColor(Color.parseColor("#333333"));
                    ll_list_brand_icon.setImageResource(R.mipmap.screen_icon_normal);
                    clickFlag = true;
                }

                assRecyclerView.setVisibility(View.VISIBLE);
                showPopupWindow1(v);
            }
        });

        list_list_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickFlag) {
                    list_list_type_txt.setTextColor(Color.parseColor("#00d8a0"));
                    list_list_type_icon.setImageResource(R.mipmap.screen_icon_selected);
                    clickFlag = false;
                } else {
                    list_list_type_txt.setTextColor(Color.parseColor("#333333"));
                    list_list_type_icon.setImageResource(R.mipmap.screen_icon_normal);
                    clickFlag = true;
                }

                assRecyclerView.setVisibility(View.GONE);
                showPopupWindow2(v);
            }
        });
    }


    private void showPopupWindow0(View v) {
        popupWindow.showAsDropDown(v);
        popAdapter = new GeneAdapter<>(R.layout.item_listview_popwin, popList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(popAdapter);

        popAdapter.setOnCallBackData(new GeneAdapter.OnCallBackData<String>() {
            @Override
            public void convertView(BaseViewHolder holder, String item) {
                ((TextView) holder.getView(R.id.listview_popwind_tv)).setText(item);
                String list_default_trim = ll_list_default_txt.getText().toString().trim();
                if (list_default_trim.equals(item)) {
                    ((TextView) holder.getView(R.id.listview_popwind_tv)).setTextColor(Color.parseColor("#00d8a0"));
                    holder.getView(R.id.iv_select_icon).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) holder.getView(R.id.listview_popwind_tv)).setTextColor(Color.parseColor("#333333"));
                    holder.getView(R.id.iv_select_icon).setVisibility(View.GONE);
                }
            }
        });

        popAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //后期自己做请求处理
                popupWindow.dismiss();
                currentBrand = popList.get(position);
                ll_list_default_txt.setText(currentBrand);
                ll_list_default_txt.setTextColor(Color.parseColor("#333333"));
                ll_list_default_icon.setImageResource(R.mipmap.screen_icon_normal);
                clickFlag = true;
            }
        });

    }

    private void showPopupWindow1(View v) {
        popupWindow.showAsDropDown(v);
        acolumnAdapter = new AcolumnAdapter(R.layout.a_column_layout, resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(acolumnAdapter);
        //设置默认的选取状态
        acolumnAdapter.setSelection(flag);


        bcolumnAdapter = new BcolumnAdapter(R.layout.b_column_layout, result1List);
        assRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assRecyclerView.setAdapter(bcolumnAdapter);

        result1List.clear();
        result1List.addAll(resultList.get(flag).getList());
        bcolumnAdapter.notifyDataSetChanged();


        bcolumnAdapter.setOnCallBackData(new BcolumnAdapter.OnCallBackData<JsonBean.DataBean.ResultBean.ListBean>() {
            @Override
            public void convertView(BaseViewHolder holder, JsonBean.DataBean.ResultBean.ListBean item) {
                ((TextView) holder.getView(R.id.tv_name)).setText(item.getName());
                String list_brand_trim = list_brand_txt.getText().toString().trim();
                if (list_brand_trim.equals(item.getName())) {
                    ((TextView) holder.getView(R.id.tv_name)).setTextColor(Color.parseColor("#00d8a0"));
                    holder.getView(R.id.iv_select_icon).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) holder.getView(R.id.tv_name)).setTextColor(Color.parseColor("#333333"));
                    holder.getView(R.id.iv_select_icon).setVisibility(View.GONE);
                }
            }
        });

        //左侧列表的事件处理
        acolumnAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                acolumnAdapter.setSelection(position);
                flag = position;
                result1List.clear();
                result1List.addAll(resultList.get(position).getList());
                bcolumnAdapter.notifyDataSetChanged();
            }
        });
        //右侧列表的事件处理
        bcolumnAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String name = result1List.get(position).getName();
                list_brand_txt.setText(name);
                acolumnAdapter.setSelection(flag);
                list_brand_txt.setTextColor(Color.parseColor("#333333"));
                ll_list_brand_icon.setImageResource(R.mipmap.screen_icon_normal);
                clickFlag = true;
                result1List.clear();
                popupWindow.dismiss();
            }
        });

    }

    private void showPopupWindow2(View v) {
        popupWindow.showAsDropDown(v);
        popAdapter2 = new GeneAdapter<>(R.layout.item_listview_popwin, popList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(popAdapter2);
        popAdapter2.setOnCallBackData(new GeneAdapter.OnCallBackData<String>() {
            @Override
            public void convertView(BaseViewHolder holder, String item) {
                ((TextView) holder.getView(R.id.listview_popwind_tv)).setText(item);
                String list_list_trim = list_list_type_txt.getText().toString().trim();
                if (list_list_trim.equals(item)) {
                    ((TextView) holder.getView(R.id.listview_popwind_tv)).setTextColor(Color.parseColor("#00d8a0"));
                    holder.getView(R.id.iv_select_icon).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) holder.getView(R.id.listview_popwind_tv)).setTextColor(Color.parseColor("#333333"));
                    holder.getView(R.id.iv_select_icon).setVisibility(View.GONE);
                }
            }
        });

        popAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //后期自己做请求处理
                popupWindow.dismiss();
                currentType = popList2.get(position);
                list_list_type_txt.setText(currentType);
                list_list_type_txt.setTextColor(Color.parseColor("#333333"));
                list_list_type_icon.setImageResource(R.mipmap.screen_icon_normal);
                clickFlag = true;
            }
        });

    }

    private void InitView() {
        popList = new ArrayList<>();
        popList.add("综合排序");
        popList.add("距离优先");
        popList.add("人气优先");

        jsonData = JSON.toJSONString(popList);
        Log.e("tag", jsonData);

        popList1 = new ArrayList<>();
        popList1.add("全部分类");
        popList1.add("景点");
        popList1.add("餐饮");
        popList1.add("购物地");
        popList1.add("休闲娱乐");
        popList1.add("酒店");

        jsonData = JSON.toJSONString(popList1);
        Log.e("tag", jsonData);

        popList2 = new ArrayList<>();
        popList2.add("全部商区");
        popList2.add("热门商区");
        popList2.add("朝阳区");
        popList2.add("东城区");
        popList2.add("海淀区");
        popList2.add("西城区");
        popList2.add("丰台区");
        popList2.add("延庆区");
        popList2.add("顺义区");
        popList2.add("昌平区");
        popList2.add("密云区");
        popList2.add("怀柔区");
        popList2.add("石景山区");
        popList2.add("房山区");
        popList2.add("大兴区");
        popList2.add("通州区");

        jsonData = JSON.toJSONString(popList2);
        Log.e("tag", jsonData);

    }
}
