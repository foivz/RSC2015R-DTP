package hr.foi.rsc.core.prompts;

import android.app.ProgressDialog;
import android.content.Context;

/**
 *
 * Created by Tomislav Turek on 09.11.15..
 */
public class LoadingPrompt extends AlertPrompt {

    ProgressDialog progressDialog;
    private static final String PLEASE_WAIT = "Please wait...";

    public LoadingPrompt(Context context) {
        super(context);
    }

    /**
     * shows the progress dialog
     */
    @Override
    public void showPrompt() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this.context, null, PLEASE_WAIT, true, false);
        }
    }

    /**
     * hides the progress dialog
     */
    public void hidePrompt() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
