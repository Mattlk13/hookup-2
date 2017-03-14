package hookupandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.model.Person;
import hookupandroid.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnViewNonFriendProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewNonFriendProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewNonFriendProfileFragment extends Fragment {

    private User person;

    private OnViewNonFriendProfileInteractionListener mListener;
    private View inflatedView;
    private Unbinder unbinder;

    @BindView(R.id.txt_non_friend_view_fullname_and_age) TextView txtPersonFullname;
    @BindView(R.id.txt_non_friend_city) TextView txtPersonCity;
    @BindView(R.id.txt_non_friend_career) TextView txtPesonCareer;
    @BindView(R.id.txt_non_friend_view_about_me) TextView txtPersonAboutMe;

    public ViewNonFriendProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewNonFriendProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewNonFriendProfileFragment newInstance(String param1, String param2) {
        ViewNonFriendProfileFragment fragment = new ViewNonFriendProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = (User) getArguments().getSerializable("personData");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView =  inflater.inflate(R.layout.fragment_view_non_friend_profile, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);
        setViewDetails(person);

        return inflatedView;
    }

    private void setViewDetails(User friend) {
        txtPersonFullname.setText(friend.getFirstname() + " " + friend.getLastname() + ", " + friend.getAge());
        txtPersonCity.setText(friend.getCity());
        txtPesonCareer.setText(friend.getBasicInfo().getCareer());
        txtPersonAboutMe.setText(friend.getAboutMe());
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnViewNonFriendProfileInteractionListener) {
//            mListener = (OnViewNonFriendProfileInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnViewNonFriendProfileInteractionListener");
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
    public interface OnViewNonFriendProfileInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
