package com.twotr.twotr.tutorfiles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.twotr.twotr.R;
import com.twotr.twotr.globalpackfiles.Global_url_twotr;
import com.wang.avi.AVLoadingIndicatorView;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile_Update_Professional extends AppCompatActivity {
    EditText ed_prof_wortt;
    SearchableSpinner ed_prof_inst;
    TextView ed_prof_startd,ed_prof_endd;
    private ArrayList<String> ASorganization;
    SharedPreferences Shared_user_details;
    String Stoken;
    Button Bsavecontinue;
    private boolean BisProfessionalCompleted;
    AVLoadingIndicatorView avi;
    String Stitle,Sinsitutionname,Sstartyear,Sendyear,instu_selec,sweetmessage,Sexperience;

    SharedPreferences.Editor editor;


    TextView textViewskipid,tvexperince_year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__update__professional);
        ed_prof_wortt=findViewById(R.id.prof_prof_work);
        ed_prof_inst=findViewById(R.id.prof_prof_org);
        ed_prof_startd=findViewById(R.id.per_edu_startd);
        ed_prof_endd=findViewById(R.id.per_edu_endd);
        tvexperince_year=findViewById(R.id.experince_year);
        avi=findViewById(R.id.avi);
        avi.hide();
        Bsavecontinue=findViewById(R.id.Button_saveconti);
        ed_prof_endd.setEnabled(false);
        Shared_user_details=getSharedPreferences("user_detail_mode",0);
        Stoken=  Shared_user_details.getString("token", null);
        BisProfessionalCompleted=  Shared_user_details.getBoolean("isProfessionalCompleted",false);
       // textViewskipid=findViewById(R.id.skip_veri);
//        textViewskipid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Profile_Update_Professional.this, HomePage.class));
//                finish();
//            }
//        });

        ASorganization = new ArrayList<String>();
        institution_list();

        ed_prof_inst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                instu_selec= parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent intent = getIntent();
        Bundle Bintent = intent.getExtras();
        if(Bintent != null)
        {

            Stitle = (String) Bintent.get("stitle");
            Sinsitutionname = (String) Bintent.get("sinsitutionname");
            Sexperience=(String) Bintent.get("sexperience");


        }
        ed_prof_wortt.setText(Stitle);
        ed_prof_inst.setTitle(Sinsitutionname);
        tvexperince_year.setText(Sexperience);
        Bsavecontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_prof_wortt.getText().toString().equals(""))
                {
                    sweetmessage="Please Enter Work Title";
                }
                else
                {
                    if (instu_selec==null)
                    {
                        sweetmessage="Please Select Your Organization";
                        new SweetAlertDialog(Profile_Update_Professional.this, SweetAlertDialog.WARNING_TYPE).setTitleText(sweetmessage)
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                }).show();
                    }
                    else
                    {
                        if (tvexperince_year.getText().toString().equals(""))
                        {
                            sweetmessage="Please Enetr your Experience";
                            new SweetAlertDialog(Profile_Update_Professional.this, SweetAlertDialog.WARNING_TYPE).setTitleText(sweetmessage)
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                        }
                                    }).show();
                        }

                            else
                            {

                                avi.show();
                                Stitle=ed_prof_wortt.getText().toString();
                                Sinsitutionname=ed_prof_inst.getSelectedItem().toString();

int iexperi=Integer.valueOf(tvexperince_year.getText().toString());
                                professionalapiins(Stitle,Sinsitutionname,iexperi);

                        }
                    }
                }



                //startActivity(new Intent(Profile_Update_Professional.this,Tutor_VerficationPage.class));

            }
        });

        ed_prof_wortt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    ed_prof_wortt.setBackgroundResource( R.drawable.edittext_selected);
                }
                else
                {
                    ed_prof_wortt.setBackgroundResource( R.drawable.edittext_unselected);

                }
            }
        });
        final int[] choosenYear = {2017};
        ed_prof_startd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_prof_endd.setEnabled(true);
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Profile_Update_Professional.this, new MonthPickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        ed_prof_startd.setText(Integer.toString(selectedYear));
                        choosenYear[0] = selectedYear;
                    }
                }, choosenYear[0], 0);

                builder.showYearOnly()
                        .setYearRange(1920, 2018)
                        .build()
                        .show();
            }

        });
        final int[] choosenYearr = {2017};

        ed_prof_endd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Profile_Update_Professional.this, new MonthPickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        ed_prof_endd.setText(Integer.toString(selectedYear));
                        choosenYearr[0] = selectedYear;
                    }
                }, choosenYearr[0], 0);

                builder.showYearOnly()
                        .setYearRange(choosenYear[0], 2018)
                        .build()
                        .show();
            }

        });


    }
    public void institution_list() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Profile_institute_level, new Response.Listener<String>() {

            public void onResponse(String response) {
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String title = jsonobject.getString("title");
                        ASorganization.add(title);

                    }

                    ed_prof_inst.setAdapter(new ArrayAdapter<String>(Profile_Update_Professional.this, android.R.layout.simple_dropdown_item_1line, ASorganization));



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);


                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }

    public void professionalapiins(String stitle, String sinsitutionname, int iexperience)
    {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("title",stitle);
            jsonObject.put("institutionName",sinsitutionname);
            jsonObject.put("experience",iexperience);
            jsonBody.put("professionalInfo",jsonObject);


            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Global_url_twotr.User_Profile_professional, new Response.Listener<String>() {

                public void onResponse(String response) {
                    editor = Shared_user_details.edit();
                    editor.putBoolean("isProfessionalCompleted",true);
                    editor.commit();
                    avi.hide();

                    startActivity(new Intent(Profile_Update_Professional.this, HomePage.class));
                    finish();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    avi.hide();

                    new SweetAlertDialog(Profile_Update_Professional.this, SweetAlertDialog.NORMAL_TYPE).setTitleText("Server Error")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();            }
            }) {
                @Override
                public String getBodyContentType() {

                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //  headers.put("content-Type", "application/json");
                    headers.put("x-tutor-app-id", "tutor-app-android");
                    headers.put("authorization", "Bearer "+Stoken);


                    return headers;

                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");

                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }




            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
