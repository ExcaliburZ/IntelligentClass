package com.wings.intelligentclass.student.check_in;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wings.intelligentclass.R;
import com.wings.intelligentclass.api.IUserBiz;
import com.wings.intelligentclass.domain.MyCheckInData;
import com.wings.intelligentclass.utils.RetrofitManager;
import com.wings.intelligentclass.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCheckInActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    PieChart mChart;
    @BindView(R.id.tv_check_in_count)
    TextView mTvCheckInCount;
    @BindView(R.id.tv_check_in_amount)
    TextView mTvCheckInAmount;
    private MyCheckInData mMyCheckInData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_check_in);
        ButterKnife.bind(this);
        getMyCheckInData(getIntent());
    }

    private void getMyCheckInData(Intent intent) {
        String account = intent.getStringExtra("account");
        String classId = intent.getStringExtra("class_id");
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<MyCheckInData> myCheckInCall = iUserBiz.getMyCheckIn(classId, account);
        myCheckInCall.enqueue(new Callback<MyCheckInData>() {
            @Override
            public void onResponse(Call<MyCheckInData> call, Response<MyCheckInData> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(MyCheckInActivity.this, "获取签到数据失败");
                    return;
                }
                mMyCheckInData = response.body();
                mTvCheckInCount.setText("签到次数 : " + mMyCheckInData.getCount());
                mTvCheckInAmount.setText("签到总次数 : " + mMyCheckInData.getAmount());
                drawChart();
            }

            @Override
            public void onFailure(Call<MyCheckInData> call, Throwable t) {
                ToastUtils.showToast(MyCheckInActivity.this, "获取签到数据失败");
            }
        });
    }

    private void drawChart() {
        mChart.setCenterText("签到统计");
        PieData data = new PieData();
        List<PieEntry> yVals = new ArrayList<>();

        yVals.add(new PieEntry(mMyCheckInData.getCount(), "签到"));
        yVals.add(new PieEntry(mMyCheckInData.getAmount() - mMyCheckInData.getCount(), "未签到"));
        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setSliceSpace(5f);
        List<Integer> colors = new ArrayList<>();
        // 饼图颜色
        colors.add(Color.rgb(255, 210, 140));
        colors.add(Color.rgb(140, 235, 255));
        pieDataSet.setColors(colors);
        // entry label styling

        mChart.animateXY(1000, 1000);  //设置动画
        data.setDataSet(pieDataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                return decimalFormat.format(value) + "%";
            }
        });
        showChart(mChart, data);
    }

    private void showChart(PieChart pieChart, PieData pieData) {

        pieChart.setHoleRadius(40f);  //半径
        pieChart.setTransparentCircleRadius(5f); // 半透明圈


        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(-30); // 初始旋转角度

        mChart.setUsePercentValues(true);
        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        //pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        //设置数据
        pieChart.setData(pieData);
        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
    }

}
