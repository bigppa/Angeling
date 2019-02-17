package org.sopt.angeling.Fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.sopt.angeling.Activity.MainActivity;
import org.sopt.angeling.Controller.CCAdapter;
import org.sopt.angeling.Controller.DBManager;
import org.sopt.angeling.Model.CCitem;
import org.sopt.angeling.R;

import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by DongHyun on 2016-01-13.
 */
public class CountryFragment extends Fragment {

    private DBManager dbAdapter;
    private ArrayList<CCitem> ccitemsFirst = null;
    private ArrayList<CCitem> ccitemsSecond = null;
    private ArrayList<CCitem> ccitemsThird = null;
    String selectedCity = null, selectedCountry =null;
    ListView gunguListView_1, gunguListView_2, gunguListView_3;
    private CCAdapter adapterFirst, adapterSecond, adapterThird;
    TextView tv1,tv2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.country_fragment, container, false);

        /* 번들로 받을것*/

        Bundle extra = getArguments();
        selectedCity = extra.getString("selectedCity");

        this.dbAdapter = new DBManager(getActivity().getApplicationContext());
        dbAdapter.open();
        if(dbAdapter.fetchAllNotes().getCount()==0) {
            copyExcelDataToDatabase();
        }

        tv1 = (TextView)view.findViewById(R.id.textView_country1);
        tv2 = (TextView)view.findViewById(R.id.textView_city1);

        Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/BMJUA_ttf.ttf");
        tv1.setTypeface(font);
        tv2.setTypeface(font);


        gunguListView_1 = (ListView)view.findViewById(R.id.country_list_1);
        gunguListView_2 = (ListView)view.findViewById(R.id.country_list_2);
        gunguListView_3 = (ListView)view.findViewById(R.id.country_list_3);

        dbAdapter.open();

        initModel();
        getCountryList(selectedCity);

        gunguListView_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CCitem ccitem_temp = (CCitem) adapterFirst.getItem(position);
                selectedCountry = ccitem_temp.region;

                //fragment 넘겨야함 -> LocationFragment
                initModel();
                gotoCountryFragment(selectedCountry);
            }
        });

        gunguListView_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CCitem ccitem_temp = (CCitem) adapterSecond.getItem(position);
                selectedCountry = ccitem_temp.region;
                initModel();
                gotoCountryFragment(selectedCountry);
            }
        });

        gunguListView_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CCitem ccitem_temp = (CCitem) adapterThird.getItem(position);
                selectedCountry = ccitem_temp.region;
                initModel();
                gotoCountryFragment(selectedCountry);
            }
        });



        return view;
    }

    private void gotoCountryFragment(String selectedCountry){
        LocationFragment newFragment = new LocationFragment();

        Bundle bundle = new Bundle();
        bundle.putString("selectedCity",selectedCity);
        bundle.putString("selectedCountry", selectedCountry);
        newFragment.setArguments(bundle);

        ((MainActivity) getActivity()).fm = getActivity().getFragmentManager();
        ((MainActivity) getActivity()).ft = ((MainActivity) getActivity()).fm.beginTransaction();
        ((MainActivity) getActivity()).ft.replace(R.id.fragment_place, newFragment);
        ((MainActivity) getActivity()).ft.addToBackStack(null);
        ((MainActivity) getActivity()).stack_size ++;
        ((MainActivity) getActivity()).spinner.setEnabled(true);
        ((MainActivity) getActivity()).ft.commit();
        ((MainActivity) getActivity()).goMain = true;
    }


    private void initModel() {
        ccitemsFirst = new ArrayList<>();
        ccitemsSecond = new ArrayList<>();
        ccitemsThird = new ArrayList<>();
    }


    public void getCountryList(String region) {
        dbAdapter.open();
        Cursor result = dbAdapter.selectGUNGU(region);
        result.moveToFirst();

        int evenORodd = 1;
        while (!result.isAfterLast()) {
            String sidostr = result.getString(0);
            if(evenORodd % 3 == 1) {
                CCitem ccitem_submit = new CCitem();
                ccitem_submit.region = sidostr;
                ccitemsFirst.add(0, ccitem_submit);
                evenORodd++;
            }
            else if (evenORodd % 3 == 2){
                CCitem ccitem_submit = new CCitem();
                ccitem_submit.region = sidostr;
                ccitemsSecond.add(0, ccitem_submit);
                evenORodd++;
            }
            else {
                CCitem ccitem_submit = new CCitem();
                ccitem_submit.region = sidostr;
                ccitemsThird.add(0, ccitem_submit);
                evenORodd++;
            }
            //adapter.notifyDataSetChanged();
            result.moveToNext();
        }
        adapterFirst = new CCAdapter(ccitemsFirst, getActivity().getApplicationContext());
        gunguListView_1.setAdapter(adapterFirst);
        adapterSecond = new CCAdapter(ccitemsSecond, getActivity().getApplicationContext());
        gunguListView_2.setAdapter(adapterSecond);
        adapterThird = new CCAdapter(ccitemsThird, getActivity().getApplicationContext());
        gunguListView_3.setAdapter(adapterThird);
        result.close();
        dbAdapter.close();
    }
    private void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getActivity().getBaseContext().getResources().getAssets().open("region.xls");
            workbook = Workbook.getWorkbook(is);
            workbook.getSheets();

            if (workbook != null) {
                sheet = workbook.getSheet(0);

                if (sheet != null) {

                    int nMaxColumn = 2;
                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0;
                    int nColumnEndIndex = sheet.getRow(2).length - 1;

                    dbAdapter.open();
                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {
                        String sido = sheet.getCell(nColumnStartIndex, nRow).getContents();
                        String gungu = sheet.getCell(nColumnStartIndex + 1, nRow).getContents();
                        //String dong = sheet.getCell(nColumnStartIndex + 2, nRow).getContents();
                        dbAdapter.createNote(sido, gungu);
                    }
                    dbAdapter.close();
                } else {
                    System.out.println("Sheet is null!!");
                }
            } else {
                System.out.println("WorkBook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }
}
