package com.sg.eyedoctor.common.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sg.eyedoctor.common.bean.Doctor;
import com.sg.eyedoctor.common.manager.BaseManager;

import org.xutils.x;

/**
 * Created by wyouflf on 15/11/4.
 */
public abstract class CommFragment extends Fragment {
    protected static Doctor mDoctor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDoctor= BaseManager.getDoctor();
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        x.view().inject(this, this.getView());

        initView();
        initListener();
    }


    protected abstract void initView();

    protected abstract void initListener();







}
