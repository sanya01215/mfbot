package com.mfgroup.tgbot.botdata;

import java.util.List;

public class BotData {
    public static class KeyboardFactoryButtonsText {
        public static final List<String> MAIN_REPLY_BUTTONS_TEXT = List.of("Get all users", "Delete me", "Find best match user");
        public static final List<String> CONTINUE_CANCEL_BUTTONS_TEXT = List.of("Ok", "Cancel");
        public static final List<String> OK_REMOVE_BUTTONS_TEXT = List.of("OK", "REMOVE");
        public static final List<String> ALL_QUIZ_TAGS = List.of("eng", "ukr", "rus", "it", "psychology", "sport", "drawing", "nature", "business", "cryptocurrency", "travel", "law");
    }

    public static class BeforeRegMessageHandlerSpeech {
        public static final String START_HELLO = "Hello! There is The Bot for meeting some new interesting people : \n" +
                "Please answer for a few questions, that help us to find you suitable person. \n" +
                "Wait your choose...";
        public static final String TYPE_NAME = "Type your Full Name";
        public static final String NOW_QUIZ = "Now quiz. Please, tap all buttons which topic you prefer to conversation\nand then tap <Ok> : ";
        public static final String INCORRECT = "Incorrect.Please try again";
        public static final String TYPE_CITY = "Type your city";
        public static final String TYPE_AGE = "Type your age";
    }

    public static class AfterRegMessageHandlerSpeech {
        public static final String GET_USERS = "Get all users";
        public static final String DELETE_ME = "Delete me";
        public static final String FIND_MATCH_USER = "Find best match user";
        public static final String YOUR_MATCH_USER = "Your best match user: ";
        public static final String INVALID_COMMAND = "Command is not correct";
        public static final String CAN_CONTACT = "You can contact with the user by: ";
    }

    public static class CallbackQueryHandlerSpeech {
        public static final String ALL_DONE = "All done. Congratulations!!!";
        public static final String YOUR_TAGS = "Your tags:";
        public static final String TAGS_DELIMITER = " || ";
    }

    public enum UserPositionEnum {
        START,
        ACCEPT,
        INPUT_FULLNAME,
        INPUT_AGE,
        INPUT_CITY,
        INPUT_QUIZ,
        NONE,
        DONE_REGISTRATION
    }
}
