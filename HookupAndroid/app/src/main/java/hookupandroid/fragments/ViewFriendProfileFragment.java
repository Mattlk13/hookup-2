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
import hookupandroid.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnViewFriendProfileInteractionListner} interface
 * to handle interaction events.
 * Use the {@link ViewFriendProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFriendProfileFragment extends Fragment {

    private User friend;

    private Unbinder unbinder;

    @BindView(R.id.txt_friend_view_fullname_and_age) TextView txtPersonFullname;
    @BindView(R.id.txt_friend_city) TextView txtPersonCity;
    @BindView(R.id.txt_friend_career) TextView txtPesonCareer;
    @BindView(R.id.txt_friend_view_about_me) TextView txtPersonAboutMe;

    private OnViewFriendProfileInteractionListner mListener;

    public ViewFriendProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewFriendProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFriendProfileFragment newInstance(String param1, String param2) {
        ViewFriendProfileFragment fragment = new ViewFriendProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            friend = (User) getArguments().getSerializable("personData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_view_friend_profile, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        setViewDetails(friend);

        return inflatedView;
    }

    private void setViewDetails(User friend) {
        txtPersonFullname.setText(friend.getFirstname() + " " + friend.getLastname() + ", " + friend.getAge());
        txtPersonCity.setText(friend.getCity());
        txtPesonCareer.setText(friend.getBasicInfo().getCareer());
        txtPersonAboutMe.setText(friend.getAboutMe());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnViewFriendProfileInteractionListner) {
//            mListener = (OnViewFriendProfileInteractionListner) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnViewFriendProfileInteractionListner");
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
    public interface OnViewFriendProfileInteractionListner {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
