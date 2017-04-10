package matchbaker.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import matchbaker.R;
import matchbaker.model.UserData;
import matchbaker.tasks.RegisterUserTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSignupFragmentInteractionListner} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment implements Validator.ValidationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnSignupFragmentInteractionListner mListener;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FirebaseAuth auth;

    private Unbinder unbinder;
    private View inflatedView;
    private Validator validator;

    private DatePickerDialog birthDatePickerDialog;
    private CountryPicker countryPicker;


    @NotEmpty
    @Email
    @BindView(R.id.input_signup_email)
    EditText emailEditText;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @BindView(R.id.input_signup_password)
    EditText passwordEditText;

    @ConfirmPassword
    @BindView(R.id.input_signup_repeat_password)
    EditText repeatPasswordEditText;

    @BindView(R.id.txt_signup_country)
    TextView txtCountry;
    @BindView(R.id.img_signup_country)
    ImageView imgCountry;


    @BindView(R.id.input_signup_city) EditText cityEditText;

    @NotEmpty
    @BindView(R.id.input_signup_firstname)
    EditText firstnameEditText;
    @NotEmpty
    @BindView(R.id.input_signup_lastname)
    EditText lastnameEditText;

    @BindView(R.id.radioF) RadioButton femaleRadioButton;

//    @NotEmpty
//    @Min(value = 18, message = "Should be greather than 18 years")
//    @BindView(R.id.input_signup_age)
//    EditText ageEditText;

    @NotEmpty
    @BindView(R.id.input_signup_birthday)
    EditText birthdayEditText;

    @NotEmpty
    @BindView(R.id.input_signup_about_me)
    EditText aboutMeEditText;


    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        auth = FirebaseAuth.getInstance();

        validator = new Validator(this);
        validator.setValidationListener(this);

        countryPicker = CountryPicker.newInstance("Select Country");
        countryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                txtCountry.setText(name);
                imgCountry.setImageResource(flagDrawableResID);
                countryPicker.dismiss();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthdayEditText.setText(new SimpleDateFormat("dd.MM.yyyy").format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        birthdayEditText.setInputType(InputType.TYPE_NULL);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onValidationSucceeded() {
        auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "You have successfully registered ", Toast.LENGTH_SHORT).show();
                    String token = FirebaseInstanceId.getInstance().getToken();

                    if(mLastLocation == null) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                    }

                    RegisterUserTask registerAsyncTask = new RegisterUserTask(getContext());
                    UserData data = new UserData();
                    data.setEmail(emailEditText.getText().toString());
                    data.setUid(auth.getCurrentUser().getUid());
                    if(mLastLocation != null) {
                        data.setLatitude(String.valueOf(mLastLocation.getLatitude()));
                        data.setLongitude(String.valueOf(mLastLocation.getLongitude()));
                    }
                    data.setFirstname(firstnameEditText.getText().toString());
                    data.setLastname(lastnameEditText.getText().toString());
                    data.setToken(token);
//                    data.setAge(ageEditText.getText().toString());
                    data.setBirthDay(birthdayEditText.getText().toString());
                    if(femaleRadioButton.isChecked()) {
                        data.setGender("Female");
                    }
                    else {
                        data.setGender("Male");
                    }
                    data.setCity(cityEditText.getText().toString());
                    if(!txtCountry.getText().toString().isEmpty()) {
                        data.setCountry(txtCountry.getText().toString());
                    }
                    data.setAboutMe(aboutMeEditText.getText().toString());

                    registerAsyncTask.execute(data);

                    mListener.onSuccessRegistration();
                } else {
                    Toast.makeText(getContext(), "Registration failed. Try again...", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.btn_register)
    public void register() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        validator.validate();
    }

    @OnClick(R.id.signup_layout_country)
    public void selectCountryOnClick() {
        countryPicker.show(getFragmentManager(), "COUNTRY_PICKER");
    }

    @OnClick(R.id.input_signup_birthday)
    public void openDateDialogOnClick() {
        birthDatePickerDialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // TODO: Add TextWatcher for realtime validation...

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignupFragmentInteractionListner) {
            mListener = (OnSignupFragmentInteractionListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSignupFragmentInteractionListner {
        // TODO: Update argument type and name
        void onSuccessRegistration();
    }
}
