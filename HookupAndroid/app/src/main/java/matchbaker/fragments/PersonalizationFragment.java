package matchbaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import matchbaker.R;
import matchbaker.adapters.PersonalizationPageAdapter;
import matchbaker.fragments.personalizationFragmentPages.ActivitiesPageFragment;
import matchbaker.fragments.personalizationFragmentPages.BasicInfoPageFragment;
import matchbaker.fragments.personalizationFragmentPages.PsychologyPageFragment;
import matchbaker.model.UserActivities;
import matchbaker.model.UserBasicInfo;
import matchbaker.model.UserPersonalization;
import matchbaker.model.UserPsychology;
import matchbaker.tasks.UpdateUserPersonalizationTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPersonalizationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalizationFragment extends Fragment { // implements PsychologyPageFragment.OnPsychologyPageFragmentInteractionListener {

    private UserPersonalization userPersonalization;

    private PersonalizationPageAdapter personalizationPageAdapter;
    private View inflatedView;
    private Unbinder unbinder;

    private int currentSelectedPage;

    private BasicInfoPageFragment basicInfoPageFragment;
    private ActivitiesPageFragment activitiesPageFragment;
    private PsychologyPageFragment psychologyPageFragment;

    private OnPersonalizationFragmentInteractionListener mListener;

    @BindView(R.id.view_pager_personalization) ViewPager  viewPager;
//    @BindView(R.id.personalization_scroll_view) ScrollView scrollView;



    public PersonalizationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PersonalizationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalizationFragment newInstance() {
        PersonalizationFragment fragment = new PersonalizationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        currentSelectedPage = 0;

        userPersonalization = new UserPersonalization();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            userPersonalization.setUid(user.getUid());
        }

        // TODO: add into bundle these subfragments
        basicInfoPageFragment = (BasicInfoPageFragment) Fragment.instantiate(getContext(), BasicInfoPageFragment.class.getName());
        activitiesPageFragment = (ActivitiesPageFragment) Fragment.instantiate(getContext(), ActivitiesPageFragment.class.getName());
        psychologyPageFragment = (PsychologyPageFragment) Fragment.instantiate(getContext(), PsychologyPageFragment.class.getName());
    }

    private void initializePages() {
        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(basicInfoPageFragment);
        fragments.add(activitiesPageFragment);
        fragments.add(psychologyPageFragment);
//        fragments.add(Fragment.instantiate(getContext(), BasicInfoPageFragment.class.getName()));
//        fragments.add(Fragment.instantiate(getContext(), ActivitiesPageFragment.class.getName()));
//        fragments.add(Fragment.instantiate(getContext(), PsychologyPageFragment.class.getName()));
        this.personalizationPageAdapter  = new PersonalizationPageAdapter(getChildFragmentManager(), fragments);

//        viewPager.setOnPageChangeListener(new CircularViewPagerHandler(viewPager)); //makes circular viewPager
        viewPager.setAdapter(this.personalizationPageAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        inflatedView =  inflater.inflate(R.layout.fragment_personalization, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        viewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageSelected(int position) {
                if(currentSelectedPage == 0) {
                    basicInfoPageFragment.updateUserBasicInfo();
                }
                else if(currentSelectedPage == 1) {
                    activitiesPageFragment.updateUserActivities();
//                    activitiesPageFragment.checkIfFormIsValid();
                }
                else if(currentSelectedPage == 2) {
                    psychologyPageFragment.updateUserPsychology();
//                    psychologyPageFragment.checkIsFormValid();
                }

                currentSelectedPage = position;
            }
        });

        initializePages();

        return inflatedView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.personalization_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.btn_personalization_done) {
            int currentPageItem = viewPager.getCurrentItem();
            if(currentPageItem == 0 ) {
                basicInfoPageFragment.updateUserBasicInfo();
            }
            else if (currentPageItem == 1) {
                activitiesPageFragment.updateUserActivities();
            }
            else if (currentPageItem == 2) {
                psychologyPageFragment.updateUserPsychology();
            }

            if(basicInfoPageFragment.isFormValid() &&
                    activitiesPageFragment.isFormValid() &&
                    psychologyPageFragment.isFormValid()) {

                UserBasicInfo basicInfo = basicInfoPageFragment.getUserBasicInfo();
                UserActivities activities = activitiesPageFragment.getUserActivities();
                UserPsychology psychology = psychologyPageFragment.getUserPsychology();

                userPersonalization.setBasicInfo(basicInfo);
                userPersonalization.setActivities(activities);
                userPersonalization.setPsychology(psychology);

                UpdateUserPersonalizationTask personalizationTask = new UpdateUserPersonalizationTask();
                personalizationTask.execute(userPersonalization);

                mListener.onPersonalizationDone();
            }
            else {
                Toast.makeText(getContext(), "You haven't filled all required fields!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onPersonalizationDoneButtonClicked() {
//        UserBasicInfo basicInfo = basicInfoPageFragment.getUserBasicInfo();
//        UserActivities activities = activitiesPageFragment.getUserActivities();
//        UserPsychology psychology = psychologyPageFragment.getUserPsychology();
//
//        userPersonalization.setBasicInfo(basicInfo);
//        userPersonalization.setActivities(activities);
//        userPersonalization.setPsychology(psychology);
//
//        UpdateUserPersonalizationTask personalizationTask = new UpdateUserPersonalizationTask();
//        personalizationTask.execute(userPersonalization);
//
//        // TODO check if all data is valid and filled
//        mListener.onPersonalizationDone();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPersonalizationFragmentInteractionListener) {
            mListener = (OnPersonalizationFragmentInteractionListener) context;
        } else if (getActivity() instanceof OnPersonalizationFragmentInteractionListener) {
            mListener =(OnPersonalizationFragmentInteractionListener) getActivity();
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnPersonalizationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnPersonalizationFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPersonalizationDone();
    }
}
