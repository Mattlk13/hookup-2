package hookupandroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hookupandroid.R;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.fragments.FriendsFragment;
import hookupandroid.model.Person;
import hookupandroid.model.User;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> implements FriendsFragment.OnFriendsListFragmentInteractionListener {

    private final List<User> mValues;
    private final FriendsFragment.OnFriendsListFragmentInteractionListener mListener;

    public FriendsRecyclerViewAdapter(List<User> items, FriendsFragment.OnFriendsListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mItem.setTempPosition(position);
        holder.mItem.setPersonRelation(PersonRelation.FRIEND);

//        int mod = position%3;
//
//        if(mod == 0) {
//            holder.mItem.setPersonRelation(PersonRelation.NON_FRIEND);
//        }
//        else if (mod == 1) {
//            holder.mItem.setPersonRelation(PersonRelation.FRIEND);
//        }
//        else {
//            holder.mItem.setPersonRelation(PersonRelation.PENDING);
//        }

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
        return mValues.size();
    }

    @Override
    public void onPersonViewClicked(User item) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User mItem;

        @BindView(R.id.txt_friend_fullname) TextView txtFullname;
        @BindView(R.id.txt_friend_hometown) TextView txtHometown;
        @BindView(R.id.img_friend_delete) ImageView imgDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setPersonData() {
            txtFullname.setText(mItem.getFirstname() + " " + mItem.getLastname());
            txtHometown.setText(mItem.getCity() + ", " + mItem.getCountry());
        }

    }
}
