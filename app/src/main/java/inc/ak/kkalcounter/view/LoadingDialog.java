package inc.ak.kkalcounter.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import inc.ak.kkalcounter.R;


public class LoadingDialog extends ProgressDialog {

    private ImageView logo;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.view_loading_dialog);
    }
}

