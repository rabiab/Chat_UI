package android.lavie.com.chat_ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    Context mContext;
    List<Sms> smsList;

    public CustomListAdapter(Context mContext,List<Sms> smsList)
    {
        this.mContext=mContext;
        this.smsList=smsList;
    }

    @Override
    public int getCount() {
        return smsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView num,time,msg;
        if(convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.background_list_allmessages,null);
        }
        num=convertView.findViewById(R.id.list_num);
        time=convertView.findViewById(R.id.list_time);
        msg=convertView.findViewById(R.id.list_msg);

        Sms sms=smsList.get(position);
        num.setText(sms.getAddress());
        Date date=new Date(Long.parseLong(sms.getTime()));
        time.setText(date.getHours()+" : "+date.getMinutes());
        msg.setText(sms.getMsg());

        return convertView;
    }
}
