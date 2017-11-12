package com.example.blood_group_app;
import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import static com.example.blood_group_app.Constants.FIRST_COLUMN;
import static com.example.blood_group_app.Constants.SECOND_COLUMN;
import static com.example.blood_group_app.Constants.THIRD_COLUMN;
import static com.example.blood_group_app.Constants.FOURTH_COLUMN;
public class DonarlistScreenActivity extends Activity {
ListView listView1;
ArrayList<BeanDetails> ar_bean;
private ArrayList<HashMap> list;
BeanDetails bean = new BeanDetails();
DatabaseHelper helper;
SQLiteDatabase database;
Cursor cursor;
Activity act;
static String str_blood_gp;
String str_pincode;
private static final String fields[] = { BaseCol._ID, "username","addr", "mobileno" };
ListAdapter adapter;
String str_uname;
String str_id;
@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.donarlistscreen);
ActionBar actionBar = getActionBar();
actionBar.setBackgroundDrawable(new ColorDrawable(Color
.parseColor("#000000")));
listView1 = (ListView) findViewById(R.id.listView1);
str_blood_gp = getIntent().getStringExtra("BloodGroup");
str_pincode = getIntent().getStringExtra("PinCode");
str_uname = getIntent().getStringExtra("username");
str_id = getIntent().getStringExtra("Id");
act = this;
helper = new DatabaseHelper(this);
ar_bean = new ArrayList<BeanDetails>();
 list = new ArrayList<HashMap>();
