package ce.yildiz.android.ui.email;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.R;
import ce.yildiz.android.util.EmailHelper;

public class EmailComposeActivity extends AppCompatActivity {
    private static final String TAG = EmailComposeActivity.class.getSimpleName();
    private static final int EMAIL_ATTACHMENT_REQUEST_CODE = 1;
    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_compose);

        final EditText fromET = findViewById(R.id.email_from_et);
        final EditText toET = findViewById(R.id.email_to_et);
        final EditText subjectET = findViewById(R.id.email_subject_et);
        final EditText composeET = findViewById(R.id.email_compose);
        ImageButton sendBtn = findViewById(R.id.email_send_button);
        ImageButton attachmentBtn = findViewById(R.id.email_attachment_btn);

        Intent comingIntent = getIntent();
        String intentEmail = null;

        try {
            intentEmail = comingIntent.getStringExtra("email");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intentEmail != null) {
            fromET.setText(intentEmail);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = fromET.getText().toString().trim();
                String to = toET.getText().toString().trim();
                String subject = subjectET.getText().toString().trim();
                String compose = composeET.getText().toString().trim();

                if (from.isEmpty() || to.isEmpty() || subject.isEmpty() || compose.isEmpty()) {
                    Toast.makeText(EmailComposeActivity.this, "Please fill all areas", Toast.LENGTH_SHORT).show();
                } else {
                    EmailHelper helper = new EmailHelper(EmailComposeActivity.this);
                    helper.send(new String[]{ to }, subject, compose, fileUri);
                }
            }
        });

        attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                Intent i = Intent.createChooser(intent, "File");
                startActivityForResult(i, EMAIL_ATTACHMENT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EMAIL_ATTACHMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();
            }
        }
    }
}
