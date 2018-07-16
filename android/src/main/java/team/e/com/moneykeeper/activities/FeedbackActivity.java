package team.e.com.moneykeeper.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;

import team.e.com.moneykeeper.R;
import team.e.com.moneykeeper.fragments.FeedbackFragment;


public class FeedbackActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertFragment(new FeedbackFragment());
        setupActionBar();
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void onSendMessage(View view) {
        EditText messageView = (EditText) findViewById(R.id.message);
        String messageText = messageView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("kmb99@mail.ru") +
                "?subject=" + Uri.encode("Feedback") +
                "&body=" + Uri.encode(messageText);
        Uri uri = Uri.parse(uriText);
        intent.setData(uri);
        startActivity(Intent.createChooser(intent,"Отправить через..."));
    }
}
