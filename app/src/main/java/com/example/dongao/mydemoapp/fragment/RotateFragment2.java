package com.example.dongao.mydemoapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dongao.mydemoapp.R;
import com.example.dongao.mydemoapp.rotate3d.SwitchCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RotateFragment2 extends Fragment implements View.OnClickListener {


    private SwitchCallback callback;
    private Button bt2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate_fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt2 = (Button) view.findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
        view.findViewById(R.id.tv2).setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt2:
                callback.switchOther(false);
                break;
            case R.id.tv2:
                Toast.makeText(getActivity(), "this is fragment2", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setSwitchCallback(SwitchCallback callback){
        this.callback=callback;
    }


    public void startRotate() {
        bt2.setEnabled(false);
    }

    public void centerRotate(){

    }

    public void endRotate(){
        bt2.setEnabled(true);
    }
}
