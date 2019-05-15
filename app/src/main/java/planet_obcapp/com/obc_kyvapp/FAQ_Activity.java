package planet_obcapp.com.obc_kyvapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet_obcapp.com.obc_kyvapp.Adapter.ExpandableListAdapter;

/**
 * Created by Admin on 4/21/2017.
 */

public class FAQ_Activity extends AppCompatActivity {


   private   ExpandableListView expListView;
   private   List<String> listDataHeader;
   private   HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);

        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expListView = (ExpandableListView) findViewById(R.id.lvExp);


        prepareListData();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);


        expListView.setAdapter(listAdapter);


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });


        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });



        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();






        listDataHeader.add("What is Contact Point Verification (CPV)?");
        listDataHeader.add("What are the Key benefits of CPV?");
        listDataHeader.add("Prerequisites");
        listDataHeader.add("Why do we need Contact Point Verification application?");
        listDataHeader.add("Due to poor connectivity I could n't submit form , can I submit now?");
        listDataHeader.add("Branch's mail box was full at the time I submit the visit record, can I submit now?");
        listDataHeader.add("I forgot my password, how can I reset it?");
        listDataHeader.add("What is the OS version required to run the application?");
        listDataHeader.add("Is it available for IOS and Windows supported handset?");
        listDataHeader.add("In case of any clarification , where should I contact?");


        List<String> ans1 = new ArrayList<String>();
        ans1.add("Contact Point Verification is an application developed for staff only, to do KYC verification of proprietorship accounts. Application is designed in such a way, it captures all the mandatory fields along with Geo co-ordinates and photographs of the location.  After filling up the required details , user submit the application , which will be send to branch email id in pdf format. Branch shall take printout of the visit record and taken signature of visiting official. This will be kept with account opening form and  used as second KYC document for proprietorship firm");




        List<String> ans2 = new ArrayList<String>();
        ans2.add("1. It helps staff to fill all the required field in predefined approved format in mobile application.");
        ans2.add("2. Customer KYC is done at the doorstep of customer");
        ans2.add("3. It works as supplement to letter of thanks to Customer and provide additional tool to  bank to confirm customer's address.");






        List<String> ans3 = new ArrayList<String>();
        ans3.add("1. Application is designed for staff only, user having Smart phone can access the application.");
        ans3.add("2. Internet connection on smart phone is required to access application.");



        List<String> ans4 = new ArrayList<String>();
        ans4.add("As per bank's guidelines, for proprietor account we need two documentary proof to open a current account , However, in cases where the banks are satisfied that it is not possible to furnish two such documents, they would have the discretion to accept only one of those documents as activity proof. In such cases, the banks would have to undertake contact point verification, collect such information as would be required to establish the existence of such firm, confirm, clarify and satisfy themselves that the business activity has been verified from the address of the proprietary concern");

        List<String> ans5 = new ArrayList<String>();
        ans5.add("Yes , user can submit the application as it will be saved in old records, user can view and resent the application.");

        List<String> ans6 = new ArrayList<String>();
        ans6.add("Yes , user can submit the application as it will be saved in old records, user can view and resent the application.");

        List<String> ans7 = new ArrayList<String>();
        ans7.add("User can regenerate password by putting his sol id in \"forgot password\" application will send the password of user to branch mail id.");

        List<String> ans8 = new ArrayList<String>();
        ans8.add("Currently the App is supported Android handset with 4.1 & above versions.");

        List<String> ans9 = new ArrayList<String>();
        ans9.add("It is available for IOS but not for windows.");

        List<String> ans10 = new ArrayList<String>();
        ans10.add("In case of any support/ clarification you need to contact at pnd@obc.co.in");


        listDataChild.put(listDataHeader.get(0), ans1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), ans2);
        listDataChild.put(listDataHeader.get(2), ans3);
        listDataChild.put(listDataHeader.get(3), ans4);
        listDataChild.put(listDataHeader.get(4), ans5);
        listDataChild.put(listDataHeader.get(5), ans6);
        listDataChild.put(listDataHeader.get(6), ans7);
        listDataChild.put(listDataHeader.get(7), ans8);
        listDataChild.put(listDataHeader.get(8), ans9);
        listDataChild.put(listDataHeader.get(9), ans10);


    }

    }

