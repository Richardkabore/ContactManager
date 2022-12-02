package com.app.contactmanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public String contactsFile = "mycontacts.txt";
    int contactsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        showContacts(MainActivity.this);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_contact_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String positionStr = "0";
                String count = String.valueOf(contactsCount);
                Intent newIntent = new Intent(MainActivity.this, ContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("p", positionStr);
                bundle.putString("n", count);
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });
        setListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showContacts(Context applicationContext) {
        try {
            InputStream inpStream = applicationContext.openFileInput(contactsFile);
            if (inpStream != null) {
                InputStreamReader inpStreamReader = new InputStreamReader(inpStream);
                BufferedReader buffReader = new BufferedReader(inpStreamReader);
                String contactMessage = "";

                while ((contactMessage = buffReader.readLine()) != null) {
                    contactsCount++;
                }
                inpStream.close();
            }
            System.out.println("========TOtal contacts count == " + contactsCount);

            String[] contactsArray = new String[contactsCount];

            InputStream inpStream1 = applicationContext.openFileInput(contactsFile);
            int i = 0;
            if (inpStream1 != null) {
                InputStreamReader inpStrReader1 = new InputStreamReader(inpStream1);
                BufferedReader buffReader1 = new BufferedReader(inpStrReader1);
                String personDetailsStr = "";
                while ((personDetailsStr = buffReader1.readLine()) != null) {
                    String[] contactItems = personDetailsStr.split(",");
                    if (contactItems == null || contactItems.length<=0)
                        continue;
                    if (personDetailsStr.isEmpty()) {
                        continue;
                    }
                    if (contactItems[1].matches(";")) {
                         contactItems[1] = "";
                    }
                        if (contactItems[2].matches(";")) {
                            contactItems[2] = "";
                        }
                        if (contactItems[3].matches(";")) {
                            contactItems[3] = "";
                        }
                        if (contactItems[4].matches(";")) {
                            contactItems[4] = "";
                        }
                        String personDetails = "" + contactItems[0] + "\t" + contactItems[1] + "\t" + contactItems[2];
                        contactsArray[i] = personDetails;
                        i++;
                }

                //Arrays.sort(ret, String.CASE_INSENSITIVE_ORDER);
                if (contactsArray != null || contactsArray.length > 0) {
                    ArrayAdapter<String> viewAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, contactsArray);
                    ListView currentListView = (ListView) findViewById(R.id.view_contact_list);
                    currentListView.setAdapter(viewAdapter);
                }
            }
            inpStream1.close();
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "Opening contacts file failed: " + e.toString());
        } catch (IOException e) {
            Log.e("MainActivity", "Failed to read contacts " + e.toString());
        }
    }

    private void setListViewListener() {
        ListView contactList = (ListView) findViewById(R.id.view_contact_list);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View listView, int currentPosition, long id) {
                currentPosition+=1;
                String positionStr =String.valueOf(currentPosition);
                String totalLines = String.valueOf(contactsCount);
                Intent addContactIntent = new Intent(MainActivity.this, ContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("p",positionStr);
                bundle.putString("n",totalLines);
                addContactIntent.putExtras(bundle);
                startActivity(addContactIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.addStoryItem:
                Toast.makeText(getApplicationContext(), "Add Story selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.chatHistory:
                Toast.makeText(getApplicationContext(), "Chat history selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.viewContacts:
                Toast.makeText(getApplicationContext(), "View Contacts Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}