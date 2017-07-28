package com.deafwake.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.deafwake.R;

/**
 * Created by wel on 15-04-2017.
 */

public class CustomTextView extends TextView implements View.OnClickListener{
    public String isSelected="0";
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        customize();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customize();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        customize();
    }

    private void customize(){
        setBackground(getResources().getDrawable( R.drawable.textviewgrey));
        setTextColor(Color.WHITE);
        setOnClickListener(this);
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public void onClick(View v) {
        if (isSelected.equalsIgnoreCase("1")) {
            setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            isSelected="0";
        } else {
            setBackground(getResources().getDrawable(R.drawable.textvieworange));
            isSelected="1";
        }
    }
}
