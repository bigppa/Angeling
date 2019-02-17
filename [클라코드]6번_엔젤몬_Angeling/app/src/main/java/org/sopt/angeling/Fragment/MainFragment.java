package org.sopt.angeling.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.sopt.angeling.Activity.MainActivity;
import org.sopt.angeling.R;

/**
 * Created by DongHyun on 2016-01-13.
 */
public class MainFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        initView(view);

        return view;
    }


    private void initView(View view){
        ImageView img_dog = (ImageView)view.findViewById(R.id.img1);
        ImageView img_blood = (ImageView)view.findViewById(R.id.img2);
        ImageView img_local = (ImageView)view.findViewById(R.id.img3);
        ImageView img_knowledge = (ImageView)view.findViewById(R.id.img4);
        ImageView img_global = (ImageView)view.findViewById(R.id.img5);
        ImageView img_talent = (ImageView)view.findViewById(R.id.img6);

        img_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(MainActivity.THEME_ENV);
            }
        });
        img_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(MainActivity.THEME_EDU);
            }
        });
        img_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(MainActivity.THEME_LOCAL);
            }
        });
        img_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(MainActivity.THEME_ALI);
            }
        });
        img_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(MainActivity.THEME_LIVING);
            }
        });
        img_talent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListFragment(MainActivity.THEME_OTHER);
            }
        });
    }

    private void gotoListFragment(int themeNum){
        Fragment newFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("search", false);
        bundle.putInt("themeNum",themeNum);
        newFragment.setArguments(bundle);
        ((MainActivity) getActivity()).fm = getActivity().getFragmentManager();
        ((MainActivity) getActivity()).ft = ((MainActivity) getActivity()).fm.beginTransaction();
        ((MainActivity) getActivity()).ft.replace(R.id.fragment_place, newFragment);
        ((MainActivity) getActivity()).spinner.setEnabled(true);
        ((MainActivity) getActivity()).ft.commit();
        ((MainActivity) getActivity()).goMain = true;
    }

}
