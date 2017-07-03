package com.example.dongao.mydemoapp.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by xd on 2017/6/29.
 */

public class PhoneEditText extends android.support.v7.widget.AppCompatEditText {

    public PhoneEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if ((dend <= dstart && end+dest.length() >=14) || dstart==13 || end >=14 || end + dstart>=14){
                    Toast.makeText(context, "长度不能超过11位", Toast.LENGTH_SHORT).show();
                    return "";
                }
                return null;
            }
        }});
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (TextUtils.isEmpty(text)) return;
        String[] ss = text.toString().split(" ");
        String str = "";
        for (int i = 0; i < ss.length; i++) {
            str += ss[i];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char aValue = str.charAt(i);
            if ((i == 3 || i == 7) && ' ' != aValue) {
                sb.append(" ");
            }
            sb.append(aValue);
        }

        int index = sb.length();
        if (lengthBefore > lengthAfter) {
            if (index > start) {
                if (start == 4 || start == 9)
                    index = start - 1;
                else
                    index = start;
            }
        } else if (lengthAfter > lengthBefore) {
            if (lengthAfter == 1) {
                if (start == 3 || start == 8)
                    index = start + 2;
                else
                    index = start + 1;
            }
        }
        if (!sb.toString().equals(text.toString())) {
            setText(sb.toString());
        }
        setSelection(index);
    }
}
