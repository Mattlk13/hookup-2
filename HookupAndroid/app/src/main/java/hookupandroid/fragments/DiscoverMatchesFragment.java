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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.adapters.NonFriendsSwipeDeckAdapter;
import hookupandroid.common.CommonUtils;
import hookupandroid.model.User;
import hookupandroid.tasks.DislikeUserTask;
import hookupandroid.tasks.LikeUserTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { DiscoverMatchesFragment.OnViewFriendProfileInteractionListner} interface
 * to handle interaction events.
 * Use the {@link DiscoverMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverMatchesFragment extends Fragment {
    private static final String ARG_NON_FRIENDS = "non-friends";
    private List<User> nonFriends;

    private View inflatedView;
    private Unbinder unbinder;

    private User currentDisplayingUser;
    private List<Integer> listPositionsToDelete;

    @BindView(R.id.swipe_deck) SwipeDeck nonFriendsSwipeDeck;

    public DiscoverMatchesFragment() {
        // Required empty public constructor
        listPositionsToDelete = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiscoverMatchesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverMatchesFragment newInstance(ArrayList<User> nonFriends) {
        DiscoverMatchesFragment fragment = new DiscoverMatchesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NON_FRIENDS, nonFriends);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nonFriends = (List<User>) getArguments().getSerializable(ARG_NON_FRIENDS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_discover_matches, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        final NonFriendsSwipeDeckAdapter adapter = new NonFriendsSwipeDeckAdapter(nonFriends, getContext());
        nonFriendsSwipeDeck.setAdapter(adapter);

        if(nonFriends.size() > 0) {
            currentDisplayingUser = nonFriends.get(0);
        }

        nonFriendsSwipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
                new DislikeUserTask().execute(nonFriends.get(position).getFirebaseUID());
//                new DislikeUserTask().execute(currentDisplayingUser.getFirebaseUID());
//                currentDisplayingUser = nonFriends.get(position);
//                nonFriends.remove(position);
                listPositionsToDelete.add(position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                new LikeUserTask().execute(nonFriends.get(position).getFirebaseUID());
//                new LikeUserTask().execute(currentDisplayingUser.getFirebaseUID());
//                currentDisplayingUser = nonFriends.get(position);
//                nonFriends.remove(position);
                listPositionsToDelete.add(position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
                currentDisplayingUser = null;
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });

        nonFriendsSwipeDeck.setLeftImage(R.id.left_image);
        nonFriendsSwipeDeck.setRightImage(R.id.right_image);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for(int position : listPositionsToDelete) {
            if(NavDrawerMainActivity.nonFriends.get(position) != null) {
                NavDrawerMainActivity.nonFriends.remove(position);
            }
        }
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
