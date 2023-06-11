package com.example.simplechat;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainActivity extends AppCompatActivity {
    private final int SIGN_IN_CODE = 1;
    private RelativeLayout activityMain;
    private FirebaseListAdapter<Message> adapter;

    private FloatingActionButton logoutButton;

    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton;
    private ImageView submitButton;
    private EmojIconActions emojIconActions;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activityMain, "You are logged in", Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
            } else {
                Snackbar.make(activityMain, "You are not authorized", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = findViewById(R.id.activity_main);
        submitButton = findViewById(R.id.btnSend);
        emojiButton = findViewById(R.id.emoji_button);
        emojiconEditText = findViewById(R.id.textField);
        emojIconActions = new EmojIconActions(getApplicationContext(), activityMain, emojiconEditText, emojiButton);
        emojIconActions.ShowEmojIcon();
        logoutButton = findViewById(R.id.logout);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiconEditText.getText().toString().equals("")) {
                    return;
                }
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                emojiconEditText.getText().toString()));
                emojiconEditText.setText("");
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),
                        SIGN_IN_CODE);
            }
        });


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_CODE);
        } else {
            Snackbar.make(activityMain, "You are logged in", Snackbar.LENGTH_SHORT).show();
            displayAllMessages();
        }
    }

    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageUser, messageTime;
                BubbleTextView messageText;
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                messageText = v.findViewById(R.id.message_text);

                messageUser.setText(model.getUserName());
                messageText.setText(model.getTextMessage());
                messageTime.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}