package matchbaker.customComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import matchbaker.R;

/**
 * Created by Bandjur on 2/19/2017.
 */

public class SelfMeasureQuestionGroupView extends LinearLayout implements HundredScaleSeekbarLayout.HundredScaleSeekbarInteractionListener {

    private Unbinder unbinder;

    @BindView(R.id.remaming_points_value) TextView remainingPointsTextView;

    @BindView(R.id.attractive_seek_bar) HundredScaleSeekbarLayout attrSeekbar;
    @BindView(R.id.sincere_seek_bar) HundredScaleSeekbarLayout sincSeekbar;
    @BindView(R.id.intelligent_seek_bar) HundredScaleSeekbarLayout intSeekBar;
    @BindView(R.id.fun_seek_bar) HundredScaleSeekbarLayout funSeekbar;
    @BindView(R.id.ambitious_seek_bar) HundredScaleSeekbarLayout ambSeekbar;

    public SelfMeasureQuestionGroupView(Context context) {
        super(context);
    }

    public SelfMeasureQuestionGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed
        unbinder.unbind();
    }

    @Override
    public void onSeekbarProgressChanged(int oldValue, int newValue, SeekBar seekbar) {
        int remainingPoints = getRemainingPointsValue();

        if(newValue>oldValue) {
//            int remainingPoints = Integer.getInteger(remainingPointsTextView.getText().toString());
            if (oldValue + newValue > remainingPoints) {
                Toast.makeText(getContext(), "Maximum points reached! Adjust points amoung these attributes as you desire.", Toast.LENGTH_SHORT).show();
                // seekBar.setProgress(oldValue); or seekBar.setProgress(oldValue + remaining)
                seekbar.setProgress(oldValue+remainingPoints);
                remainingPointsTextView.setText(Integer.toString(0));
            } else {
                remainingPoints -= newValue;
                // TODO: call setProgress of seekBar
                remainingPointsTextView.setText(Integer.toString(remainingPoints));
            }
        }
        else {
            remainingPoints += (oldValue - newValue);
            remainingPointsTextView.setText(Integer.toString(remainingPoints));
        }
    }

    public int getRemainingPointsValue() {
        String remainingValue = remainingPointsTextView.getText().toString();
        int remainingPoints = Integer.parseInt(remainingValue);

        return remainingPoints;
    }
}
