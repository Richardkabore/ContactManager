package com.app.contactmanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContactActivity extends AppCompatActivity {
    public EditText firstNameField;
    public EditText lastNameField;
    public EditText phoneField;
    public EditText emailField;
    public EditText dobField;

    public Button saveButton;
    public Button removeButton;
    public int cotactsCount;
    public int idEditIndex = -1;
    public String contactFileName = "mycontacts.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        firstNameField = (EditText) findViewById(R.id.contactFname);
        lastNameField = (EditText) findViewById(R.id.contactLname);
        phoneField = (EditText) findViewById(R.id.contactPhone);
        emailField = (EditText) findViewById(R.id.contactEmail);
        dobField = (EditText) findViewById(R.id.contactDob);

        saveButton = (Button) findViewById(R.id.addContactButton);
        removeButton = (Button) findViewById(R.id.removeContactButton);
        removeButton.setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        String posStr = bundle.getString("p");
        String contactStr = bundle.getString("n");

        int posId = Integer.parseInt(posStr);
        cotactsCount = Integer.parseInt(contactStr);

        final String[] contactsList = new String[cotactsCount];
        try {
            InputStream inpStream = this.openFileInput(contactFileName);
            int i = 0;
            if (inpStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inpStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    contactsList[i] = receiveString;
                    i++;
                }
                 //Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
                inpStream.close();

            }
        } catch (FileNotFoundException e1) {
            Log.e("ContactActivity", "Error " + e1.toString());
        } catch (IOException e1) {
            Log.e("ContactActivity", "Error: " + e1.toString());
        }
        if (posId > 0) {
            updateContactDetails(contactsList, posId);
        }
       saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveButtonClickHandler(ContactActivity.this, contactsList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removeButtonHandler(ContactActivity.this, contactsList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeButtonHandler(Context context, String[] contactsList) throws IOException {
        OutputStreamWriter copyStreamWriter = new OutputStreamWriter(context.openFileOutput(contactFileName, Context.MODE_PRIVATE));
        String result = "";
        copyStreamWriter.write(result);
        copyStreamWriter.close();
        OutputStreamWriter outStreamWr1 = new OutputStreamWriter(context.openFileOutput(contactFileName, Context.MODE_PRIVATE));

        for (int i = 0; i < cotactsCount; i++) {
            if (i != idEditIndex) {
                outStreamWr1.write(contactsList[i] + "\n");
            }
        }

        outStreamWr1.close();
        Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void saveButtonClickHandler(Context applicationContext, String[] contactList) throws IOException {
        if (firstNameField.getText().toString().matches("")) {
            Toast.makeText(applicationContext, "First Name cannot be empty", Toast.LENGTH_SHORT).show();
            firstNameField.requestFocus();
        }
        else
        {
            if (lastNameField.getText().toString().matches("")) {
                lastNameField.setText(";");
            }
            if (phoneField.getText().toString().matches("")) {
                phoneField.setText(";");
            }
            if (emailField.getText().toString().matches("")) {
                emailField.setText(";");
            }
            if (dobField.getText().toString().matches("")) {
                dobField.setText(";");
            }

            if (idEditIndex < 0) {
                try {
                    String newContactStr = "";
                    if (cotactsCount > 0)
                        newContactStr = "\n" + firstNameField.getText().toString() + "," + lastNameField.getText().toString() + "," + phoneField.getText().toString() + "," + emailField.getText().toString() + "," + dobField.getText().toString();
                    else
                        newContactStr = firstNameField.getText().toString() + "," + lastNameField.getText().toString() + "," + phoneField.getText().toString() + "," + emailField.getText().toString() + "," + dobField.getText().toString();
                    FileOutputStream fInpStream = openFileOutput(contactFileName, applicationContext.MODE_APPEND);
                    OutputStreamWriter outStreamWriter = new OutputStreamWriter(applicationContext.openFileOutput(contactFileName, Context.MODE_APPEND));
                    String result = newContactStr;
                    outStreamWriter.write(result);
                    outStreamWriter.close();
                    System.out.println("============Writing to file " + result);
                    Toast.makeText(applicationContext, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
            {
                OutputStreamWriter outStreamWriter = new OutputStreamWriter(applicationContext.openFileOutput(contactFileName, Context.MODE_PRIVATE));
                String result = "";
                outStreamWriter.write(result);
                outStreamWriter.close();
                OutputStreamWriter outStreamWriter1 = new OutputStreamWriter(applicationContext.openFileOutput(contactFileName, Context.MODE_PRIVATE));

                for (int i = 0; i < cotactsCount; i++) {
                    if (i == idEditIndex) {
                        String addStr = firstNameField.getText().toString() + "," + lastNameField.getText().toString() + "," + phoneField.getText().toString() + "," + emailField.getText().toString() + "," + dobField.getText().toString();
                        outStreamWriter1.write(addStr);
                    } else {
                        outStreamWriter1.write(contactList[i]);
                    }
                    if (i!= cotactsCount -1) {
                        outStreamWriter1.write("\n");
                    }
                }  outStreamWriter1.close();

                Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    public void updateContactDetails(String[] contactsList, int posidToUpdate) {
        String[] details = contactsList[posidToUpdate - 1].split(",");
        idEditIndex = posidToUpdate - 1;
        for(int i=1;i<5;i++)
        {
            if(details[i].matches(";"))
                details[i]="";
        }

        firstNameField.setText(details[0]);
        lastNameField.setText(details[1]);
        phoneField.setText(details[2]);
        emailField.setText(details[3]);
        dobField.setText(details[4]);
        removeButton.setEnabled(true);
    }

}
