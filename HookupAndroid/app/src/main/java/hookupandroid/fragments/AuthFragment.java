package hookupandroid.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAuthFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnAuthFragmentInteractionListener mListener;
    private Unbinder unbinder;

    @BindView(R.id.auth_scroll_view) ScrollView scrollView;
//    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btnSignup) Button btnRegister;
    @BindView(R.id.input_login_email) EditText txtEmail;
    @BindView(R.id.input_login_password) EditText txtPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.imgFacebookLogin) ImageView imgFacebookLogin;
    @BindView(R.id.imgGoogleLogin) ImageView imgGoogleLogin;

    View inflatedView = null;

    public AuthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        inflatedView = inflater.inflate(R.layout.fragment_auth, container, false);
        unbinder = ButterKnife.bind(this,inflatedView);

        return inflatedView;
    }

    @OnClick(R.id.btnSignup)
    public void onRegisterButtonClicked() {
        if (mListener != null) {
            mListener.onRegisterButtonClicked();
        }
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onTestButtonPressed() {
//        if (mListener != null) {
//            mListener.onRegisterButtonClicked("Button from Auth Fragment has just been clicked");
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAuthFragmentInteractionListener) {
            mListener = (OnAuthFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAuthFragmentInteractionListener");
        }
    }

//    @OnFocusChange(R.id.input_email)
//    public void onEmailInputFocusChanged(boolean focused) {
//        if (focused) {
//            scrollView.scrollTo(0, txtEmail.getBottom());
//        }
//        else {
//            int a = 1;
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind(); // set views = null, Butter knifes does it for all views
    }

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
    public interface OnAuthFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRegisterButtonClicked();
    }
}
