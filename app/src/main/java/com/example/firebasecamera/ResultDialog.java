package com.example.firebasecamera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultDialog extends DialogFragment {

    private Button clic;
    private TextView result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragmentlayout,container,false);
        String resultText;
        clic=view.findViewById(R.id.b2);
        result=view.findViewById(R.id.text1);

        Bundle bundle=getArguments();
        resultText=bundle.getString(LCOapplication.RESULT_TEXT);
        result.setText(resultText);

        clic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
