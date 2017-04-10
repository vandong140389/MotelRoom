package com.example.vonga.motelroom;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ThangThanhToanActivity extends AppCompatActivity {

    EditText txtThangThanhToan;
    Button btnThangThanhToan,btnThanhToan;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    private ProgressDialog pDialog;
    int soDienCu, soDienMoi, soNuocCu, soNuocMoi;
    double giaPhong, giaDien, giaNuoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thang_thanh_toan);
        addControls();
        addEvents();
    }

    private boolean validateInput(){
        if(txtThangThanhToan.getText().toString().equals("")){
            //Toast.makeText(ThangThanhToanActivity.this,"Vui lòng chọn tháng",Toast.LENGTH_SHORT).show();
            txtThangThanhToan.setError("Vui lòng chọn tháng");
            DangNhapActivity.showError(txtThangThanhToan,this);
            txtThangThanhToan.requestFocus();
            return false;
        }
        return true;
    }

    private void addEvents() {
        btnThangThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else {
                    String thang = txtThangThanhToan.getText().toString();
                    String maphong = MainActivity.p.getMa_Phong() + "";
                    listDien task1 = new listDien();
                    task1.execute(maphong, thang);
                    listNuoc task2 = new listNuoc();
                    task2.execute(maphong, thang);
                    listGia task3 = new listGia();
                    task3.execute(maphong, thang);
                }
            }
        });
    }

    private void addControls() {
        txtThangThanhToan = (EditText) findViewById(R.id.txtThangThanhToan);
        btnThangThanhToan = (Button) findViewById(R.id.btnThangThanhToan);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
    }

    private void showDatePicker() {
        final DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                txtThangThanhToan.setText(sdf.format(calendar.getTime()));
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(ThangThanhToanActivity.this,
                callBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    class listDien extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ThangThanhToanActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"dien/?maphong="+Integer.parseInt(params[0])+"&thang="+params[1]);

                //mo ket noi
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json;charset = utf8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = br.readLine();
                while(line!=null){
                    builder.append(line);
                    line=br.readLine();
                }
                if(builder.toString().equals("[]")) {
                    soDienCu = 0;
                    soDienMoi=0;
                    giaDien=0;
                }else {
                    JSONArray jsonArray = new JSONArray(builder.toString());
                    //tien hanh mo hinh hoa nguoc tu jsonArray ve mo hinh lop
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            soDienCu = jsonObject.getInt("So_Dien_Cu");
                            soDienMoi = jsonObject.getInt("So_Dien_Moi");
                            giaDien = jsonObject.getDouble("Gia_Dien");
                            Log.d("Sodiencu: ", soDienCu + "");
                            Log.d("Sodienmoi: ", soDienMoi + "");
                        } catch (Exception ex) {
                            Log.e("LOI SO DIEN", "i = " + i + " " + ex.toString());
                        }
                    }
                }

            }catch (Exception ex){
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }
    class listNuoc extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"nuoc/?maphong="+Integer.parseInt(params[0])+"&thang="+params[1]);

                //mo ket noi
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json;charset = utf8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = br.readLine();
                while(line!=null){
                    builder.append(line);
                    line=br.readLine();
                }
                if(builder.toString().equals("[]")) {
                    soNuocCu = 0;
                    soNuocMoi=0;
                    giaNuoc = 0;
                }else {
                    JSONArray jsonArray = new JSONArray(builder.toString());
                    //tien hanh mo hinh hoa nguoc tu jsonArray ve mo hinh lop
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            soNuocCu = jsonObject.getInt("So_Nuoc_Cu");
                            soNuocMoi = jsonObject.getInt("So_Nuoc_Moi");
                            giaNuoc = jsonObject.getDouble("Gia_Nuoc");
                            Log.d("Sonuoccu: ", soNuocCu + "");
                            Log.d("Sonuocmoi: ", soNuocMoi + "");
                        } catch (Exception ex) {
                            Log.e("LOI SO Nuoc", "i = " + i + " " + ex.toString());
                        }
                    }
                }

            }catch (Exception ex){
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }
    class listGia extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            ThanhToanActivity dialog = new ThanhToanActivity(ThangThanhToanActivity.this);
            TextView txtGiaDienThanhToan = (TextView) dialog.findViewById(R.id.txtGiaDienThanhToan);
            TextView txtGiaNuocThanhToan = (TextView) dialog.findViewById(R.id.txtGiaNuocThanhToan);
            TextView txtGiaPhongThanhToan = (TextView) dialog.findViewById(R.id.txtGiaPhongThanhToan);
            TextView txtSoKhoiNuoc = (TextView) dialog.findViewById(R.id.txtSoKhoiNuoc);
            TextView txtSoKyDien = (TextView) dialog.findViewById(R.id.txtSoKyDien);
            TextView txtTongTien = (TextView) dialog.findViewById(R.id.txtTongTienPhong);

            int kydien = soDienMoi - soDienCu;
            int khoinuoc = soNuocMoi - soNuocCu;
            double tongtien = (kydien*giaDien)+(khoinuoc*giaNuoc)+giaPhong;

            txtGiaPhongThanhToan.setText(giaPhong+"");
            txtSoKyDien.setText(kydien+"");
            txtSoKhoiNuoc.setText(khoinuoc+"");
            txtGiaDienThanhToan.setText(giaDien+"");
            txtGiaNuocThanhToan.setText(giaNuoc+"");
            txtTongTien.setText(tongtien+"");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"giaphong/?maphong="+Integer.parseInt(params[0])+"&thang="+params[1]);

                //mo ket noi
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json;charset = utf8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = br.readLine();
                while(line!=null){
                    builder.append(line);
                    line=br.readLine();
                }
                if(builder.toString().equals("[]")) {
                    giaPhong=0;
                }else {
                    JSONArray jsonArray = new JSONArray(builder.toString());
                    //tien hanh mo hinh hoa nguoc tu jsonArray ve mo hinh lop
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            giaPhong = jsonObject.getDouble("Gia");
                            Log.d("giaPhong: ", giaPhong + "");
                        } catch (Exception ex) {
                            Log.e("LOI GIA PHONG", "i = " + i + " " + ex.toString());
                        }
                    }
                }

            }catch (Exception ex){
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }
}
