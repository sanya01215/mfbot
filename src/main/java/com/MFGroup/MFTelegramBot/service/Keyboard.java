package com.MFGroup.MFTelegramBot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Set;

public interface Keyboard {
    ReplyKeyboardMarkup mainKbInit();
    ReplyKeyboardMarkup continueCancelKbInit();
    InlineKeyboardMarkup regQuizAttachKbInit(Set<String> quizSet);

}
