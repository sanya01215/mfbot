package com.MFGroup.MFTelegramBot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;

import java.util.*;

import static com.MFGroup.MFTelegramBot.factory.KeyboardNameEnum.*;
import static com.MFGroup.MFTelegramBot.dao.BotData.KeyboardFactoryButtonsText.*;

@Component
public class KeyboardFactory {
    private final Map<KeyboardNameEnum, ReplyKeyboard> keyboardMap;
    public KeyboardFactory() {
        keyboardMap = new HashMap<>();
    }

    public ReplyKeyboardMarkup getMainReplyKeyBoardMarkup() {
        return (ReplyKeyboardMarkup) getKeyBoardFromCacheOrMake(MAIN_REPLY_KEYBOARD, ReplyKeyboardMarkup.class, MAIN_REPLY_BUTTONS_TEXT, 1);
    }

    public ReplyKeyboardMarkup getContinueCancelReplyKeyBoard() {
        return (ReplyKeyboardMarkup) getKeyBoardFromCacheOrMake(CONTINUE_CANCEL_REPLY_KEYBOARD, ReplyKeyboardMarkup.class, CONTINUE_CANCEL_BUTTONS_TEXT, 1);
    }

    public InlineKeyboardMarkup getOkRemoveAttachKeyBoard() {
        return (InlineKeyboardMarkup) getKeyBoardFromCacheOrMake(OK_REMOVE_INLINE_KEYBOARD, InlineKeyboardMarkup.class, OK_REMOVE_BUTTONS_TEXT, 1);
    }

    public InlineKeyboardMarkup getRegQuizInlineKeyBoard() {
        return (InlineKeyboardMarkup) getKeyBoardFromCacheOrMake(REG_QUIZ_INLINE_KEYBOARD, InlineKeyboardMarkup.class, ALL_QUIZ_TAGS, 2);
    }

    public ReplyKeyboardRemove removeReplyKeyBoard() {
        return ReplyKeyboardRemove.builder()
                .selective(false)
                .removeKeyboard(true)
                .build();
    }

    //get cache keyboard or make it and save
    private ReplyKeyboard getKeyBoardFromCacheOrMake(KeyboardNameEnum keyboardNameEnum, Class<? extends ReplyKeyboard> kbClass, List<String> btnNames, int columnCount) {
        if (keyboardMap.containsKey(keyboardNameEnum)) {return keyboardMap.get(keyboardNameEnum);}

        if (kbClass == ReplyKeyboardMarkup.class) {
            ReplyKeyboardMarkup replyKeyboardMarkup = getKeyBoardMarkup(btnNames);
            keyboardMap.put(keyboardNameEnum,replyKeyboardMarkup);
            return replyKeyboardMarkup; }

        if (kbClass == InlineKeyboardMarkup.class) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(getInlineKeyBoardButtons(btnNames, columnCount));
            keyboardMap.put(keyboardNameEnum, inlineKeyboardMarkup);
            return inlineKeyboardMarkup;
        }
        throw new RuntimeException("Problem with keyboard factory cache method");
    }

    //inline keyboards setup
    private List<List<InlineKeyboardButton>> getInlineKeyBoardButtons(Collection<String> btnNames, int columnCount) {
        List<List<InlineKeyboardButton>> resultKB = new ArrayList<>();
        Iterator<String> btnIterator = btnNames.iterator();
        while (btnIterator.hasNext()) {
            String s;
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();

            for (int i = columnCount; i > 0; i--) {
                if (btnIterator.hasNext()) {
                    s = btnIterator.next();
                    InlineKeyboardButton inlBtn = InlineKeyboardButton.builder()
                            .callbackData(s)
                            .text(s)
                            .build();
                    keyboardButtonsRow.add(inlBtn);
                }
            }
            resultKB.add(keyboardButtonsRow);
        }

        return resultKB;
    }

    //reply keyboards setup
    private ReplyKeyboardMarkup getKeyBoardMarkup(List<String> btnNames) {
        ArrayList<KeyboardRow> keyboard = getKeyBoardWithTextButtons(btnNames);
        return setKeyBoardDefaultSettings(keyboard);

    }
    private ArrayList<KeyboardRow> getKeyBoardWithTextButtons(List<String> btnNames) {
        ArrayList<KeyboardRow> resultBoard = new ArrayList<>();
        KeyboardRow keyboardFirstLine = new KeyboardRow();
        btnNames.forEach(keyboardFirstLine::add);
        resultBoard.add(keyboardFirstLine);
        return resultBoard;
    }

    //keyboards settings
    private ReplyKeyboardMarkup setKeyBoardDefaultSettings(ArrayList<KeyboardRow> keyboard) {
        return ReplyKeyboardMarkup.builder()
                .selective(false)
                .resizeKeyboard(false)
                .oneTimeKeyboard(false)
                .keyboard(keyboard)
                .build();
    }
}
