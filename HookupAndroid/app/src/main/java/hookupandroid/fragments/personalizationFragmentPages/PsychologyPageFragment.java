package hookupandroid.fragments.personalizationFragmentPages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.customComponents.PsychologyQuestionGroupView;
import hookupandroid.customComponents.SelfMeasureQuestionGroupView;
import hookupandroid.fragments.PersonalizationFragment;
import hookupandroid.model.UserPsychology;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPsychologyPageFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PsychologyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PsychologyPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_PSYCHOLOGY = "user_psychology";
    private UserPsychology userPsychology;

    private View inflatedView;
    private Unbinder unbinder;

    @BindView(R.id.psychology_view_a) PsychologyQuestionGroupView aQuestionGroupView;
    @BindView(R.id.psychology_view_b) PsychologyQuestionGroupView bQuestionGroupView;
    @BindView(R.id.psychology_view_c) SelfMeasureQuestionGroupView cSelfMeasureGroupView;

    @BindView(R.id.btnPersDon) Button btnDone;

    private OnPsychologyPageFragmentInteractionListener mListener;

    public PsychologyPageFragment() {
        // Required empty public constructor
    }

    public static PsychologyPageFragment newInstance(UserPsychology userPsychology) {
        PsychologyPageFragment fragment = new PsychologyPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_PSYCHOLOGY, userPsychology);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userPsychology = (UserPsychology) getArguments().getSerializable(ARG_USER_PSYCHOLOGY);
        }
        else {
            userPsychology = new UserPsychology();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_psychology_page, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        updateUserPsychology();
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateUserPsychology() {
//        userPsychology.setAmbA(seek);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment parentFragment = getParentFragment();
        if (context instanceof OnPsychologyPageFragmentInteractionListener) {
            mListener = (OnPsychologyPageFragmentInteractionListener) context;
        }
        else if (parentFragment != null && parentFragment instanceof PersonalizationFragment) {
            mListener = (OnPsychologyPageFragmentInteractionListener) parentFragment;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnPsychologyPageFragmentInteractionListener");
        }
    }

    @OnClick(R.id.btnPersDon)
    public void onPersonalizationButtonClicked() {
        mListener.onPersonalizationDoneButtonClicked();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public UserPsychology getUserPsychology() {
        return userPsychology;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPsychologyPageFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPersonalizationDoneButtonClicked();
    }
}
