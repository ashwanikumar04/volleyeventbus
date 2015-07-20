package ashwani.in.volleyeventbus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ashwani.in.volleyeventbus.events.error.GithubUserErrorEvent;
import ashwani.in.volleyeventbus.events.success.GithubUserSuccessEvent;
import ashwani.in.volleyeventbus.managers.HTTPRequestManager;
import ashwani.in.volleyeventbus.utils.EventBusHook;
import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {

    static String TAG = "VolleyEventBus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @EventBusHook
    public void onEvent(GithubUserSuccessEvent githubUserSuccessEvent) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        Log.d(TAG, githubUserSuccessEvent.getResponse());
    }

    @EventBusHook
    public void onEvent(GithubUserErrorEvent githubUserErrorEvent) {
        Toast.makeText(this, "Error occurred.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, githubUserErrorEvent.getErrorData().getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Button requests = (Button) findViewById(R.id.requests);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HTTPRequestManager<GithubUserSuccessEvent, GithubUserErrorEvent> request = new HTTPRequestManager<>(MainActivity.this, "https://api.github.com/users/ashwanikumar04", GithubUserSuccessEvent.class, GithubUserErrorEvent.class);
                request.execute();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
