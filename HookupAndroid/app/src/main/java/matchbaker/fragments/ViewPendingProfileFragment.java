package matchbaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import matchbaker.R;
import matchbaker.model.User;
import matchbaker.tasks.UnfriendUserTask;
import matchbaker.tasks.UpdateHookupResponseTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnViewPendingProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPendingProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPendingProfileFragment extends Fragment {

    private User pendingProfile;

    private OnViewPendingProfileInteractionListener mListener;
    private View inflatedView;
    private Unbinder unbinder;

    @BindView(R.id.txt_pending_friend_view_fullname_and_age) TextView txtPersonFullname;
    @BindView(R.id.txt_pending_friend_city) TextView txtPersonCity;
    @BindView(R.id.txt_pending_friend_career) TextView txtPesonCareer;
    @BindView(R.id.txt_pending_friend_view_about_me) TextView txtPersonAboutMe;

    public ViewPendingProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewPendingProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPendingProfileFragment newInstance() {
        ViewPendingProfileFragment fragment = new ViewPendingProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pendingProfile = (User) getArguments().getSerializable("personData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_view_pending_profile, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);
        setViewDetails(pendingProfile);

        return inflatedView;
    }

    private void setViewDetails(User friend) {
        txtPersonFullname.setText(friend.getFirstname() + " " + friend.getLastname() + ", " + friend.getAge());
        txtPersonCity.setText(friend.getCity());
        txtPesonCareer.setText(friend.getBasicInfo().getCareer());
        txtPersonAboutMe.setText(friend.getAboutMe());
    }

    @OnClick(R.id.img_pending_profile_accept)
    public void onHeartIconClicked() {
        new UpdateHookupResponseTask().execute(new String[]{FirebaseAuth.getInstance().getCurrentUser().getUid(), pendingProfile.getFirebaseUID()});

        if(mListener != null) {
            mListener.onPendingHookupResponseAction(pendingProfile);
        }
    }

    @OnClick(R.id.img_pending_profile_decline)
    public void onBrokenHeartClicked() {
        new UnfriendUserTask().execute(pendingProfile.getFirebaseUID());

        if(mListener != null) {
            mListener.onPendingHookupResponseAction(pendingProfile);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof  OnViewPendingProfileInteractionListener) {
            mListener = (OnViewPendingProfileInteractionListener) context;
        }
        else if(getActivity() instanceof OnViewPendingProfileInteractionListener) {
            mListener = (OnViewPendingProfileInteractionListener) getActivity();
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewNonFriendProfileInteractionListener");
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
    public interface OnViewPendingProfileInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
        void onPendingHookupResponseAction(User pendingProfile);
//        void onPendingHookupResponseAction();
    }
}
