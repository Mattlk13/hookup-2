package hookupandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import hookupandroid.R;
import hookupandroid.model.User;

/**
 * Created by Bandjur on 3/23/2017.
 */

public class NonFriendsSwipeDeckAdapter extends BaseAdapter {

    private List<User> nonFriends;
    private Context context;

    public NonFriendsSwipeDeckAdapter(List<User> nonFriends, Context context) {
        this.nonFriends = nonFriends;
        this.context = context;
    }

    @Override
    public int getCount() {
        return nonFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return nonFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            // normally use a viewholder
            v = inflater.inflate(R.layout.non_friend_card_view, parent, false);
        }

        User nonFriend = nonFriends.get(position);
        ((TextView) v.findViewById(R.id.txt_non_friend_swipe_view_fullname_and_age)).setText(nonFriend.getFirstname() + " " + nonFriend.getLastname() + ", " + nonFriend.getAge());
        ((TextView) v.findViewById(R.id.txt_non_friend_swipe_view_city)).setText(nonFriend.getCity());
        ((TextView) v.findViewById(R.id.txt_non_friend_swipe_view_career)).setText(nonFriend.getBasicInfo().getCareer());
        ((TextView) v.findViewById(R.id.txt_non_friend_swipe_view_about_me)).setText(nonFriend.getAboutMe());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String item = (String)getItem(position);
//                Log.i("MainActivity", item);
            }
        });

        return v;
    }

}
