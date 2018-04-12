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
public class RotateFragment1 extends Fragment implements View.OnClickListener {


    private SwitchCallback callback;
    private Button bt1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate_fragment1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt1 = (Button) view.findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        view.findViewById(R.id.tv1).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt1:
                callback.switchOther(true);
                break;
            case R.id.tv1:
                Toast.makeText(getActivity(), "this is fragment1", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setSwitchCallback(SwitchCallback callback){
        this.callback=callback;
    }

    public void startRotate() {
        bt1.setEnabled(false);
    }

    public void centerRotate(){

    }

    public void endRotate(){
        bt1.setEnabled(true);
    }
}
