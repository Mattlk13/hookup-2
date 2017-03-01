package hookupandroid.fragments.personalizationFragmentPages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.customComponents.TenScaleSeekbarLayout;
import hookupandroid.model.UserBasicInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBasicInfoPageInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BasicInfoPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicInfoPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_BASIC_INFO = "USER_BASIC_INFO";

    private UserBasicInfo basicInfo;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Unbinder unbinder;
    private View inflatedView;

//    private OnBasicInfoPageInteractionListener mListener;


    @BindView(R.id.imprace_seek_bar) TenScaleSeekbarLayout impraceSeekbar;
    @BindView(R.id.imprelig_seek_bar) TenScaleSeekbarLayout impreligSeekbar;
    @BindView(R.id.input_pers_race) Spinner raceSpinner;
    @BindView(R.id.input_pers_field) Spinner fieldSpinner;
    @BindView(R.id.input_pers_career) Spinner careerSpinner;


    public BasicInfoPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BasicInfoPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicInfoPageFragment newInstance(String param1, UserBasicInfo basicInfo) {
        BasicInfoPageFragment fragment = new BasicInfoPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_BASIC_INFO, basicInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            basicInfo = (UserBasicInfo) getArguments().getSerializable(ARG_BASIC_INFO);
        }
        else {
            basicInfo = new UserBasicInfo();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView =  inflater.inflate(R.layout.fragment_basic_info_page, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        updateUserBasicInfo();     // TODO: GET RID OF THIS ANTI-PATTERN! Data should be passed to another activity/fragment through interface so the reusability is perserved
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateUserBasicInfo () {
        basicInfo.setRace(raceSpinner.getSelectedItemPosition());
        basicInfo.setField(fieldSpinner.getSelectedItemPosition());
        basicInfo.setCareer(careerSpinner.getSelectedItemPosition());
        basicInfo.setImprace(impraceSeekbar.getSeekbarProgressValue());
        basicInfo.setImprelig(impreligSeekbar.getSeekbarProgressValue());
    }

    public UserBasicInfo getUserBasicInfo() {
        return basicInfo;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnBasicInfoPageInteractionListener) {
//            mListener = (OnBasicInfoPageInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnBasicInfoPageInteractionListener");
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
    public interface OnBasicInfoPageInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
