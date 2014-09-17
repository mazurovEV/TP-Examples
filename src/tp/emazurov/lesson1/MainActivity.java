package tp.emazurov.lesson1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//TODO: запуск активити по Intent с action
//TODO: тест onSaveInstanceState
//TODO: LinearLayouts с weight
public class MainActivity extends Activity {

    public static final String EXTRA_EDIT_TEXT = "extra_edit_text";
    private static final int REQUEST_CODE_ACTIVITY_FOR_RESULT = 5;
    private static final String LOG_TAG = MainActivity.class.getName();
    private EditText mEditText;

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

        mEditText = (EditText) findViewById(R.id.edit_text);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ACTIVITY_FOR_RESULT) {
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
        super.onDestroy();
    }
}
