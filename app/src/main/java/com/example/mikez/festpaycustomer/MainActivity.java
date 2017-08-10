package com.example.mikez.festpaycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int creditnum = 9999;


    //on button press



    NfcAdapter nfcAdapter;
    Button tglReadWrite;
    Edit
    NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    Button tglReadWrite=(Button) findViewById(R.id.tglReadWrite);

    if( nfcAdapter != null && nfcAdapter.isEnabled()){
        Toast.makeText(this,"Merge",Toast.LENGTH_LONG).show();
    }else
    {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            Toast.makeText(this,"NfcIntent!",Toast.LENGTH_LONG).show();

            if(tglReadWrite.isChecked())
            {

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if(parcelables != null && parcelables.length>0)
                {
                    readTextFromMessage((NdefMessage)parcelables[0]);
                }
                else {Toast.makeText(this,"No NDEF message found!",Toast.LENGTH_LONG).show();}

            }
            else
            {
                Tag tag= intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage=createNdefMessage(txtTagContent.getText()+"");

                writeNdefMessage(tag,ndefMessage);
            }


        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecord = ndefMessage.getRecords();

        if(ndefRecord!=null && ndefRecord.length>0)
        {
            NdefRecord ndelRecord = ndefRecord[0];

            String tagContent= getTextFromNdefRecord(ndefRecord);
            txtTagContent.setText(tagContent);
        }
        else
        {
            Toast.makeText(this,"No NDEF records found!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {



        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {

        super.onPause();
        disableForegroundDispatchSystem();
    }


    private void enableForegroundDispatchSystem()
    {
        Intent intent= new Intent(this,MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = new PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);
    }

    private void disableForegroundDispatchSystem()
    {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage)
    {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable==null){Toast.makeText(this,"It is not Ndef formatable!",Toast.LENGTH_LONG).show();return;}

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Toast.makeText(this,"Tag writen!",Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            Log.e("formatTag",e.getMessage());}
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage)
    {
        try {
            if(tag==null)
            {
                Toast.makeText(this,"Tag object cannot be null!",Toast.LENGTH_SHORT).show();
                return;;
            }

            Ndef ndef =Ndef.get(tag);

            if(ndef==null)
            {

                formatTag(tag,ndefMessage);
            }
            else
            {
                ndef.connect();

                if(!ndef.isWritable())
                {
                    Toast.makeText(this,"Tag in not writable!",Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this,"Tag writen!",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            Log.e("writeNdefMessage",e.getMessage());}
    }

    private NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    public void tglReadWriteOnClick(View view){
        txtTagContent.setText("");
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;

        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0]&128)==0)? "UTF-8":"UTF=16";
            int languageSize=payload[0]&0063;
            tagContent=new String(payload,languageSize+1,payload.length-languageSize-1,textEncoding));

        }catch (UnsupportedEncodingException e)
        {
            Log.e("getTextFromNdefRecord",e.getMessage(),e);
        }
        return tagContent;
    }



    ///////








    //pay history products logout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView creditNumberText = (TextView) findViewById(R.id.main_text_creditnumber);
        creditNumberText.setText("You have " + String.valueOf(creditnum) + " credits");
        Button buttonPay = (Button) findViewById(R.id.main_button_pay);
        Button buttonHistory = (Button) findViewById(R.id.main_button_history);
        Button buttonProducts = (Button) findViewById(R.id.main_button_products);
        Button buttonLogOut = (Button) findViewById(R.id.main_button_logout);

        buttonPay.setOnClickListener(this);
        buttonHistory.setOnClickListener(this);
        buttonProducts.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_button_logout:
                Intent intentBack = new Intent(this, LoginActivity.class);
                startActivity(intentBack);
                finish();
                break;
            case R.id.main_button_pay:
                Toast.makeText(this, "Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_button_history:
                Intent intentHistory = new Intent(this, HistoryActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.main_button_products:
                Intent intentProducts = new Intent(this, ProductsActivity.class);
                startActivity(intentProducts);
                break;

        }

    }

}
