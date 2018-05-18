package android.lavie.com.chat_ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class CustomConverAdapter extends BaseAdapter {

    Context mContext;
    List<Sms> messages;

    public CustomConverAdapter(Context mContext,List<Sms> messages)
    {
        this.mContext=mContext;
        this.messages=messages;

    }

    @Override
    public int getCount() {
        return messages.size();
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

//        Sms smsSent=new Sms(),smsRec=new Sms();
//        Date dateSent=new Date(),dateRec=new Date();
//
//        if(posSent<sentMessages.size())
//        {
//            smsSent=sentMessages.get(posSent);
//            dateSent=new Date(Long.parseLong(smsSent.getTime()));
//        }
//        if(posRec<recMessages.size())
//        {
//            smsRec=recMessages.get(posRec);
//            dateRec=new Date(Long.parseLong(smsRec.getTime()));
//        }

        Sms sms=messages.get(position);

        if(sms.get_type().contains("2"))
         {
           LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView=inflater.inflate(R.layout.my_message,null);
             TextView msg=convertView.findViewById(R.id.message_body);
             TextView time=convertView.findViewById(R.id.message_time);
             msg.setText(sms.getMsg());
             Date date=new Date(Long.parseLong(sms.getTime()));
             time.setText(date.getHours()+" : "+date.getMinutes());
             //posSent++;
         }
         else
         {
             LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView=inflater.inflate(R.layout.thier_message,null);
             TextView msg=convertView.findViewById(R.id.their_message_body);
             TextView time=convertView.findViewById(R.id.their_time);
             msg.setText(sms.getMsg());
             Date date=new Date(Long.parseLong(sms.getTime()));
             time.setText(date.getHours()+" : "+date.getMinutes());
         }

         return convertView;
    }
}
