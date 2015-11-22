package hr.foi.rsc.core.prompts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;

import hr.foi.rsc.core.Input;

/**
 *
 * Created by Tomislav Turek on 09.11.15..
 */
public class InputPrompt extends AlertPrompt {

    Bitmap map;

    /**
     * default constructor
     * @param context current application context
     */
    public InputPrompt(Context context) {
        super(context);
    }

    public InputPrompt(Context context, Bitmap map) {
        super(context);
        this.map = map;
    }

    @Override
    public void prepare(int title, DialogInterface.OnClickListener positive, int positiveMessage,
                        DialogInterface.OnClickListener negative, int negativeMessage) {
        super.prepare(title, positive, positiveMessage, negative, negativeMessage);

        // dialog input
        ImageView image = new ImageView(context);
        image.setImageBitmap(map);
        this.builder.setView(image);
    }
}
