package com.MFGroup.MFTelegramBot.handler;


import com.MFGroup.MFTelegramBot.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageHandler implements Handler<Message> {
    public final MessageSender messageSender;

    public MessageHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void choose(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = null;
        InlineKeyboardMarkup inlineKeyboardMarkup = null;

        String msg = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        switch (msg.trim()) {
            case "Меню":
                replyKeyboardMarkup = keyboardInit();
                sendMessage.setText("Выберите что Вас интересует");
                break;
            case "Инфо":
                sendMessage.setText("Бот создан в развлекательных и учебных целях. За предложениями и вопросами обращайтесь к @Siabruk. Надеюсь Вам понравится =)");
                break;
            default:
                replyKeyboardMarkup = keyboardInit();
                sendMessage.setText("Пожалуйста выберите пункт из меню :");
                break;
        }
        sendMessage.enableHtml(true);
        if (replyKeyboardMarkup != null) sendMessage.setReplyMarkup(replyKeyboardMarkup);
        else sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        messageSender.sendMessage(sendMessage);
    }


    private ReplyKeyboardMarkup keyboardInit() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();
        keyboardFirstRow.add("Цитата");
        keyboardFirstRow.add("Новости");
        keyboardSecondRow.add("Курс валют");
        keyboardSecondRow.add("Инфо");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup attachCurrencyKeyboardInit() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("Bitcoin");
        inlineKeyboardButton1.setCallbackData("BTC");

        inlineKeyboardButton2.setText("Ethereum");
        inlineKeyboardButton2.setCallbackData("ETH");

        inlineKeyboardButton3.setText("Dogecoin");
        inlineKeyboardButton3.setCallbackData("DOGE");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow3.add(inlineKeyboardButton3);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

}
