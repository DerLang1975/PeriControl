package de.jkaumanns.pericontrol.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.jkaumanns.pericontrol.R;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DevicePortButtonView extends LinearLayout {

    private TextView channelNoView;
    private TextView channelResistanceView;
    private TextView channelPowerView;

    public DevicePortButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DevicePortButtonView);
        Integer channelNo = a.getInteger(R.styleable.DevicePortButtonView_channelNo, 0);
        String resistance = a.getString(R.styleable.DevicePortButtonView_resistance);
        String power = a.getString(R.styleable.DevicePortButtonView_power);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.button_device_port, this, true);

        channelNoView = (TextView) v.findViewById(R.id.deviceChannelButtonNo);
        channelNoView.setText(channelNo + "");

        channelResistanceView = (TextView) v.findViewById(R.id.deviceChannelButtonResistance);
        channelResistanceView.setText(resistance);

        channelPowerView = (TextView) v.findViewById(R.id.deviceChannelButtonPower);
        channelPowerView.setText(power);
    }

    public DevicePortButtonView(Context context) {
        this(context, null);
    }

    public void setChannelNo(int channelNo) {
        channelNoView.setText(channelNo + "");
        invalidate();
    }

    public void setChannelResistance(String channelResistance) {
        channelResistanceView.setText(channelResistance);
        invalidate();
    }

    public void setChannelPower(String channelPower) {
        channelPowerView.setText(channelPower);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return performClick();
        }
        return true;
    }
}
