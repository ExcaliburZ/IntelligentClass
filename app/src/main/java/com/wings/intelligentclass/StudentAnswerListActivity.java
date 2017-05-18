package com.wings.intelligentclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wings.intelligentclass.domain.Answer;
import com.wings.intelligentclass.utils.TimeUtils;
import com.wings.intelligentclass.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAnswerListActivity extends AppCompatActivity {

    @BindView(R.id.rv_que_list)
    RecyclerView mQueListRecyclerView;
    private String mQuestionId;
    private List<Answer> mQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_student_list);
        ButterKnife.bind(this);
        mQuestionId = getIntent().getStringExtra("question_id");
        getAnswerList();
    }

    private void getAnswerList() {
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<List<Answer>> answerListCall = iUserBiz.getAnswerList(mQuestionId);
        answerListCall.enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(StudentAnswerListActivity.this, "获取回答失败");
                    return;
                }
                mQuestionList = response.body();
                AnswerListAdapter adapter = new AnswerListAdapter();
                mQueListRecyclerView.setLayoutManager(new LinearLayoutManager(StudentAnswerListActivity.this));
                mQueListRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                ToastUtils.showToast(StudentAnswerListActivity.this, "获取回答失败");
            }
        });
    }

    private class AnswerListAdapter extends RecyclerView.Adapter<ClassHolder> {

        @Override
        public ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(StudentAnswerListActivity.this).inflate(R.layout.item_my_class_view, parent, false);
            return new ClassHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ClassHolder holder, int position) {
            final Answer answer = mQuestionList.get(position);
            holder.tvName.setText(answer.getUser_name());
            holder.tvTime.setText("answer at : " + TimeUtils.longToDateStr(answer.getAnswer_time()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(StudentAnswerListActivity.this)
                            .title(answer.getUser_name())
                            .content("答案 : " + answer.getAnswer())
                            .negativeText("确认")
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mQuestionList.size();
        }
    }

    class ClassHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvTime;

        ClassHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }
}
