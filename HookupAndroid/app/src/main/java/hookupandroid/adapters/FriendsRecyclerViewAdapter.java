package hookupandroid.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hookupandroid.R;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.fragments.FriendsFragment;
import hookupandroid.model.User;
import hookupandroid.tasks.UnfriendUserTask;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> implements FriendsFragment.OnFriendsListFragmentInteractionListener {

    private final List<User> friends;
    private final FriendsFragment.OnFriendsListFragmentInteractionListener mListener;
//    private final FriendsFragment.OnFriendsListPlacePickerListener mPlacePickerListener;
    private FriendsFragment mFriendsFragment;

    //    public FriendsRecyclerViewAdapter(List<User> items
//            , FriendsFragment.OnFriendsListFragmentInteractionListener listener,
//            FriendsFragment.OnFriendsListPlacePickerListener placeListener) {
    public FriendsRecyclerViewAdapter(List<User> items,
                  FriendsFragment.OnFriendsListFragmentInteractionListener listener,
                  FriendsFragment parentFragment) {
        friends = items;
        mListener = listener;
        mFriendsFragment = parentFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = friends.get(position);
        holder.mItem.setTempPosition(position);
        holder.mItem.setPersonRelation(PersonRelation.FRIEND);

        holder.setPersonData();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPersonViewClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public void onPersonViewClicked(User item) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User mItem;

        @BindView(R.id.txt_friend_fullname)
        TextView txtFullname;
        @BindView(R.id.txt_friend_hometown)
        TextView txtHometown;
//        @BindView(R.id.img_friend_list_item_delete)
//        ImageView imgDelete;
        @BindView(R.id.layout_friend_delete) LinearLayout unfriendLayout;
        @BindView(R.id.text_friends_date)
        TextView txtFriendsDate;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setPersonData() {
            txtFullname.setText(mItem.getFirstname() + " " + mItem.getLastname());
            txtHometown.setText(mItem.getCity() + ", " + mItem.getCountry());
//            txtFriendsDate.setText(mItem.getFriendsDate().toString());'
            if(mItem.getFriendsDate() != null) {
                txtFriendsDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(mItem.getFriendsDate()));
            }
        }

//        @OnClick(R.id.img_friend_list_item_delete)
//        public void unfriend() {
//            new AlertDialog.Builder(imgDelete.getContext())
//                    .setTitle("Unfriend")
//                    .setMessage("Are you sure you want to unfriend this person?")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            new UnfriendUserTask().execute(mItem.getFirebaseUID());
//                            removeItem();
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }

//        @OnClick(R.id.img_friend_place_finder)
//        public void suggestPlace() {
//            if(mFriendsFragment != null) {
//                mFriendsFragment.suggestPlace(mItem);
//            }
//        }

        @OnClick(R.id.layout_friend_delete)
        public void unfriend(){
            new AlertDialog.Builder(unfriendLayout.getContext())
                    .setTitle("Unfriend")
                    .setMessage("Are you sure you want to unfriend this person?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new UnfriendUserTask().execute(mItem.getFirebaseUID());
                            removeItem();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @OnClick(R.id.layout_friend_meetup)
        public void suggestPlace() {
            if(mFriendsFragment != null) {
                mFriendsFragment.suggestPlace(mItem);
            }
        }

        private void removeItem() {
            friends.remove(mItem.getTempPosition());
            notifyItemRemoved(mItem.getTempPosition());
            notifyItemRangeChanged(mItem.getTempPosition(), friends.size());
        }

    }
}
