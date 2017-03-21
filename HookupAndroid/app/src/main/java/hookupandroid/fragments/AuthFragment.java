package hookupandroid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;
import hookupandroid.common.enums.FirebaseAuthMethod;
import hookupandroid.tasks.GetAllUserDataTask;
import hookupandroid.tasks.UpdateUserAuthToken;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAuthFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment implements Validator.ValidationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    int RC_SIGN_IN = 6;
    int FACEBOOK_REQUEST_CODE = 64206;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth auth;

    private OnAuthFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private Validator validator;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;

    @BindView(R.id.auth_scroll_view) ScrollView scrollView;
    @BindView(R.id.btn_signup) Button btnRegister;

    @NotEmpty
    @Email
    @BindView(R.id.input_login_email) EditText txtEmail;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @BindView(R.id.input_login_password) EditText txtPassword;

    @BindView(R.id.btn_login) Button btnLogin;

    @BindView(R.id.facebook_login_button) LoginButton facebookLoginButton;
    @BindView(R.id.google_sign_in_button) SignInButton googleSignInButton;

    View inflatedView = null;

    public AuthFragment() {
        // Required empty public constructor
    }

    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        String defaultWebClientId = getString(R.string.oauth_web_client_id);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.oauth_web_client_id))
//                .requestIdToken(getString(R.string.oauth_web_client_id))
                .requestEmail()
                .build();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }


        auth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_auth, container, false);
        unbinder = ButterKnife.bind(this,inflatedView);

        configureFirebaseAuthListener();
        configureFacebookLoginButton();

        return inflatedView;
    }


    @OnClick(R.id.btn_signup)
    public void onRegisterButtonClicked() {
        if (mListener != null) {
            mListener.onRegisterButtonClicked();
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginButtonClicked() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        validator.validate();
    }

    @OnClick(R.id.google_sign_in_button)
    public void onGoogleLoginButtonClicked() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if (requestCode == FACEBOOK_REQUEST_CODE){
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void configureFirebaseAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    new UpdateUserAuthToken().execute(token);
                    new GetAllUserDataTask(getActivity(), inflatedView, mListener).execute();
//                    mListener.onSuccessLogon();
                } else {
                    // User is signed out
                }
            }
        };
    }

    private void configureFacebookLoginButton() {
        if(facebookLoginButton != null) {
            facebookLoginButton.setReadPermissions("email");
            facebookLoginButton.setFragment(this);

            facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFirebaseAccessToken(loginResult.getAccessToken(), null, FirebaseAuthMethod.Facebook);
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });
        }
    }

    private void handleFirebaseAccessToken(AccessToken facebookAccessToken, GoogleSignInAccount googleAccount, FirebaseAuthMethod method) {
        AuthCredential credential = null ;

        if (method == FirebaseAuthMethod.Facebook) {
            credential = FacebookAuthProvider.getCredential(facebookAccessToken.getToken());
        }
        else if (method == FirebaseAuthMethod.Google) {
            credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
        }

        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        }
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            handleFirebaseAccessToken(null, acct, FirebaseAuthMethod.Google);
//            updateUI(true);
            // TODO: notify parent activity, and store info about user, so you may logout him on. Firebase.getUser doesnt work
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

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

    @Override
    public void onValidationSucceeded() {
//        Toast.makeText(getContext(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        auth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getContext(), "You have successfully logged in ", Toast.LENGTH_SHORT).show();
//                    String token = FirebaseInstanceId.getInstance().getToken();
//                    // TODO check if token equals token in SQLite db
//                    // TODO: remove (move) body of this if (isSuccessfull), because it should be all called on AuthListener in above method
//                    new UpdateUserAuthToken().execute(token);
////                    mListener.onSuccessLogon();
                }
                else {
                    Toast.makeText(getContext(), "Authentification failed. Try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        if (auth != null) {
            auth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        void onSuccessLogon();
    }
}
