package hookupandroid.fragments.personalizationFragmentPages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.customComponents.TenScaleSeekbarLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnActivitiesPageInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivitiesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private View inflatedView;
    private Unbinder unbinder;

    private OnActivitiesPageInteractionListener mListener;

    @BindView(R.id.sports_seek_bar) TenScaleSeekbarLayout sportsSeekbar;
    @BindView(R.id.tvsports_seek_bar) TenScaleSeekbarLayout tvSportsSeekbar;
    @BindView(R.id.excersice_seek_bar) TenScaleSeekbarLayout exersiceSeekbar;
    @BindView(R.id.dining_seek_bar) TenScaleSeekbarLayout diningSeekbar;
    @BindView(R.id.museums_seek_bar) TenScaleSeekbarLayout museumsSeekbar;
    @BindView(R.id.art_seek_bar) TenScaleSeekbarLayout artSeekbar;
    @BindView(R.id.hiking_seek_bar) TenScaleSeekbarLayout hikingSeekbar;
    @BindView(R.id.gaming_seek_bar) TenScaleSeekbarLayout gamingSeekbar;
    @BindView(R.id.clubbing_seek_bar) TenScaleSeekbarLayout clubbingSeekbar;
    @BindView(R.id.reading_seek_bar) TenScaleSeekbarLayout readingSeekbar;
    @BindView(R.id.tv_seek_bar) TenScaleSeekbarLayout tvSeekbar;
    @BindView(R.id.theater_seek_bar) TenScaleSeekbarLayout theaterSeekbar;
    @BindView(R.id.movies_seek_bar) TenScaleSeekbarLayout moviesSeekbar;
    @BindView(R.id.concerts_seek_bar) TenScaleSeekbarLayout concertsSeekbar;
    @BindView(R.id.music_seek_bar) TenScaleSeekbarLayout musicSeekbar;
    @BindView(R.id.shopping_seek_bar) TenScaleSeekbarLayout shoppingSeekbar;
    @BindView(R.id.yoga_seek_bar) TenScaleSeekbarLayout yogaSeekbar;

    public ActivitiesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ActivitiesPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivitiesPageFragment newInstance(String param1, String param2) {
        ActivitiesPageFragment fragment = new ActivitiesPageFragment();
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
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_activities_page, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
    public interface OnActivitiesPageInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
