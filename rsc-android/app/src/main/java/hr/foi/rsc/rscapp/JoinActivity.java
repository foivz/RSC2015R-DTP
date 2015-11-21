package hr.foi.rsc.rscapp;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.core.prompts.AlertPrompt;

import static android.nfc.NdefRecord.createMime;

public class JoinActivity extends AppCompatActivity {

    EditText code;
    ImageView qr;
    Button submitCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        code = (EditText) findViewById(R.id.code_input);
        submitCode = (Button) findViewById(R.id.submit_code);
        submitCode.setOnClickListener(onSubmit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_join_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_qr_scan) {
            startActivityForResult(IntentIntegrator.createScanIntent(getApplicationContext(), IntentIntegrator.QR_CODE_TYPES,
                    "Place the QR code inside the viewfinder"), IntentIntegrator.REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets called when barcode is scanned
     * @param requestCode intent code
     * @param resultCode result code
     * @param intent scanning intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // if scan succeeded there will be an intent
        if (intent != null) {
            Log.i("hr.foi.debug", "Scan successful");
            String contents;
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            // on successful result decode the contents
            if (result != null) {
                contents = result.getContents();
                if (contents != null) {

                    // call result activity with the contents
                    Intent showResult = new Intent(getApplicationContext(), ChooseTeamActivity.class);
                    showResult.putExtra("code", contents);
                    startActivity(showResult);

                } else {
                    Log.w("hr.foi.debug", "Scan returned no result");
                    Toast.makeText(getApplicationContext(), getString(R.string.decoding_fail), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // there is no intent
            Log.i("hr.foi.debug", "Scan interrupted");
            finish();
        }
    }

    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String codeContent = code.getText().toString();

            if(!TextUtils.isEmpty(codeContent)) {
                // call result activity with the contents
                Intent showResult = new Intent(getApplicationContext(), ChooseTeamActivity.class);
                showResult.putExtra("code", codeContent);
                startActivity(showResult);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.code_field_empty),
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onBackPressed() {
        // ask for sign out if back is pressed
        DialogInterface.OnClickListener signOutListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionManager.getInstance(getApplicationContext()).destroyAll();
                dialog.dismiss();
                JoinActivity.super.onBackPressed();
            }
        };
        AlertPrompt signOutPrompt = new AlertPrompt(this);
        signOutPrompt.prepare(R.string.signout_question, signOutListener,
                R.string.sign_out, null, R.string.cancel);
        signOutPrompt.showPrompt();
    }

}

