package edu.ycce.womensafety;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    public  String loc = "";
    private ImageButton ib;
    private TextView tv;
    private int counter = 0;
    GPSTracker gps;

    private SharedPreferences pref;

    public MainFragment() {

    }

    public ImageButton getIb() {
        return this.ib;
    }

    public void location() {
        gps = new GPSTracker(getLayoutInflater().getContext());
        if(gps.canGetLocation()) {
            loc = gps.getLatitude() + "," + gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        pref = getLayoutInflater().getContext().getSharedPreferences("contacts", Context.MODE_PRIVATE);

        ib = (ImageButton) v.findViewById(R.id.imageButton);
        ib.setOnClickListener(this);
        location();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                sos(ib);
                break;
        }
    }

    public void sos(ImageButton ib) {

        tv = (TextView) getView().findViewById(R.id.help);
        if(counter%2==0) {
            ib.setBackgroundResource(R.drawable.roundcornersgreen);
            tv.setText("Click to reset SOS");
            alert();
        }
        else {
            ib.setBackgroundResource(R.drawable.roundcorners);
            Toast t = Toast.makeText(getLayoutInflater().getContext(), "Resetting sos", Toast.LENGTH_SHORT);
            t.show();
            tv.setText("");
        }
        counter++;
    }

    public void alert() {
        if (pref.contains("c1")) {
            String c1 = (pref.getString("c1", ""));
            String s1 = (pref.getString("s1", ""));
            String s2 = (pref.getString("s2", ""));
            String s3 = (pref.getString("s3", ""));

            SmsManager sm = SmsManager.getDefault();

            String sms = "I'm in danger.\nHelp Me.\nMy Approximate Location is\n http://maps.google.com/maps?q=" + loc;

            Toast t = Toast.makeText(getLayoutInflater().getContext(), sms, Toast.LENGTH_SHORT);
            t.show();
            sm.sendTextMessage(s1, null, sms, null, null);
            sm.sendTextMessage(s2, null, sms, null, null);
            sm.sendTextMessage(s3, null, sms, null, null);

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + c1));
            startActivity(intent);
        } else {
            Toast t = Toast.makeText(getLayoutInflater().getContext(), "Please update your contacts from Setting", Toast.LENGTH_SHORT);
            t.show();
            Fragment f = new SettingFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, f);
            ft.commit();
        }
    }

}
