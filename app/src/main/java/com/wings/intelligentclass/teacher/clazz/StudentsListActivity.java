package com.wings.intelligentclass.teacher.clazz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wings.intelligentclass.R;
import com.wings.intelligentclass.domain.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentsListActivity extends AppCompatActivity {

    @BindView(R.id.rv_student_list)
    RecyclerView mStudentListRecyclerView;
    private List<User> mStudentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        ButterKnife.bind(this);
        setTitle("学生名单");
        mStudentsList = (List<User>) getIntent().getSerializableExtra("studentsList");
        if (mStudentsList == null || mStudentsList.size() == 0) {
            return;
        }
        mStudentListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStudentListRecyclerView.setAdapter(new StudentListAdapter());
    }

    public class StudentListAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
        public StudentListAdapter() {
            super(R.layout.item_student_view, mStudentsList);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, User item) {
            viewHolder.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_number, item.getNumber());
        }
    }
}
