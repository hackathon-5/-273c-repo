package com.sparcedge.n273c.charlestontech;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//import com.firebase.client.Firebase;


/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EventListFragment} and the item details
 * (if present) is a {@link EventDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EventListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class EventListActivity extends Activity
        implements EventListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    myDB mDB;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mDB = new myDB(this,null,null,1);
        lv = (ListView) findViewById(R.id.listView);

        if (findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((EventListFragment) getFragmentManager()
                    .findFragmentById(R.id.event_list))
                    .setActivateOnItemClick(true);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String temp = (String) lv.getItemAtPosition(position);
                author = temp;
                String a = "'";
                if (author.contains(a)){
                    author.replaceAll(a,"%20");
                }
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                intent.putExtra("query", author);
                HomeActivity.this.startActivity(intent);*/

            }
        });



//        Firebase.setAndroidContext(this);

            //Get Json objects
            String data = "";
            try {
                data = new Getdata().execute().get();
            }
            catch (Exception e){
            }
            initList(data);

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link EventListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */

    public void updatelist(ArrayList al){
        yourAdapter<Events> mAdapter = new yourAdapter<Events>(this, al);
        lv.setAdapter(mAdapter);
    }

    private void initList(String data) {
        ArrayList<Events> al = new ArrayList<>();
        try{
            JSONArray jb = new JSONArray(data);
            for (int i = 0; i < jb.length(); i++){
                JSONObject jarr = jb.getJSONObject(i);

                //date = new parser.parse(jarr.getString("date"));
                String dateString = jarr.getString("date");
                String[] parts = dateString.split("T");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = df.parse(parts[0]);

                String website = jarr.getString("website");
                String desc = jarr.getString("description");
                String name = jarr.getString("name");
                Events ev = new Events(name, date, website, desc);
                al.add(ev);
            }
            updatelist(al);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        yourAdapter<Events> mAdapter = new yourAdapter(this, al);
        lv.setAdapter(mAdapter);
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(EventDetailFragment.ARG_ITEM_ID, id);
            EventDetailFragment fragment = new EventDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.event_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EventDetailActivity.class);
            detailIntent.putExtra(EventDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private class Getdata extends AsyncTask<String, Void, String> {
        private Exception e;

        protected String doInBackground(String ...query){
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String request = "https://fierce-caverns-8423.herokuapp.com/api/events";
            HttpGet httpGet = new HttpGet(request);
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e("APP", "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
        protected void onPostExecute(String result){
        }

        protected void onPreExecute() {}

        protected void onProgressUpdate(Void... values) {}
    }
}


class yourAdapter<Events> extends BaseAdapter {

    Context context;
    ArrayList<Events> data;
    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, ArrayList<Events> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView text = (TextView) vi.findViewById(R.id.eventName);
        text.setText(data.get(position).toString());
        return vi;
    }
}

