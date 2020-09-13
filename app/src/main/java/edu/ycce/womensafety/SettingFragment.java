package edu.ycce.womensafety;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{

    private ImageButton c1btn, s1btn, s2btn, s3btn;
    private EditText c1, s1, s2, s3;
    private Button btn;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;

    public SettingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        pref = getLayoutInflater().getContext().getSharedPreferences("contacts", Context.MODE_PRIVATE);
        editor = pref.edit();

        c1btn = (ImageButton) v.findViewById(R.id.c1btn);
        s1btn = (ImageButton) v.findViewById(R.id.s1btn);
        s2btn = (ImageButton) v.findViewById(R.id.s2btn);
        s3btn = (ImageButton) v.findViewById(R.id.s3btn);

        c1 = (EditText) v.findViewById(R.id.c1);
        s1 = (EditText) v.findViewById(R.id.s1);
        s2 = (EditText) v.findViewById(R.id.s2);
        s3 = (EditText) v.findViewById(R.id.s3);

        btn = (Button) v.findViewById(R.id.update);

        c1btn.setOnClickListener(this);
        s1btn.setOnClickListener(this);
        s2btn.setOnClickListener(this);
        s3btn.setOnClickListener(this);

        btn.setOnClickListener(this);

        contact();

        return v;
    }

    public void contact() {
        c1.setText(pref.getString("c1", ""));
        s1.setText(pref.getString("s1", ""));
        s2.setText(pref.getString("s2", ""));
        s3.setText(pref.getString("s3", ""));
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        switch (v.getId()) {
            case R.id.c1btn:
                startActivityForResult(intent, 11);
                break;
            case R.id.s1btn:
                startActivityForResult(intent, 22);
                break;
            case R.id.s2btn:
                startActivityForResult(intent, 33);
                break;
            case R.id.s3btn:
                startActivityForResult(intent, 44);
                break;
            case R.id.update:
                update();
                break;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && (reqCode == 11 || reqCode ==22 || reqCode ==33 || reqCode ==44)) {
            Uri contactData = data.getData();
            Cursor c =  getActivity().getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                int index = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                switch (reqCode) {

                    case 11:
                        c1.setText(c.getString(index));
                        break;
                    case 22:
                        s1.setText(c.getString(index));
                        break;
                    case 33:
                        s2.setText(c.getString(index));
                        break;
                    case 44:
                        s3.setText(c.getString(index));
                        break;
                }
                c.close();
            }
        }
    }

    public void update() {
        String c1no, s1no, s2no, s3no;
        c1no = c1.getText().toString();
        s1no = s1.getText().toString();
        s2no = s2.getText().toString();
        s3no = s3.getText().toString();

        if(c1no.isEmpty() || s1no.isEmpty() || s2no.isEmpty() || s3no.isEmpty()){
            Toast t = Toast.makeText(getLayoutInflater().getContext(), "Please fill all fields", Toast.LENGTH_SHORT);
            t.show();
        } else {
            editor.putString("c1", c1no);
            editor.putString("s1", s1no);
            editor.putString("s2", s2no);
            editor.putString("s3", s3no);
            editor.commit();
            Toast t = Toast.makeText(getLayoutInflater().getContext(), "Contacts Successfully Updated", Toast.LENGTH_SHORT);
            t.show();
            Fragment f = new MainFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, f);
            ft.commit();
        }

    }
}
