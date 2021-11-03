package com.MFGroup.MFTelegramBot.handler.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

public class KeyBoard {

    public static ReplyKeyboardMarkup mainKbInit() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //kb init
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        //kb settings
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        //kb buttons
        keyboardFirstRow.add("Get all users");
        keyboardFirstRow.add("Delete all users");
        keyboardSecondRow.add("Find best match user");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        //kb set on markup
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup continueCancelKbInit() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //kb init
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        //kb settings
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        //kb buttons
        keyboardFirstRow.add("Ok. Let's go! ");
        keyboardFirstRow.add("Cancel");
        keyboard.add(keyboardFirstRow);

        //kb set on markup
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup regQuizAttachKbInit(Set<String> quizSet) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Iterator<String> setIterator = quizSet.iterator();
//add quiz buttons
        while(setIterator.hasNext()){
            String s;
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            for(int i=2;i>0;i--) {
                if(setIterator.hasNext()) {
                    s = setIterator.next();
                    InlineKeyboardButton inlBtn = InlineKeyboardButton.builder()
                            .callbackData(s)
                            .text(s)
                            .build();
                    keyboardButtonsRow1.add(inlBtn);
                }
            }


            rowList.add(keyboardButtonsRow1);
        }
//add Ok button to quiz
        List<InlineKeyboardButton> okButtonRow = new ArrayList<>();
        okButtonRow.add(InlineKeyboardButton.builder().text("OK. Done it!").callbackData("OK. Done it!").build());
        rowList.add(okButtonRow);
//
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
