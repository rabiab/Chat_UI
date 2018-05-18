package android.lavie.com.chat_ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    ListView listConversation;
    //TextView tvConversion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        listConversation=findViewById(R.id.messages_list);
        //tvConversion=findViewById(R.id.tvShowData);

        String number=getIntent().getStringExtra("num");
         //tvConversion.setText("Showing messages for "+number);
        getSupportActionBar().setTitle(number);
        new taskFetch().execute(number);
    }

//    private List<Sms> getAllSmsFromInbox(String num) {
//
//        List<Sms> lstSms = new ArrayList<Sms>();
//        Sms objSms = new Sms();
//        Uri message = Uri.parse("content://sms/");
//        ContentResolver cr = getContentResolver();
//
//        Cursor c = cr.query(message, null, "address =?", new String[]{num}, "date ASC");
//        this.startManagingCursor(c);
//        int totalSMS = c.getCount();
//
//        String show=" ";
//        if (c.moveToFirst()) {
//            for (int i = 0; i < totalSMS; i++) {
//                objSms = new Sms();
//                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
//                 show+=c.getString(c.getColumnIndexOrThrow("_id"));
//                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
//                 show+=" "+c.getString(c.getColumnIndexOrThrow("address"));
//                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
//                show+=" "+c.getString(c.getColumnIndexOrThrow("body"));
//                objSms.setReadState(c.getString(c.getColumnIndex("read")));
//                show+=" Read state"+c.getString(c.getColumnIndex("read"));
//                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
//                show+=" "+c.getString(c.getColumnIndexOrThrow("date"));
//                objSms.setFolderName("inbox");
//                show+=" type "+c.getString(c.getColumnIndexOrThrow("type"));
////                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
////                    objSms.setFolderName("inbox");
////                } else {
////                    objSms.setFolderName("sent");
////                }
//
//                show+="\n";
//                lstSms.add(objSms);
//                c.moveToNext();
//            }
//            // Log.d("Data",show);
//        }
//               String text=tvConversion.getText().toString();
//               tvConversion.setText(text+"\n Received From messages: "+show);
//
//  //      c.close();
//
//        return lstSms;
//    }

    private List<Sms> getAllSmsFromSent(String num)
    {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, "address =?", new String[]{num}, "date ASC");
        this.startManagingCursor(c);
        int totalSMS = c.getCount();

        String show=" ";
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                show+=c.getString(c.getColumnIndexOrThrow("_id"));
                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                show+=" "+c.getString(c.getColumnIndexOrThrow("address"));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                show+=" "+c.getString(c.getColumnIndexOrThrow("body"));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                show+="Read state "+c.getString(c.getColumnIndexOrThrow("read"));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                show+=" "+c.getString(c.getColumnIndexOrThrow("date"));
                show+=" type "+c.getString(c.getColumnIndexOrThrow("type"));
                objSms.set_type(c.getString(c.getColumnIndexOrThrow("type")));
                objSms.setFolderName("sent");
//                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
//                    objSms.setFolderName("inbox");
//                } else {
//                    objSms.setFolderName("sent");
//                }

                show+="\n";
                lstSms.add(objSms);
                c.moveToNext();
            }
            // Log.d("Data",show);
        }

    //    c.close();
//        String text=tvConversion.getText().toString();
//        tvConversion.setText(text+"\n Sent to messages: "+show);

        return lstSms;
    }

    class taskFetch extends AsyncTask<String,Void,Void>
    {
        ProgressDialog mProgress;
        List<Sms> msgs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress=new ProgressDialog(ConversationActivity.this);
            mProgress.setTitle("Please Wait");
            mProgress.setMessage("Fetching Messages");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String num=strings[0];

            msgs=getAllSmsFromSent(num);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomConverAdapter adapter=new CustomConverAdapter(ConversationActivity.this,msgs);
            listConversation.setAdapter(adapter);
            mProgress.dismiss();
        }
    }
}
