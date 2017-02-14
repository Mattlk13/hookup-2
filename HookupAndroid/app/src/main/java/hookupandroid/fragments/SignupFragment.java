package hookupandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hookupandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSignupFragmentInteractionListner} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment implements Validator.ValidationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Unbinder unbinder;
    private View inflatedView;
    private Validator validator;

    private CountryPicker countryPicker;


    @NotEmpty
    @Email
    @BindView(R.id.input_signup_email) EditText emailEditText;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    @BindView(R.id.input_signup_password) EditText passwordEditText;

    @ConfirmPassword
    @BindView(R.id.input_signup_repeat_password) EditText repeatPasswordEditText;

    @BindView(R.id.txt_signup_country) TextView txtCountry;
    @BindView(R.id.img_signup_country) ImageView imgCountry;

    @NotEmpty
    @BindView(R.id.input_signup_firstname) EditText firstnameEditText;
    @NotEmpty
    @BindView(R.id.input_signup_lastname) EditText lastnameEditText;


    @NotEmpty
    @Min(value = 18, message = "Should be greather than 18 years")
    @BindView(R.id.input_signup_age) EditText ageEditText;

    private OnSignupFragmentInteractionListner mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(getContext(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        // TODO: firebase call + serverCall
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
    public void Register() {
        validator.validate();
    }

    @OnClick(R.id.signup_layout_country)
    public void SelectCountryOnClick() {
        countryPicker.show(getFragmentManager(), "COUNTRY_PICKER");
    }

    // TODO: Add TextWatcher for realtime validation...


    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnSignupFragmentInteractionListner) {
//            mListener = (OnSignupFragmentInteractionListner) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnHomeFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
//        void onFragmentInteraction(Uri uri);
    }
}
