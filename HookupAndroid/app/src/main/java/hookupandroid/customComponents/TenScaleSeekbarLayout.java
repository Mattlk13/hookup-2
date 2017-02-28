package hookupandroid.customComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hookupandroid.R;

/**
 * Created by Bandjur on 2/19/2017.
 */

public class TenScaleSeekbarLayout extends LinearLayout {

    private Unbinder unbinder;

    @BindView(R.id.seekbar_10) SeekBar seekBar;
    @BindView(R.id.seekbar10_current_value) TextView txtCurrentValue;

    public TenScaleSeekbarLayout(Context context) {
        super(context);
    }

    public TenScaleSeekbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        unbinder = ButterKnife.bind(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtCurrentValue.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed
        unbinder.unbind();
    }

    public int getSeekbarProgressValue() {
        return seekBar.getProgress();
    }

}