try{
database = helper.getReadableDatabase();	
ar_bean = getData();
if(ar_bean.size()>0){			
for(int i=0 ; i<ar_bean.size();i++)
{BeanDetails data = ar_bean.get(i);
HashMap temp = new HashMap();
temp.put(FIRST_COLUMN, data.Value1);
temp.put(SECOND_COLUMN, data.Value2);
temp.put(THIRD_COLUMN, data.Value3);	
temp.put(FOURTH_COLUMN, data.Value4);	
list.add(temp);}
for(int i = 0; i < list.size(); i++) {
HashMap temp =list.get(i);}
adapter= new ListviewAdapterDonarList(act,list);
listView1.setAdapter(adapter);}}
catch(Exception e){
Toast.makeText(getApplicationContext(),"error"+e.getMessage(), 
Toast.LENGTH_SHORT).show();}}
public ArrayList<BeanDetails> getData(){
try{
String selection = "bloodgrp=? AND pinno=? AND donar=?";
String[] selectionArgs = { str_blood_gp, str_pincode, "YES" };
cursor=database.query(DatabaseHelper.User_details_TABLE_NAME,
					fields, selection, selectionArgs, null, null, null);
cursor.moveToFirst();
do{
bean.Value1 = cursor.getString(cursor.getColumnIndex("_id"));
bean.Value2=cursor.getString(cursor.getColumnIndex("username"));
bean.Value3 = cursor.getString(cursor.getColumnIndex("address"));
bean.Value4 = cursor.getString(cursor.getColumnIndex("mobileno"));
ar_bean.add(newBeanDetails(bean.Value1,bean.Value2, bean.Value3, bean.Value4));
}while(cursor.moveToNext());		
return ar_bean;}
catch(Exception e){			
Toast.makeText(getApplicationContext(),"SorryNoDataExists", 
Toast.LENGTH_SHORT).show();
return null;}}   
@Override
protected void onStop() {
super.onStop();
finish();}
@Override
public void onBackPressed() {
super.onBackPressed();
finish();
IntentMainScreen=new Intent(getApplicationContext(),SearchdonarActivity.class);
MainScreen.putExtra("username",  str_uname);
MainScreen.putExtra("Id",  str_id);
startActivity(MainScreen);}}
2.	Receiver: are the one who is in search for the blood even hear we divide into two modules they are
•	Receiver registration: initially the receiver needs to get register for the application by filling the details.
•	Receiver login: to search for the donor or blood bank details the receiver has to login to the app by using id and perform various operations.
package com.example.blood_group_app;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
public class RegistrationScreenActivity extends Activity {
EditText et_firstname;
EditText et_lastname;
EditText et_username;
EditText et_password;
EditText et_confirmpwd;
EditText et_mobileno;
EditText et_address;
EditText et_pinno;
Spinner spn_gender;
Spinner spn_bloodgrp;
Button btn_register;
Button btn_existinguser;
DatabaseHelper helper;	
SQLiteDatabase database;
String[] str_gender = { "Male", "Female" };
String[] str_blood_group = { "A+ve", "A-ve", "B+ve", "B-ve", "O+ve", "O-ve" };
String gender="Male";
String bloodgrp="A+ve";
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.registrationscreen);
et_firstname = (EditText) findViewById(R.id.et_firstname);
et_lastname = (EditText) findViewById(R.id.et_lastname);
et_username = (EditText) findViewById(R.id.et_username);
et_password = (EditText) findViewById(R.id.et_password);
et_confirmpwd = (EditText) findViewById(R.id.et_confirmpwd);
et_mobileno = (EditText) findViewById(R.id.et_mobileno);
et_address = (EditText) findViewById(R.id.et_address);
et_pinno = (EditText) findViewById(R.id.et_pinno);
spn_gender = (Spinner) findViewById(R.id.spn_gender);
spn_bloodgrp = (Spinner) findViewById(R.id.spn_bloodgrp);
btn_register = (Button) findViewById(R.id.btn_register);		
helper = new DatabaseHelper(this);
ArrayAdapter aa = new ArrayAdapter(this,
android.R.layout.simple_spinner_item, str_gender);			    
aa.setDropDownViewResource(android.R.layout.simple_spin_dropdown_item);
spn_gender.setAdapter(aa);
 spn_gender.setOnItemSelectedListener(new OnItemSelectedListener(){
@Override
public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {					
gender = parent.getItemAtPosition(position).toString().trim();}
@Override
public void onNothingSelected(AdapterView<?> parent) {
gender = "Male";}});
ArrayAdapter aa1 = new ArrayAdapter(this,
android.R.layout.simple_spinner_item, str_blood_group);			
aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spn_bloodgrp.setAdapter(aa1);
spn_bloodgrp.setOnItemSelectedListener(new OnItemSelectedListener(){
@Override
public void onItemSelected(AdapterView<?> parent, View view,
int position, long id) {
bloodgrp = parent.getItemAtPosition(position).toString().trim();}
@Override
public void onNothingSelected(AdapterView<?> parent) {
bloodgrp = "A+ve";}});
btn_register.setOnClickListener(new OnClickListener() {
private Intent loginScreen;
@Override
public void onClick(View v) {
if (et_firstname.getText().toString().trim().equalsIgnoreCase("")
et_firstname.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter firstname",
Toast.LENGTH_LONG).show();
} else if (et_lastname.getText().toString().trim().equalsIgnoreCase("")
et_lastname.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter lastname",
Toast.LENGTH_LONG).show();
} else if (et_username.getText().toString().trim().equalsIgnoreCase("")
et_username.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter username",
Toast.LENGTH_LONG).show();
} else if (et_password.getText().toString().trim().equalsIgnoreCase("")
et_password.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter password",
Toast.LENGTH_LONG).show();
} else if (et_confirmpwd.getText().toString().trim()
.equalsIgnoreCase("")
et_confirmpwd.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter confirmpwd",
Toast.LENGTH_LONG).show();
} else if (et_mobileno.getText().toString().trim()
.equalsIgnoreCase("")
et_mobileno.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter mobileno",
Toast.LENGTH_LONG).show();
} else if (et_address.getText().toString().trim().equalsIgnoreCase("")
et_address.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter address",
Toast.LENGTH_LONG).show();
} else if (et_pinno.getText().toString().trim().equalsIgnoreCase("")
et_pinno.getText().toString().trim().length() == 0) {
Toast.makeText(getApplicationContext(), "enter pinno",
Toast.LENGTH_LONG).show();} 
else{
database = helper.getWritableDatabase();
ContentValues values = new ContentValues();
values.put("firstname", et_firstname.getText().toString().trim());
values.put("lastname", et_lastname.getText().toString().trim());
values.put("username", et_username.getText().toString().trim());
values.put("pwd", et_password.getText().toString().trim());
values.put("mobileno", et_mobileno.getText().toString().trim());
values.put("address", et_address.getText().toString().trim());
values.put("pinno", et_pinno.getText().toString().trim());
values.put("gender", gender);
values.put("bloodgrp",bloodgrp);
values.put("donar","NO");
try{
long i = database.insert(DatabaseHelper.User_details_TABLE_NAME, null, values);
if(i>0){
Toast.makeText(getApplicationContext(),"DataInsertedSuccessfully",
Toast.LENGTH_SHORT ). show();	
Intent LoginScreen = new Intent(getApplicationContext(), LoginScreenActivity.class);
						startActivity(LoginScreen);}
else{
Toast.makeText(getApplicationContext(), "Data Insertion failed" , 
Toast.LENGTH_SHORT).show();}
}catch(Exception e){
Toast.makeText(getApplicationContext(), "Inserting Data failed: " + e.getMessage() , 
Toast.LENGTH_SHORT).show();}}}});
/*btn_existinguser.setOnClickListener(new OnClickListener() {
public void onClick(View v) {
}});*/
}
@Override
protected void onStop() {
super.onStop();
finish();
}
@Override
public void onBackPressed() {
super.onBackPressed();
finish();
Intent LoginScreen = new Intent(getApplicationContext(), LoginScreenActivity.class);
		                 startActivity(LoginScreen);}}
