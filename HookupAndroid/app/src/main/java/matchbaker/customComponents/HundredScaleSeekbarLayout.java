package matchbaker.customComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import matchbaker.R;

/**
 * Created by Bandjur on 2/19/2017.
 */

public class HundredScaleSeekbarLayout extends LinearLayout {

    private Unbinder unbinder;

    private int seekbarOldValue;
    private int seekbarNewValue;

     private HundredScaleSeekbarInteractionListener mListener;

    @BindView(R.id.seekbar_100) SeekBar seekBar;
    @BindView(R.id.seekbar100_current_value) TextView txtCurrentValue;

    public HundredScaleSeekbarLayout(Context context) {
        super(context);
    }

    public HundredScaleSeekbarLayout(Context context, AttributeSet attrs) {
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
                seekbarOldValue = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarNewValue = seekBar.getProgress();
                mListener.onSeekbarProgressChanged(seekbarOldValue, seekbarNewValue, seekBar);
            }
        });

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewGroup parentQuestionGroupView = (ViewGroup)getParent().getParent();

        if (parentQuestionGroupView instanceof  PsychologyQuestionGroupView){
            mListener = (PsychologyQuestionGroupView)parentQuestionGroupView;
        }
        else if (parentQuestionGroupView instanceof SelfMeasureQuestionGroupView) {
            mListener = (SelfMeasureQuestionGroupView)parentQuestionGroupView;
        }

//        if(getParent() != null && getParent().getParent() instanceof PsychologyQuestionGroupView) {
//            ((PsychologyQuestionGroupView)((PsychologyQuestionGroupView) getParent()))
//                    .onSeekbarProgressChanged(seekbarOldValue, seekbarNewValue);
//            mListener = psychologyGroupParent;
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed
        unbinder.unbind();
    }

    public interface HundredScaleSeekbarInteractionListener {
//        public void onSeekbarProgressChanged(int oldValue, int newValue);
        public void onSeekbarProgressChanged(int oldValue, int newValue, SeekBar seekbar);
    }

    public int getSeekbarProgressValue() {
        return seekBar.getProgress();
    }

}
