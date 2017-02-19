package hookupandroid.fragments.personalizationFragmentPages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.customComponents.HundredScaleSeekbarLayout;

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
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private View inflatedView;
    private Unbinder unbinder;

    @BindView(R.id.remaming_points_a_value) TextView aRemainingPointsTextView;
    @BindView(R.id.remaming_points_b_value) TextView bRemainingPointsTextView;
    @BindView(R.id.remaming_points_c_value) TextView cRemainingPointsTextView;


    @BindView(R.id.a_attractive_seek_bar) HundredScaleSeekbarLayout aAttrSeekbar;
    @BindView(R.id.a_sincere_seek_bar) HundredScaleSeekbarLayout aSincSeekbar;
    @BindView(R.id.a_intelligent_seek_bar) HundredScaleSeekbarLayout aIntSeekBar;
    @BindView(R.id.a_fun_seek_bar) HundredScaleSeekbarLayout aFunSeekbar;
    @BindView(R.id.a_ambitious_seek_bar) HundredScaleSeekbarLayout aAmbSeekbar;
    @BindView(R.id.a_shared_interests_seek_bar) HundredScaleSeekbarLayout aSharSeekbar;

    @BindView(R.id.b_attractive_seek_bar) HundredScaleSeekbarLayout bAttrSeekbar;
    @BindView(R.id.b_sincere_seek_bar) HundredScaleSeekbarLayout bSincSeekbar;
    @BindView(R.id.b_intelligent_seek_bar) HundredScaleSeekbarLayout bIntSeekBar;
    @BindView(R.id.b_fun_seek_bar) HundredScaleSeekbarLayout bFunSeekbar;
    @BindView(R.id.b_ambitious_seek_bar) HundredScaleSeekbarLayout bAmbSeekbar;
    @BindView(R.id.b_shared_interests_seek_bar) HundredScaleSeekbarLayout bSharSeekbar;

    @BindView(R.id.c_attractive_seek_bar) HundredScaleSeekbarLayout cAttrSeekbar;
    @BindView(R.id.c_sincere_seek_bar) HundredScaleSeekbarLayout cSincSeekbar;
    @BindView(R.id.c_intelligent_seek_bar) HundredScaleSeekbarLayout cIntSeekBar;
    @BindView(R.id.c_fun_seek_bar) HundredScaleSeekbarLayout cFunSeekbar;
    @BindView(R.id.c_ambitious_seek_bar) HundredScaleSeekbarLayout cAmbSeekbar;

    @BindView(R.id.btnPersDon) Button btnDone;

    private OnPsychologyPageFragmentInteractionListener mListener;

    public PsychologyPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PsychologyPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PsychologyPageFragment newInstance(String param1, String param2) {
        PsychologyPageFragment fragment = new PsychologyPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
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
        super.onDestroyView();
        unbinder.unbind();
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnPsychologyPageFragmentInteractionListener) {
//            mListener = (OnPsychologyPageFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnPsychologyPageFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
//        void onFragmentInteraction(Uri uri);
    }
}
