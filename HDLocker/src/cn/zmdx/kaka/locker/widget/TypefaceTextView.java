
package cn.zmdx.kaka.locker.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import cn.zmdx.kaka.locker.font.FontManager;

public class TypefaceTextView extends TextView {

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeFace();
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context) {
        this(context, null);
    }

    private void setTypeFace() {
        Typeface tf = FontManager.getChineseTypeface(getContext());
        setTypeface(tf);
    }
}
