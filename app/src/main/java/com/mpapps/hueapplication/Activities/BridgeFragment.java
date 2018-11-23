package com.mpapps.hueapplication.Activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.R;

/**
 * * Activities that contain this fragment must implement the
 * {@link BridgeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BridgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BridgeFragment extends DialogFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private EditText nameEditText;
    private EditText ipEditText;

    private OnFragmentInteractionListener mListener;

    public BridgeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *@return A new instance of fragment BridgeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BridgeFragment newInstance(Context context)
    {
        BridgeFragment fragment = new BridgeFragment();
        Bundle args = new Bundle();
        fragment.onAttach(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.bridge_fragment_edittext_name);
        ipEditText = view.findViewById(R.id.bridge_fragment_edittext_ip);

        Button addButton = view.findViewById(R.id.bridge_fragment_button_add);
        addButton.setOnClickListener((View v) -> onButtonPressed(v));
    }

    public void onButtonPressed(View view)
    {
        if (mListener != null) {
            Bridge bridge = new Bridge(nameEditText.getText().toString(), ipEditText.getText().toString());
            mListener.onFragmentInteraction(bridge);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Bridge bridge);
    }
}
