package com.github.a5809909.hwork06_threading;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final int MAX = 100;
    public static final int MIN = 0;
    public static final int MLS = 10;
    public static final int WHAT_THREAD = 1;
    public static final int WHAT_EXECUTOR = 2;
    private TextView textViewThread;
    private TextView textViewAsyncTask;
    private TextView textViewExecutorService;
    private ProgressBar mProgressBarThread;
    private ProgressBar mProgressBarAsyncTask;
    private ProgressBar mProgressBarExecutorService;
    private Button mButtonThread;
    private Button mButtonAsyncTask;
    private Button mButtonExecutorService;
    SimpleAsyncTask mAsyncTask;
    private Handler mHandler;
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewThread = findViewById(R.id.text_view_thread);
        textViewAsyncTask = findViewById(R.id.text_view_async_task);
        textViewExecutorService = findViewById(R.id.text_view_executor_service);

        mProgressBarThread = findViewById(R.id.progress_bar_thread);
        mProgressBarAsyncTask = findViewById(R.id.progress_bar_async_task);
        mProgressBarExecutorService = findViewById(R.id.progress_bar_executor_service);

        mProgressBarThread.setMax(MAX);
        mProgressBarAsyncTask.setMax(MAX);
        mProgressBarExecutorService.setMax(MAX);

        mButtonThread = (Button) findViewById(R.id.button_thread);
        mButtonAsyncTask = (Button) findViewById(R.id.button_async_task);
        mButtonExecutorService = (Button) findViewById(R.id.button_executor_service);

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //  textViewThread.setText("what:" + msg.what + " i:");

                if (msg.what == WHAT_THREAD) {
                    mProgressBarThread.setProgress(msg.arg1);
                    if (msg.arg1 == MIN) {
                        mButtonThread.setEnabled(false);
                        textViewThread.setText(R.string.text_thread_start);
                        mProgressBarThread.setVisibility(View.VISIBLE);
                    }
                    if (msg.arg1 == MAX) {
                        textViewThread.setText(R.string.text_thread_finish);
                        mProgressBarThread.setVisibility(View.INVISIBLE);
                        mButtonThread.setEnabled(true);
                    }
                }
                ;
                if (msg.what == WHAT_EXECUTOR) {
                    mProgressBarExecutorService.setProgress(msg.arg1);
                    if (msg.arg1 == MIN) {
                        textViewExecutorService.setText(R.string.text_executor_service_start);
                        mProgressBarExecutorService.setVisibility(View.VISIBLE);
                        mButtonExecutorService.setEnabled(false);
                    }
                    if (msg.arg1 == MAX) {
                        textViewExecutorService.setText(R.string.text_executor_service_finish);
                        mProgressBarExecutorService.setVisibility(View.INVISIBLE);
                        mButtonExecutorService.setEnabled(true);
                    }
                }
            }
        };

    }

    public void onbtnClick(View v) {
        switch (v.getId()) {
            case R.id.button_thread:
                methodThread();
                break;
            case R.id.button_async_task:
                mAsyncTask = new SimpleAsyncTask();
                mAsyncTask.execute();
                break;
            case R.id.button_executor_service:
                methodExecutor();
                break;
            default:
                break;
        }
    }

    public class SimpleAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarAsyncTask.setVisibility(View.VISIBLE);
            textViewAsyncTask.setText(R.string.text_asynctask_start);
            mButtonAsyncTask.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = MIN; i <= MAX; i++) {
                try {
                    publishProgress(i);
                    Thread.sleep(MLS);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBarAsyncTask.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textViewAsyncTask.setText(R.string.text_asynctask_finish);
            mProgressBarAsyncTask.setVisibility(View.INVISIBLE);
            mButtonAsyncTask.setEnabled(true);
        }

    }

    private void methodThread() {

        Thread thread = new Thread(new Runnable() {

            public void run() {
                for (int i = MIN; i <= MAX; i++) {
                    try {
                        Message msg = mHandler.obtainMessage(WHAT_THREAD, i, 0);
                        mHandler.sendMessage(msg);
                        Thread.sleep(MLS);
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
                for (int i = MIN; i <= MAX; i++) {
                    try {
                        Message msg = mHandler.obtainMessage(WHAT_EXECUTOR, i, 0);
                        mHandler.sendMessage(msg);
                        Thread.sleep(MLS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mExecutorService.execute(runnable);
    }
}
