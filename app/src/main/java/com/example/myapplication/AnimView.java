package com.example.myapplication;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimView {
    public TextView email, pass, text, textView4, signIn, name, phone, textView;
    public Button log;
    public ImageView google;
    public float v = 0;

    public AnimView(TextView email, TextView pass, TextView text, TextView textView4, TextView signIn, Button log) {
        this.email = email;
        this.pass = pass;
        this.text = text;
        this.textView4 = textView4;
        this.signIn = signIn;
        this.log = log;
    }

    public AnimView(EditText editTextEmail, EditText editTextName, EditText editTextPass, EditText editTextPhone, Button reg, TextView signIn, TextView reg_acc, TextView info, TextView singUp) {
        this.email = editTextEmail;
        this.pass = editTextPass;
        this.name = editTextName;
        this.phone = editTextPhone;
        this.text = info;
        this.textView4 = reg_acc;
        this.signIn = singUp;
        this.log = reg;
        this.textView = signIn;
    }

    public void animLogin() {
        signIn.setTranslationY(800);
        log.setTranslationY(800);
        email.setTranslationY(800);
        pass.setTranslationY(800);
        text.setTranslationY(800);
        textView4.setTranslationY(800);

        pass.setAlpha(v);
        email.setAlpha(v);
        log.setAlpha(v);
        signIn.setAlpha(v);
        text.setAlpha(v);
        textView4.setAlpha(v);

        textView4.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        log.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        signIn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        text.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();
    }

    public void animRegister(){
        signIn.setTranslationY(800);
        log.setTranslationY(800);
        email.setTranslationY(800);
        pass.setTranslationY(800);
        text.setTranslationY(800);
        textView4.setTranslationY(800);
        name.setTranslationY(800);
        phone.setTranslationY(800);
        textView.setTranslationY(800);

        pass.setAlpha(v);
        email.setAlpha(v);
        log.setAlpha(v);
        signIn.setAlpha(v);
        text.setAlpha(v);
        textView4.setAlpha(v);
        name.setAlpha(v);
        phone.setAlpha(v);
        textView.setAlpha(v);

        textView4.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        log.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();
        signIn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        text.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        name.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        phone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        textView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1100).start();
    }
}