3.	Search based on blood group: receiver can search the blood based on blood group.
package com.example.blood_group_app;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
public class SearchdonarActivity extends Activity {
Spinner spn_bloodgrp;
EditText et_pinno;
Button btn_search;
String[] str_blood_group = { "A+ve", "A-ve", "B+ve", "B-ve", "O+ve", "O-ve" };
String bloodgrp="A+ve";
String str_uname;
String str_id;
protectedvoid onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);
setContentView(R.layout.searchdonar);
spn_bloodgrp = (Spinner)findViewById(R.id.spn_bloodgrp);
et_pinno = (EditText)findViewById(R.id.et_pinno);
btn_search = (Button)findViewById(R.id.btn_search);
str_uname = getIntent().getStringExtra("username");
str_id = getIntent().getStringExtra("Id");
ArrayAdapter aa = new ArrayAdapter(this,
android.R.layout.simple_spinner_item, str_blood_group);
aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spn_bloodgrp.setAdapter(aa);
spn_bloodgrp.setOnItemSelectedListener(new OnItemSelectedListener(){
@Override
public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
bloodgrp = parent.getItemAtPosition(position).toString().trim();}
@Override
public void onNothingSelected(AdapterView<?> parent) {
bloodgrp = "A+ve";}});
btn_search.setOnClickListener(new OnClickListener() {
@Override
public void onClick(View v) {
try{				 if(et_pinno.getText().toString().trim().equalsIgnoreCase("")||
et_pinno.getText().toString().trim().length()==0){
Toast.makeText(getAppContext(),"enterpinno", Toast.LENGTH_LONG).show();}
else {
Intent donarList = new Intent(getApplicationContext(), DonarlistScreenActivity.class);
donarList.putExtra("BloodGroup", bloodgrp);
donarList.putExtra("PinCode", et_pinno.getText().toString().trim());
donarList.putExtra("username",  str_uname);
donarList.putExtra("Id",  str_id);
startActivity(donarList);}}
catch(Exception e){
Toast.makeText(getApplicationContext(),"erroris:"+e.getMessage(), Toast.LENGTH_LONG).show();}}});}
@Override
protected void onStop() {
super.onStop();
finish();}
@Override
public void onBackPressed() {
super.onBackPressed();
finish();
Intent MainScreen = new Intent(getApplicationContext(),SearchScreenActivity.class);
MainScreen.putExtra("username",  str_uname);
MainScreen.putExtra("Id",  str_id);
startActivity(MainScreen);}}			
4.	Search based on pin code: the receiver can also search for the donor based on pin code.
5.	Call/ sms to donor: the receiver can make a call or can send a message to the donor.
6.	Blood bank ifo: the receiver will be able to get the information regarding the blood bank.
package com.example.blood_group_app;
import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.blood_group_app.Constants.FIRST_COLUMN;
import static com.example.blood_group_app.Constants.SECOND_COLUMN;
import static com.example.blood_group_app.Constants.THIRD_COLUMN;
import static com.example.blood_group_app.Constants.FOURTH_COLUMN;
import static com.example.blood_group_app.Constants.FIFTH_COLUMN;
import static com.example.blood_group_app.Constants.SIXTH_COLUMN;
public class BloodBankListScreenActivity extends Activity {	
ListView listView1;
ArrayList<BeanDetails> ar_bean;
private ArrayList<HashMap> list;
BeanDetails bean = new BeanDetails();
DatabaseHelper helper;
SQLiteDatabase database;
Cursor cursor;
Activity act;
private static final String fields[] = { BaseColumns._ID, "name","address", "mobileno", "latitude", "longitude" };
ListAdapter adapter;
String str_uname;
String str_id;
@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.bloodbanklistscreen);
ActionBar actionBar = getActionBar();
 actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
