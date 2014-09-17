package tp.emazurov.lesson1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartedForResultActivity extends Activity {

    private static final String LOG_TAG = StartedForResultActivity.class.getName();
    public static final String EXTRA_EDIT_TEXT_RESULT = "extra_edit_text_result";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.started_for_result_activity);
        Button returnButton = (Button) findViewById(R.id.return_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(EXTRA_EDIT_TEXT_RESULT, mEditText.getText().toString());
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        mEditText = (EditText) findViewById(R.id.edit_text_for_result);
        mEditText.setText(getIntent() != null ? getIntent().getStringExtra(MainActivity.EXTRA_EDIT_TEXT) : "getIntent() == null");
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
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
