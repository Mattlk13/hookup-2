package hookupandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hookupandroid.R;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.fragments.PendingHookupsFragment;
import hookupandroid.model.User;

import java.util.List;

public class PendingHookupRecyclerAdapter extends RecyclerView.Adapter<PendingHookupRecyclerAdapter.PendingHookupViewHolder> {

    private final List<User> persons;
    private final PendingHookupsFragment.OnPendingHookupInteractionListener mListener;

    public PendingHookupRecyclerAdapter(List<User> items, PendingHookupsFragment.OnPendingHookupInteractionListener listener) {
        persons = items;
        mListener = listener;
    }

    @Override
    public PendingHookupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_hookup_list_item, parent, false);

        return new PendingHookupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PendingHookupViewHolder holder, int position) {
//        Person person = persons.get(position);
        holder.mItem = persons.get(position);
        holder.mItem.setTempPosition(position);
        holder.setData();
        holder.mItem.setPersonRelation(PersonRelation.PENDING);
//        int mod = position%3;

//        if(mod == 0) {
//            holder.mItem.setPersonRelation(PersonRelation.NON_FRIEND);
//        }
//        else if (mod == 1) {
//            holder.mItem.setPersonRelation(PersonRelation.FRIEND);
//        }
//        else {
//            holder.mItem.setPersonRelation(PersonRelation.PENDING);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPendingHookupItemClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class PendingHookupViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_pending_fullname) TextView txtFullname;
        @BindView(R.id.txt_pending_hometown) TextView txtHometown;
        @BindView(R.id.img_pending_profile) ImageView imgProfile;
//        @BindView(R.id.layout_pending_accept) ViewGroup layoutAcceptPending;
//        @BindView(R.id.layout_pending_decline) ViewGroup layoutDecinePending;

        View viewHolder;

        User mItem;

        public PendingHookupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            viewHolder = view; // TO DO delete this
        }

        @OnClick(R.id.layout_pending_accept)
        public void OnPendingAcceptClicked() {
            Toast.makeText(viewHolder.getContext(), "fuck yeah ... accept", Toast.LENGTH_SHORT).show();
            removeItem();
        }

        @OnClick(R.id.layout_pending_decline)
        public void OnPendingDeclineClicked() {
            Toast.makeText(viewHolder.getContext(), "fuck yeah ... decline", Toast.LENGTH_SHORT).show();
            removeItem();
        }

        private void removeItem() {
            persons.remove(mItem.getTempPosition());
            notifyItemRemoved(mItem.getTempPosition());
            notifyItemRangeChanged(mItem.getTempPosition(), persons.size());
        }

        public void setData() {
            txtFullname.setText(mItem.getFirstname() + " pending " + mItem.getLastname());
            txtHometown.setText(mItem.getCity() + ", " + mItem.getCountry());
        }

        @Override
        public void onClick(View v) {

        }
    }

}
