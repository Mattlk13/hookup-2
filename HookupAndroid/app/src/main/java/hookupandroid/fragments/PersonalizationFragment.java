package hookupandroid.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.adapters.PersonalizationPageAdapter;
import hookupandroid.common.extendedComponents.VerticalScrollView;
import hookupandroid.common.handlers.CircularViewPagerHandler;
import hookupandroid.fragments.personalizationFragmentPages.ActivitiesPageFragment;
import hookupandroid.fragments.personalizationFragmentPages.BasicInfoPageFragment;
import hookupandroid.fragments.personalizationFragmentPages.PsychologyPageFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPersonalizationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalizationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private PersonalizationPageAdapter personalizationPageAdapter;
    private View inflatedView;
    private Unbinder unbinder;

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
     * @param param1 Parameter 1.
     * @return A new instance of fragment PersonalizationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalizationFragment newInstance(String param1, String param2) {
        PersonalizationFragment fragment = new PersonalizationFragment();
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

    private void initializePages() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(getContext(), BasicInfoPageFragment.class.getName()));
        fragments.add(Fragment.instantiate(getContext(), ActivitiesPageFragment.class.getName()));
        fragments.add(Fragment.instantiate(getContext(), PsychologyPageFragment.class.getName()));
        this.personalizationPageAdapter  = new PersonalizationPageAdapter(getChildFragmentManager(), fragments);

        viewPager.setOnPageChangeListener(new CircularViewPagerHandler(viewPager));
        viewPager.setAdapter(this.personalizationPageAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        });

        initializePages();

        return inflatedView;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnPersonalizationFragmentInteractionListener) {
//            mListener = (OnPersonalizationFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnPersonalizationFragmentInteractionListener");
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
    public interface OnPersonalizationFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
