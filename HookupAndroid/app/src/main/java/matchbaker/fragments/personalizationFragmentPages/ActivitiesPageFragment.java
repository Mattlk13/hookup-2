package matchbaker.fragments.personalizationFragmentPages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import matchbaker.R;
import matchbaker.customComponents.TenScaleSeekbarLayout;
import matchbaker.model.UserActivities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnActivitiesPageInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivitiesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesPageFragment extends Fragment {

    private static final String ARG_USER_ACTIVITIES = "user_activities";
    private static final String ARG_ACTIVITIES_FORM_VALID = "activities_form_valid";

    private UserActivities userActivities;
    private boolean formValid;

    private View inflatedView;
    private Unbinder unbinder;

    private OnActivitiesPageInteractionListener mListener;

    @BindView(R.id.sports_seek_bar)
    TenScaleSeekbarLayout sportsSeekbar;
    @BindView(R.id.tvsports_seek_bar)
    TenScaleSeekbarLayout tvSportsSeekbar;
    @BindView(R.id.excersice_seek_bar)
    TenScaleSeekbarLayout exersiceSeekbar;
    @BindView(R.id.dining_seek_bar)
    TenScaleSeekbarLayout diningSeekbar;
    @BindView(R.id.museums_seek_bar)
    TenScaleSeekbarLayout museumsSeekbar;
    @BindView(R.id.art_seek_bar)
    TenScaleSeekbarLayout artSeekbar;
    @BindView(R.id.hiking_seek_bar)
    TenScaleSeekbarLayout hikingSeekbar;
    @BindView(R.id.gaming_seek_bar)
    TenScaleSeekbarLayout gamingSeekbar;
    @BindView(R.id.clubbing_seek_bar)
    TenScaleSeekbarLayout clubbingSeekbar;
    @BindView(R.id.reading_seek_bar)
    TenScaleSeekbarLayout readingSeekbar;
    @BindView(R.id.tv_seek_bar)
    TenScaleSeekbarLayout tvSeekbar;
    @BindView(R.id.theater_seek_bar)
    TenScaleSeekbarLayout theaterSeekbar;
    @BindView(R.id.movies_seek_bar)
    TenScaleSeekbarLayout moviesSeekbar;
    @BindView(R.id.concerts_seek_bar)
    TenScaleSeekbarLayout concertsSeekbar;
    @BindView(R.id.music_seek_bar)
    TenScaleSeekbarLayout musicSeekbar;
    @BindView(R.id.shopping_seek_bar)
    TenScaleSeekbarLayout shoppingSeekbar;
    @BindView(R.id.yoga_seek_bar)
    TenScaleSeekbarLayout yogaSeekbar;

    public ActivitiesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param activities Parameter 1.
     * @return A new instance of fragment ActivitiesPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivitiesPageFragment newInstance(UserActivities activities, boolean formValid) {
        ActivitiesPageFragment fragment = new ActivitiesPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ACTIVITIES, activities);
        args.putSerializable(ARG_ACTIVITIES_FORM_VALID, formValid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userActivities = (UserActivities) getArguments().getSerializable(ARG_USER_ACTIVITIES);
            formValid = getArguments().getBoolean(ARG_ACTIVITIES_FORM_VALID);
        } else {
            userActivities = new UserActivities();
            formValid = false;
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
        updateUserActivities();
        formValid = checkIfFormIsValid();
        super.onDestroyView();
        unbinder.unbind();
    }

    public UserActivities getUserActivities() {
        return userActivities;
    }

    public void updateUserActivities() {
        userActivities.setArt(artSeekbar.getSeekbarProgressValue());
        userActivities.setClubbing(clubbingSeekbar.getSeekbarProgressValue());
        userActivities.setConcerts(concertsSeekbar.getSeekbarProgressValue());
        userActivities.setDining(diningSeekbar.getSeekbarProgressValue());
        userActivities.setExcersice(exersiceSeekbar.getSeekbarProgressValue());
        userActivities.setGaming(gamingSeekbar.getSeekbarProgressValue());
        userActivities.setHiking(hikingSeekbar.getSeekbarProgressValue());
        userActivities.setMovies(moviesSeekbar.getSeekbarProgressValue());
        userActivities.setMuseums(museumsSeekbar.getSeekbarProgressValue());
        userActivities.setMusic(musicSeekbar.getSeekbarProgressValue());
        userActivities.setReading(readingSeekbar.getSeekbarProgressValue());
        userActivities.setShopping(shoppingSeekbar.getSeekbarProgressValue());
        userActivities.setSports(sportsSeekbar.getSeekbarProgressValue());
        userActivities.setTheater(theaterSeekbar.getSeekbarProgressValue());
        userActivities.setTv(tvSeekbar.getSeekbarProgressValue());
        userActivities.setTvsports(tvSportsSeekbar.getSeekbarProgressValue());
        userActivities.setYoga(yogaSeekbar.getSeekbarProgressValue());

        checkIfFormIsValid();
    }

//    public boolean checkIfFormIsValid() {
//        if (sportsSeekbar.getSeekbarProgressValue() != 0 &&
//                tvSportsSeekbar.getSeekbarProgressValue() != 0 &&
//                exersiceSeekbar.getSeekbarProgressValue() != 0 &&
//                diningSeekbar.getSeekbarProgressValue() != 0 &&
//                museumsSeekbar.getSeekbarProgressValue() != 0 &&
//                artSeekbar.getSeekbarProgressValue() != 0 &&
//                hikingSeekbar.getSeekbarProgressValue() != 0 &&
//                gamingSeekbar.getSeekbarProgressValue() != 0 &&
//                clubbingSeekbar.getSeekbarProgressValue() != 0 &&
//                readingSeekbar.getSeekbarProgressValue() != 0 &&
//                tvSeekbar.getSeekbarProgressValue() != 0 &&
//                theaterSeekbar.getSeekbarProgressValue() != 0 &&
//                moviesSeekbar.getSeekbarProgressValue() != 0 &&
//                concertsSeekbar.getSeekbarProgressValue() != 0 &&
//                musicSeekbar.getSeekbarProgressValue() != 0 &&
//                shoppingSeekbar.getSeekbarProgressValue() != 0 &&
//                yogaSeekbar.getSeekbarProgressValue() != 0) {
//            return true;
//        }
//        return false;
//    }

    public boolean checkIfFormIsValid() {
        if (userActivities.getSports() != 0 &&
               userActivities.getTvsports() != 0 &&
                userActivities.getExcersice() != 0 &&
                userActivities.getDining() != 0 &&
                userActivities.getMuseums() != 0 &&
                userActivities.getArt() != 0 &&
                userActivities.getHiking() != 0 &&
                userActivities.getClubbing() != 0 &&
                userActivities.getReading() != 0 &&
                userActivities.getTv() != 0 &&
                userActivities.getTheater() != 0 &&
                userActivities.getMovies() != 0 &&
                userActivities.getConcerts() !=0 &&
                userActivities.getMusic() != 0 &&
                userActivities.getShopping() != 0&&
                userActivities.getYoga() != 0) {
            formValid = true;
        }
        else {
            formValid = false;
        }

        return formValid;
    }

    public boolean isFormValid() {
        return formValid;
    }

    public void setFormValid(boolean formValid) {
        this.formValid = formValid;
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