listView1 = (ListView) findViewById(R.id.listView1);
 str_uname = getIntent().getStringExtra("username");
str_id = getIntent().getStringExtra("Id");
act = this;
helper = new DatabaseHelper(this);
ar_bean = new ArrayList<BeanDetails>();
list = new ArrayList<HashMap>();
 try{
 database = helper.getReadableDatabase();
ar_bean = getData();
if(ar_bean.size()>0){
for(int i=0 ; i<ar_bean.size();i++){
BeanDetails data = ar_bean.get(i);
HashMap temp = new HashMap();
temp.put(FIRST_COLUMN, data.Value1);
temp.put(SECOND_COLUMN, data.Value2);
temp.put(THIRD_COLUMN, data.Value3);	
temp.put(FOURTH_COLUMN, data.Value4);
temp.put(FIFTH_COLUMN, data.Value5);
temp.put(SIXTH_COLUMN, data.Value6);
list.add(temp);}

for(int i = 0; i < list.size(); i++) {
HashMap temp =list.get(i);}
adapter= new ListviewAdapterBloodBankList(act,list);
listView1.setAdapter(adapter);}}
catch(Exception e){
Toast.makeText(getApplicationContext(),"error"+e.getMessage(), Toast.LENGTH_SHORT).show();}}
public ArrayList<BeanDetails> getData(){
try{
cursor = database.query(DatabaseHelper.BloodBank_details_TABLE_NAME,
					fields, null, null, null, null, null);
cursor.moveToFirst();
do{
bean.Value1 = cursor.getString(cursor.getColumnIndex("_id"));
bean.Value2 = cursor.getString(cursor.getColumnIndex("name"));
bean.Value3 = cursor.getString(cursor.getColumnIndex("address"));
bean.Value4 = cursor.getString(cursor.getColumnIndex("mobileno"));
bean.Value5 = cursor.getString(cursor.getColumnIndex("latitude"));
bean.Value6 = cursor.getString(cursor.getColumnIndex("longitude"));
ar_bean.add(new BeanDetails(bean.Value1, bean.Value2, bean.Value3, bean.Value4, bean.Value5, bean.Value6));
}while(cursor.moveToNext());
return ar_bean;}
catch(Exception e){
Toast.makeText(getApplicationContext(),"SorryNoDataExists", Toast.LENGTH_SHORT).show();
return null;}}   
@Override
public void onBackPressed() {
super.onBackPressed();
finish();
Intent MainScreen = new Intent(getApplicationContext(),SearchScreenActivity.class);
MainScreen.putExtra("username",  str_uname);
MainScreen.putExtra("Id",  str_id);
startActivity(MainScreen);}}
