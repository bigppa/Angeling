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
public class CityFragment extends Fragment {
    private DBManager dbAdapter;
    private ArrayList<CCitem> itemDatasFirst = null;
    private ArrayList<CCitem> itemDatasSecond = null;
    String selectedCity = null;
    ListView cityList1, cityList2;
    private CCAdapter adapterFirst, adapterSecond;
    TextView tv1, tv2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.city_fragment, container, false);

        dbAdapter = new DBManager(getActivity().getApplicationContext());

        cityList1 = (ListView)view.findViewById(R.id.city_list_1);
        cityList2 = (ListView)view.findViewById(R.id.city_list_2);

        tv1 = (TextView)view.findViewById(R.id.textView_country);
        tv2 = (TextView)view.findViewById(R.id.textView_city);

        dbAdapter.open();
        if(dbAdapter.fetchAllNotes().getCount()==0) {
            copyExcelDataToDatabase();
        }

        Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/BMJUA_ttf.ttf");
        tv1.setTypeface(font);
        tv2.setTypeface(font);


        initModel();
        getCityList();

        cityList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CCitem ccitem_temp = (CCitem) adapterFirst.getItem(position);
                selectedCity = ccitem_temp.region;
                initModel();

                //go to CountryFragment
                gotoCountryFragment(selectedCity);
            }
        });

        cityList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CCitem ccitem_temp = (CCitem) adapterSecond.getItem(position);
                selectedCity = ccitem_temp.region;
                initModel();

                //go to Country Fragment
                gotoCountryFragment(selectedCity);
            }
        });






        return view;

    }

    private void gotoCountryFragment(String selectedCity){
        CountryFragment newFragment = new CountryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("selectedCity",selectedCity);
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
        itemDatasFirst = new ArrayList<>();
        itemDatasSecond = new ArrayList<>();
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


    public void getCityList() {
        dbAdapter.open();
        Cursor result = dbAdapter.selectSIDO();
        result.moveToFirst();
        int evenORodd = 1;
        while (!result.isAfterLast()) {
            String sidostr = result.getString(0);
            if(evenORodd % 2 == 1) {
                CCitem ccitem_submit = new CCitem();
                ccitem_submit.region = sidostr;
                itemDatasFirst.add(ccitem_submit);
                evenORodd++;
            }
            else {
                CCitem ccitem_submit = new CCitem();
                ccitem_submit.region = sidostr;
                itemDatasSecond.add(ccitem_submit);
                evenORodd++;
            }
            result.moveToNext();
        }
        Log.i("aa",itemDatasFirst.get(0).region);
        adapterFirst = new CCAdapter(itemDatasFirst, getActivity().getApplicationContext()); //CustomAdapter 객체인 adapter에 itemDatas와 Context를 인자로 넘겨주었습니다!!
        cityList1.setAdapter(adapterFirst);
        adapterSecond = new CCAdapter(itemDatasSecond, getActivity().getApplicationContext()); //CustomAdapter 객체인 adapter에 itemDatas와 Context를 인자로 넘겨주었습니다!!
        cityList2.setAdapter(adapterSecond);
        result.close();
        dbAdapter.close();
    }


}
