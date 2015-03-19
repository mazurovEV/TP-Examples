package tp.emazurov.lesson1;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

import tp.emazurov.lesson1.network.WeatherService;

//TODO: запуск активити по Intent с action
//TODO: тест onSaveInstanceState
//TODO: LinearLayouts с weight
public class MainActivity extends Activity {

    public static final String EXTRA_EDIT_TEXT = "extra_edit_text";
    private static final int REQUEST_CODE_ACTIVITY_FOR_RESULT = 5;
    private static final String LOG_TAG = MainActivity.class.getName();
    private EditText mEditText;
    private Handler h;
    private ProgressBar progressBar;
    private Receiver receiver;
    private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_lesson1);

        final Button startActivity = (Button) findViewById(R.id.start_activity);
        startActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StartedActivity.class);
                startActivity(i);
            }
        });

        Button startActivityForResult = (Button) findViewById(R.id.start_activity_for_result);
        startActivityForResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StartedForResultActivity.class);
                i.putExtra(EXTRA_EDIT_TEXT, mEditText.getText().toString());
                startActivityForResult(i, REQUEST_CODE_ACTIVITY_FOR_RESULT);
            }
        });

        Button startBrowser = (Button) findViewById(R.id.start_browser);
        startBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        Button startFragmentActivity = (Button) findViewById(R.id.start_fragment_activity);
        startFragmentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragmentsActivity.class);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        final Button startHandler = (Button) findViewById(R.id.start_handler);
        startHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHandler.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        for (int i = 1; i <= 10; i++) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            h.sendEmptyMessage(i);
                            Log.d(LOG_TAG, "i = " + i);
                        }
                    }
                });
                t.start();
            }
        });

        mEditText = (EditText) findViewById(R.id.edit_text);

        h = new Handler() {//context leak
            @Override
            public void handleMessage(Message msg) {
                mEditText.setText("i = " + msg.what);
                if (msg.what == 10) {
                    startHandler.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        };

        Button startAsyncTask = (Button) findViewById(R.id.start_async_task);
        startAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new AsyncTaskExample().execute();
            }
        });

        Button startAService = (Button) findViewById(R.id.start_service);
        startAService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, WeatherService.class);
                intent.putExtra(WeatherService.LATITUDE, "55.865314");
                intent.putExtra(WeatherService.LONGITUDE, "37.603341");
                startService(intent);

                IntentFilter filter = new IntentFilter(Receiver.ACTION);
                filter.addCategory(Intent.CATEGORY_DEFAULT);
                receiver = new Receiver();
                registerReceiver(receiver, filter);
            }
        });



        Button startBoundAService = (Button) findViewById(R.id.start_bound_service);
        startBoundAService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                doBindService();
            }
        });

        Button unboundAService = (Button) findViewById(R.id.unbound_service);
        unboundAService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnbindService();
            }
        });

    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ACTIVITY_FOR_RESULT) {
            mEditText.setText(data.getStringExtra(StartedForResultActivity.EXTRA_EDIT_TEXT_RESULT));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        if(receiver != null) unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        unbindService(mConnection);
        super.onDestroy();
    }

    public void createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Уведомление")
                .setContentText("Subject").setSmallIcon(R.drawable.cross)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }

    public class AsyncTaskExample extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
//            try {
//                new Network().urlConnection();
//                new Network().httpGet();
//            } catch (IOException e) {
//                Log.e("", e.getLocalizedMessage(), e);
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mEditText.setText("AsyncTask завершен");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public class Receiver extends BroadcastReceiver {

        public static final String WEATHER = "weather";
        public static final String ACTION = "weather.received";

        @Override
        public void onReceive(Context context, Intent intent) {
            String weather = intent.getStringExtra(WEATHER);
            mEditText.setText(weather);
            progressBar.setVisibility(View.GONE);

            createNotification();
        }
    }

    private LocalService mBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalService.LocalBinder)service).getService();
            progressBar.setVisibility(View.GONE);
            mEditText.setText(String.valueOf(mBoundService.getRandom(10, 20)));
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            mEditText.setText("Unbound");
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(MainActivity.this,
                LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}
