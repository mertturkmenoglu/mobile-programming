package ce.yildiz.android.ui.email;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.databinding.ActivityEmailComposeBinding;
import ce.yildiz.android.util.EmailHelper;

public class EmailComposeActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = EmailComposeActivity.class.getSimpleName();
    private static final int EMAIL_ATTACHMENT_REQUEST_CODE = 1;
    private Uri fileUri;
    private ActivityEmailComposeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailComposeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        Intent comingIntent = getIntent();
        String intentEmail = null;

        try {
            intentEmail = comingIntent.getStringExtra("email");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intentEmail != null) {
            binding.emailFromEt.setText(intentEmail);
        }

        binding.emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from =  binding.emailFromEt.getText().toString().trim();
                String to =  binding.emailToEt.getText().toString().trim();
                String subject = binding.emailSubjectEt.getText().toString().trim();
                String compose =  binding.emailCompose.getText().toString().trim();

                if (from.isEmpty() || to.isEmpty() || subject.isEmpty() || compose.isEmpty()) {
                    Toast.makeText(EmailComposeActivity.this,
                            "Please fill all areas", Toast.LENGTH_SHORT).show();
                } else {
                    EmailHelper helper = new EmailHelper(EmailComposeActivity.this);
                    helper.send(new String[]{ to }, subject, compose, fileUri);
                }
            }
        });

        binding.emailAttachmentBtn.setOnClickListener(new View.OnClickListener() {
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
