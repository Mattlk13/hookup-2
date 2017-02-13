package hookupandroid.fragments;

import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.common.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { DiscoverMatchesFragment.OnViewFriendProfileInteractionListner} interface
 * to handle interaction events.
 * Use the {@link DiscoverMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverMatchesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    @BindView(R.id.authScrollview) ScrollView scrollView;
//    @BindView(R.id.authScrollview) ScrollView scrollView;
    @BindView(R.id.btnPlayNotification) Button btnPlayNotificaion;

    private View inflatedView;
    private Unbinder unbinder;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnViewFriendProfileInteractionListner mListener;

    public DiscoverMatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverMatchesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverMatchesFragment newInstance(String param1, String param2) {
        DiscoverMatchesFragment fragment = new DiscoverMatchesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_discover_matches, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @OnClick(R.id.btnPlayNotification)
    public void onPlayNotificationButtonClicked() {

        String notifications_new_message_ringtone = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("notifications_new_message_ringtone", "ffs");
//        Uri soundUri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, notifications_new_message_ringtone);
        Uri soundUri = Uri.parse(notifications_new_message_ringtone);
        String realAudioPath = CommonUtils.getRealAudioPathFromURI(getActivity(), soundUri);

        MediaPlayer mp = MediaPlayer.create(getActivity(), Uri.parse(realAudioPath));
        mp.start();
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
//    public interface OnViewFriendProfileInteractionListner {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
