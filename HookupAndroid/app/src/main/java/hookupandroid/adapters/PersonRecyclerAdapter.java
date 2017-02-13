package hookupandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hookupandroid.R;
import hookupandroid.model.Person;

/**
 * Created by Bandjur on 8/3/2016.
 */
public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.PersonViewHolder> {

    List<Person> persons;
    private LayoutInflater inflater;

    public PersonRecyclerAdapter(Context context, List<Person> data) {
        persons = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friend_list_item, parent, false);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        personViewHolder.setListeners();

        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = persons.get(position);
        holder.setData(person, position);
    }

    public void removeItem(int position) {
        persons.remove(position);
        notifyItemRemoved(position);
		notifyItemRangeChanged(position, persons.size());
//		notifyDataSetChanged(); // alt for 'notifyItemRangeChanged' it's resource costly and there is no animation on delete/add item
    }

    public void addItem(int position, Person person) {
        persons.add(position, person);
        notifyItemInserted(position);
		notifyItemRangeChanged(position, persons.size());
//		notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class PersonViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgProfile, imgDelete;
        private TextView txtFullname, txtHometown;

        int position;
        Person currentPerson;

        public PersonViewHolder(View itemView) {
            super(itemView);

            imgProfile = (ImageView) itemView.findViewById(R.id.img_profile);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_friend_delete);
            txtFullname = (TextView) itemView.findViewById(R.id.txt_friend_fullname);
            txtHometown = (TextView) itemView.findViewById(R.id.txt_friend_hometown);
        }

        public void setData(Person person, int position) {
            txtFullname.setText(person.getFirstname() + " " + person.getLastname());
            txtHometown.setText(person.getCity() + ", " + person.getCountry());
            imgProfile.setImageResource(person.getImage_id());

            this.position = position;
            this.currentPerson = person;
        }

        public void setListeners() {
            imgDelete.setOnClickListener(PersonViewHolder.this);
//            imgAdd.setOnClickListener(MyViewHolder.this);
//            imgThumb.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_friend_delete:
                    removeItem(position);
                    break;

//                case R.id.img_row_add:
//                    addItem(position, current);
//                    break;

            }
        }
    }
}
