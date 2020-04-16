package ce.yildiz.android.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.databinding.ActivityNoteEditBinding;

public class NoteEditActivity extends AppCompatActivity {
    private ActivityNoteEditBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteEditBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        Intent intent = getIntent();
        
        if (intent != null) {
            String noteName = intent.getStringExtra("noteName");
        }
    }
}
