package hookupandroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hookupandroid.R;
import hookupandroid.fragments.FriendsFragment;
import hookupandroid.model.Person;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Person} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> implements FriendsFragment.OnFriendsListFragmentInteractionListener {

    private final List<Person> mValues;
    private final FriendsFragment.OnFriendsListFragmentInteractionListener mListener;

    public FriendsRecyclerViewAdapter(List<Person> items, FriendsFragment.OnFriendsListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
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
    public void onPersonViewClicked(Person item) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFullname;
        public TextView txtHometown;
        public Person mItem;

        public ViewHolder(View view) {
            super(view);
//            imgProfile = (ImageView) itemView.findViewById(R.id.img_profile);
//            imgDelete = (ImageView) itemView.findViewById(R.id.img_row_delete);
            txtFullname = (TextView) view.findViewById(R.id.txtFullname);
            txtHometown = (TextView) view.findViewById(R.id.txtHometown);
        }

        public void setPersonData() {
            txtFullname.setText(mItem.getFirstname() + " " + mItem.getLastname());
            txtHometown.setText(mItem.getCity() + ", " + mItem.getCountry());
        }

    }
}
