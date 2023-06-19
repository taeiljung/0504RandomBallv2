package com.example.a0504randomball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activitysettings extends AppCompatActivity {
    private TextView monsterCountValueTextView;
    private int monsterCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        monsterCountValueTextView = findViewById(R.id.monsterCountValue);
        Button lv1Button = findViewById(R.id.level1);
        Button lv2Button = findViewById(R.id.level2);
        Button lv3Button = findViewById(R.id.level3);
        Button savebutton = findViewById(R.id.saveButton);
        lv1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monsterCount = 1;
                monsterCountValueTextView.setText(String.valueOf(monsterCount));
            }
        });

        lv2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monsterCount = 5;
                monsterCountValueTextView.setText(String.valueOf(monsterCount));
            }
        });
        lv3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monsterCount =10;
                monsterCountValueTextView.setText(String.valueOf(monsterCount));
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 값을 전달할 Intent 생성
                Intent resultIntent = new Intent();
                resultIntent.putExtra("monsterCount", monsterCount);

                // 결과 설정 및 현재 Activity 종료
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                // Toast 메시지 표시
                Toast.makeText(activitysettings.this, "Settings saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}