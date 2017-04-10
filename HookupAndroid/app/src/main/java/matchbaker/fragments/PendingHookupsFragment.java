package matchbaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import matchbaker.R;
import matchbaker.adapters.PendingHookupRecyclerAdapter;
import matchbaker.model.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPendingHookupInteractionListener}
 * interface.
 */
public class PendingHookupsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PENDING_HOOKUPS = "pending-hookups";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnPendingHookupInteractionListener mListener;
    private ArrayList<User> pendingHookups;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PendingHookupsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PendingHookupsFragment newInstance(int columnCount, ArrayList<User> pendingHookups) {
        PendingHookupsFragment fragment = new PendingHookupsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(ARG_PENDING_HOOKUPS, pendingHookups);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            pendingHookups = (ArrayList<User>) getArguments().getSerializable(ARG_PENDING_HOOKUPS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_hookups_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if(pendingHookups != null) {
                recyclerView.setAdapter(new PendingHookupRecyclerAdapter(pendingHookups, mListener));
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPendingHookupInteractionListener) {
            mListener = (OnPendingHookupInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPendingHookupInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPendingHookupInteractionListener {
        // TODO: Update argument type and name
//        void onListFragmentInteraction(Person item);
        void onPendingHookupItemClicked(User item);
    }
}
