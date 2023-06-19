package com.example.a0504randomball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MySurfaceView sb;
    private static final int REQUEST_CODE_SETTINGS = 1001;

    private Handler handler;
    Button btn1, btn2, btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
/*        sb = new MySurfaceView(this);
        setContentView(sb);*/
//        초기화면과 연결됨

        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.ButtonToStart);
        btn2 = (Button) findViewById(R.id.ButtonToLearn);
        btn3 = (Button) findViewById(R.id.ButtonToSettings);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        handler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ButtonToStart) {
            setContentView(new MySurfaceView(MainActivity.this, this));
        }else if(view.getId() == R.id.ButtonToSettings){
            Intent intent = new Intent(MainActivity.this, activitysettings.class);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        }else if(view.getId() == R.id.ButtonToLearn){
            Toast.makeText(MainActivity.this, " 사실 방법론 같은건 없습니다. \n 갓 태어난 뻐꾸기처럼 본능대로 움직이십쇼 \n 우리는 할 수 있습니다", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int monsterCount = 1;
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                monsterCount = data.getIntExtra("monsterCount", monsterCount);
                System.out.println("onActivityResult >> "+ monsterCount);
                // 게임에 설정 값을 적용하는 로직 작성
                applySettingsToGame(monsterCount);
            }
        }
    }


    private void applySettingsToGame(int monsterCount) {
        // 게임에 설정 값을 적용하는 로직 작성
        // 예를 들어, MySurfaceView 객체의 setMonsterCount()와 setSpeed() 메소드를 활용할 수 있습니다.
        System.out.println("applySettingsToGame >> "+ monsterCount);
        MySurfaceView.setMonsterCount(monsterCount);
    }

}

