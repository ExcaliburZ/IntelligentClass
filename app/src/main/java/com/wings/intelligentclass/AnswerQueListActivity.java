package com.wings.intelligentclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wings.intelligentclass.domain.Question;
import com.wings.intelligentclass.utils.TimeUtils;
import com.wings.intelligentclass.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerQueListActivity extends AppCompatActivity {

    @BindView(R.id.rv_que_list)
    RecyclerView mQueListRecyclerView;
    private String mClassId;
    private List<Question> mQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_que_list);
        ButterKnife.bind(this);
        mClassId = getIntent().getStringExtra("class_id");
        getQuestionList();
    }

    private void getQuestionList() {
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<List<Question>> questionListCall = iUserBiz.getQuestionList(mClassId);
        questionListCall.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(AnswerQueListActivity.this, "获取问题失败");
                    return;
                }
                mQuestionList = response.body();
                QuestionListAdapter adapter = new QuestionListAdapter();
                mQueListRecyclerView.setLayoutManager(new LinearLayoutManager(AnswerQueListActivity.this));
                mQueListRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                ToastUtils.showToast(AnswerQueListActivity.this, "获取问题失败");
            }
        });
    }

    private class QuestionListAdapter extends RecyclerView.Adapter<ClassHolder> {

        @Override
        public ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(AnswerQueListActivity.this).inflate(R.layout.item_question_list_view, parent, false);
            return new ClassHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ClassHolder holder, int position) {
            final Question question = mQuestionList.get(position);
            holder.tvTitle.setText(question.getTitle());
            holder.tvTime.setText("create at : " + TimeUtils.longToDateStr(question.getCreate_time()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AnswerQueListActivity.this, QuestionDetailActivity.class);
                    intent.putExtra("title", question.getTitle());
                    intent.putExtra("content", question.getContent());
                    intent.putExtra("id", question.getId());
                    intent.putExtra("class_id", mClassId);
                    AnswerQueListActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mQuestionList.size();
        }
    }

    class ClassHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvTime;

        ClassHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }
}
