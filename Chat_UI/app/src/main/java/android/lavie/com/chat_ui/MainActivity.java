package android.lavie.com.chat_ui;

import android.Manifest;
import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    ListView mListChats;
    RelativeLayout mLayout;
    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListChats=findViewById(R.id.messages_list);
        mLayout=findViewById(R.id.main_layout);

        fetchSmsPermission();

    }

    public class taskLoad extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog mProgress;
        List<Sms> msgs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress=new ProgressDialog(MainActivity.this);
            mProgress.setTitle("Please Wait");
            mProgress.setMessage("Fetching Messages");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<Sms> allSms=getAllSms();
                    msgs= convertListToUnified(allSms);
                }
            });

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            populateList(msgs);
            mProgress.dismiss();
        }
    }
    private List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, "date ASC");
        this.startManagingCursor(c);
        int totalSMS = c.getCount();


        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));

                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));

                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));

                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }


                lstSms.add(objSms);
                c.moveToNext();
            }

        }
        return lstSms;
    }

    public void fetchSmsPermission()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this,permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS},
                    112);
        }
        else
        {
            new taskLoad().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==112)
        {
               if(grantResults[0]==RESULT_OK)
               {
                  new taskLoad().execute();
               }

        }
    }

    public List<Sms> convertListToUnified(List<Sms> allSms)
    {
        Toast.makeText(MainActivity.this,"Before size "+allSms.size(),Toast.LENGTH_LONG).show();
        List<Sms> noRepeat = new ArrayList<Sms>();

        noRepeat.add(allSms.get(0));
        for (int i=1;i<allSms.size();i++) {
            boolean isFound = false;
            for (int j=0;j<noRepeat.size();j++) {
               if(noRepeat.get(j).getAddress().contains(allSms.get(i).getAddress()))
               {
                   noRepeat.get(j).setMsg(allSms.get(i).getMsg());
                   noRepeat.get(j).setTime(allSms.get(i).getTime());
                   isFound=true;
               }
            }
            if (!isFound) noRepeat.add(allSms.get(i));

        }
        Toast.makeText(MainActivity.this,"After removing duplications size "+noRepeat.size(),Toast.LENGTH_LONG).show();
        return noRepeat;

    }

    public void populateList(final List<Sms> mlist)
    {
        if(mlist.size()>0)
        {
            CustomListAdapter adapter=new CustomListAdapter(MainActivity.this,mlist);
            mListChats.setAdapter(adapter);

            mListChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Sms sms=mlist.get(position);
                   String num=sms.getAddress();
                   Intent start=new Intent(MainActivity.this,ConversationActivity.class);
                   start.putExtra("num",num);
                   startActivity(start);
                }
            });
        }
        else
        {
            Snackbar.make(mLayout,"Inbox is Empty",Snackbar.LENGTH_LONG).show();
        }

    }



}
