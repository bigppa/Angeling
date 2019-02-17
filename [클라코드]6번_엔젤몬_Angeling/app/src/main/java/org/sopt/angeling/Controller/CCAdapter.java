package org.sopt.angeling.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sopt.angeling.Model.CCitem;
import org.sopt.angeling.R;
import org.sopt.angeling.View.CCViewHolder;

import java.util.ArrayList;

/**
 * Created by DongHyun on 2016-01-13.
 */
public class CCAdapter extends BaseAdapter{

    public ArrayList<CCitem> ccitems = null; //인자로 받아온 itemDatas를 저장하기 위한 어댑터 내의 ArrayList<ItemData> 객체
    private LayoutInflater layoutInflater = null; //ListView 아이템 레이아웃을 가져오기 위한 클래스
    public static Typeface mTypeface = null;//폰트
    Context Ctx;

    /*생성자를 통해서 itemDatas를 받아옵니다.
      ctx는 BaseAdapter에 내재되어 있지 않으므로 인자로 받아오니다.*/

    public CCAdapter(ArrayList<CCitem> ccitems, Context ctx){
        this.ccitems = ccitems;
        this.layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Ctx = ctx;
    }

    //생성자를 통해서가 아닌 메소드를 통해 itemDatas를 받아오기 위한 메소드
    //이번 실습에서는 사용하지는 않으니 참고만 해주세요.

    private void setItemDatas(ArrayList<CCitem> ccitems){
        this.ccitems = ccitems;
        this.notifyDataSetChanged();
    }

    //int getCount 메소드 -> ListView의 아이템 개수를 int형으로 반환
    @Override
    public int getCount() {
        return (ccitems != null) ? ccitems.size() : 0;
    }

    //Object getItem 메소드 -> position에 해당하는 아이템을 객체의 형태로 반환
    //이번 실습에서는 ListView의 아이템이 ItemData형이므로 getItem 메소드를 통해 Object형으로 데이터를 받아오면 temData형으로 캐스팅을 해주어야겠죠?
    @Override
    public Object getItem(int position) {
        return (ccitems != null && (0 <= position && position < ccitems.size()) ? ccitems.get(position) : null ); // A ? B : C 아시조??
    }

    //long getItemId 메소드 -> position에 해당하는 아이템의 Id를 반환
    @Override
    public long getItemId(int position) {
        return (ccitems != null && (0 <= position && position < ccitems.size()) ? position : 0);
    }

    //View getView 메소드 -> 받아온 데이터(itemDatas)를 ListView의 아이템으로 만들어주는 메소드
    //convertView는 재활용이 가능한 View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CCViewHolder ccviewHolder = new CCViewHolder(); //화면에서 사라진 convertView를 담아두기 위한 용도로 viewHolder를 사용

        if(convertView == null){ //'convertView == null' = 'convertView가 한번도 생성이 된 적 없다면'

            convertView = layoutInflater.inflate(R.layout.cclist_item, parent, false); //TextView 2개를 포함하고 있는 list_item을 inflate해서 convertView에 대입

            //viewHolder를 초기화. 한번 해두면 이 position의 convertView는 다시 findViewById를 할 필요가 없어집니다.
            ccviewHolder.textView_Region = (TextView)convertView.findViewById(R.id.txtDescription_item);

            convertView.setTag(ccviewHolder); //convertView에 viewHolder를 태그로 달아줍니다.이렇게 해두면 화면에서 ListView의 아이템(convertView)가 사라져도 메모리에서 지워지지 않습니다.
        }

        else{
            ccviewHolder = (CCViewHolder)convertView.getTag();//convertView가 존재한다면(한번이라도 생성이 되었다면) 달아뒀던 viewHolder가 존재하겠죠? 이를 다시 가져옵니다.
        }

        CCitem ccitem = ccitems.get(position); //position에 해당하는 itemData를 받아옵니다.

        Typeface font = Typeface.createFromAsset(Ctx.getAssets(), "fonts/BMJUA_ttf.ttf");
        ccviewHolder.textView_Region.setText(ccitem.region);
        ccviewHolder.textView_Region.setTypeface(font);


        return convertView; //헷갈리실 수 있는데 viewHolder를 반환하는게 아니라 convertView를 반환해야 합니다!! 사용하려고 inflate까지 했잖아요?!
    }

}
