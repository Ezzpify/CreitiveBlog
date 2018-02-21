package com.casper.creitive.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.casper.creitive.R;

/**
 * Created by Desu on 2018-02-21.
 */

public class NoticeDialog extends Dialog
{
    private Context _context;
    private String _title;
    private String _body;

    public NoticeDialog(Context context, String title, String body) {
        super(context);
        this._context = context;
        this._title = title;
        this._body = body;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_notice);

        TextView title = findViewById(R.id.title);
        title.setText(_title);

        TextView body = findViewById(R.id.body);
        body.setText(_body);

        Button okBtn = findViewById(R.id.ok);
        okBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });
    }
}
