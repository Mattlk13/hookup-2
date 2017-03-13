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

    @BindView(R.id.txt_pending_view_fullname) TextView txtPersonFullname;

    public ViewPendingProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPendingProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPendingProfileFragment newInstance(String param1, String param2) {
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

    private void setViewDetails(User person) {
        txtPersonFullname.setText(person.getFirstname() + " " + person.getLastname());
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
    }
}
