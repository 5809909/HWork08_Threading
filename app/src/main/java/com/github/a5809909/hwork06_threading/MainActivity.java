package com.github.a5809909.hwork06_threading;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private TextView textViewThread;
    private TextView textViewAsyncTask;
    private TextView textViewExecutorService;
    SimpleAsyncTask mAsyncTask;
    private Handler mHandler;
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewThread = findViewById(R.id.text_view_thread);
        textViewAsyncTask = findViewById(R.id.text_view_asynctask);
        textViewExecutorService = findViewById(R.id.text_view_executor_service);

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //  textViewThread.setText("what:" + msg.what + " i:");
                if (msg.what == 1) {
                    if (msg.arg1 == 1) {
                        textViewThread.setText(R.string.text_thread_start);
                    }
                    if (msg.arg1 == 10) {
                        textViewThread.setText(R.string.text_thread_finish);
                    }
                }
                ;
                if (msg.what == 2) {
                    if (msg.arg1 == 1) {
                        textViewExecutorService.setText(R.string.text_executor_service_start);
                    }
                    if (msg.arg1 == 10) {
                        textViewExecutorService.setText(R.string.text_executor_service_finish);
                    }
                }
            }
        };

    }

    public void onbtnClick(View v) {
        switch (v.getId()) {
            case R.id.button_Thread:
                methodThread();
                break;
            case R.id.button_AsyncTask:
                mAsyncTask = new SimpleAsyncTask();
                mAsyncTask.execute();
                break;
            case R.id.button_ExecutorService:
                methodExecutor();
                break;
            default:
                break;
        }
    }

    public class SimpleAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewAsyncTask.setText(R.string.text_asynctask_start);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textViewAsyncTask.setText(R.string.text_asynctask_finish);
        }

    }

    private void methodThread() {

        Thread thread = new Thread(new Runnable() {

            public void run() {
                for (int i = 1; i <= 10; i++) {
                    try {
                        Message msg = mHandler.obtainMessage(1, i, 0);
                        mHandler.sendMessage(msg);
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void methodExecutor() {

        Runnable runnable = (new Runnable() {

            public void run() {
                for (int i = 1; i <= 10; i++) {
                    try {
                        Message msg = mHandler.obtainMessage(2, i, 0);
                        mHandler.sendMessage(msg);
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mExecutorService.execute(runnable);
    }
}
