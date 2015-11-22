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

    ImageView map;

    /**
     * default constructor
     * @param context current application context
     */
    public InputPrompt(Context context) {
        super(context);
    }

    public InputPrompt(Context context, ImageView map) {
        super(context);
        this.map = map;
    }

    @Override
    public void prepare(int title, DialogInterface.OnClickListener positive, int positiveMessage,
                        DialogInterface.OnClickListener negative, int negativeMessage) {
        this.builder.setTitle(title)
                .setPositiveButton(positiveMessage, positive)
                .setNegativeButton(negativeMessage, negative).setView(map);
    }
}
