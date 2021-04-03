package com.example.weacher_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

public class InputDialog {
    // Введенный текст
    private String mText;
    // Родитель
    private Context mParent;
    // Заголовок
    private String mTitle;
    // Представитель
    private DialogDelegate mDelegate = null;

    InputDialog(Context parent, String title){
        mParent = parent;
        mTitle = title;
    }

    public String getText()
        { return mText; }

    public void setDelegate(DialogDelegate delegate)
        { mDelegate = delegate; }

    public void show(){
        // Строитель диалогов
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
        // Установим надпись
        builder.setTitle(mTitle);
        // Создадим текстовое поле
        final EditText input = new EditText(mParent);
        // Установим режим ввода
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // Установим текствое поле
        builder.setView(input);

        // Обработчик на кнопку "ОК"
        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                // Записываем текс
                mText = input.getText().toString();
                //Скрываем диалог
                dialog.dismiss();
                // Сообщим делегату
                if(mDelegate != null)
                    mDelegate.onConfirm();
            }
        });

        // Обработчик на кнопку "Отмена"
        builder.setPositiveButton("Отмена", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        // Покажем диалог
        builder.show();
    }
}
