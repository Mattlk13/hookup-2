package hookupandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import hookupandroid.R;
import hookupandroid.adapters.PersonRecyclerAdapter;
import hookupandroid.model.Person;

public class PersonRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_recycler_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My hookups");
        setSupportActionBar(toolbar);

        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerView personRecylerView = (RecyclerView) findViewById(R.id.persons_recycler_view);
        PersonRecyclerAdapter adapter=  new PersonRecyclerAdapter(this, getPersonsTestData());
        personRecylerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        personRecylerView.setLayoutManager(mLinearLayoutManagerVertical);

        personRecylerView.setItemAnimator(new DefaultItemAnimator()); // Even if we dont use it then also our items shows default animation. #Check Docs
    }


    private List<Person> getPersonsTestData() {
        List<Person> retList = new ArrayList<>();

        Person p = new Person();

        p.setFirstname("Jovana");
        p.setLastname("Jovanic");
        p.setCountry("Serbia");
        p.setCity("Novi Sad");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Tijana");
        p.setLastname("Tijanic");
        p.setCountry("Serbia");
        p.setCity("Belgrade");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Milica");
        p.setLastname("Milic");
        p.setCountry("Serbia");
        p.setCity("Nis");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Ema");
        p.setLastname("Caswell");
        p.setCountry("UK");
        p.setCity("London");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Jovana");
        p.setLastname("Jovanic");
        p.setCountry("Serbia");
        p.setCity("Novi Sad");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Tanja");
        p.setLastname("Kuznyakova");
        p.setCountry("Russia");
        p.setCity("Moscow");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Anna");
        p.setLastname("Zimmer");
        p.setCountry("Germany");
        p.setCity("Berlin");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Sara");
        p.setLastname("Vukovic");
        p.setCountry("Croatia");
        p.setCity("Zagreb");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        p = new Person();
        p.setFirstname("Sanja");
        p.setLastname("Stankovic");
        p.setCountry("BiH");
        p.setCity("Brcko");
        p.setImage_id(R.drawable.user1);
        retList.add(p);

        return retList;
    }
}
