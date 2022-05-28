package com.arup.yahmoney.data;

import java.util.Arrays;

public class Console {
    private static final int MAXIMUM_NUMBER_IN_STRING = 2;

    private static String NAME;
    private static String PHONE_NO;
    private static String AMOUNT;
    private static String COMMAND_TO;
    private static String DATE;

    private static String[] COMMAND_ARRAY;

    public String getName() {
        return NAME;
    }

    public String getContact() {
        return PHONE_NO;
    }

    public String getAmount() {
        return AMOUNT;
    }

    public String getCommandTo() {
        return COMMAND_TO;
    }

    public String getDate() {
        return DATE;
    }

    public static boolean tryConsole(String command) {
        COMMAND_ARRAY = command.split(" ");
        if(COMMAND_ARRAY.length > 1) {
            return false;
        }
        Console console = new Console(null);
        return console.FindNumberAvailability();
    }

    public Console(String command) {
        command = command.replace("+91", "");
        COMMAND_ARRAY = command.split(" ");
        FindName();
        FindPhoneNo();
        FindCommandTo();
    }

    private boolean FindNumberAvailability() {
        for(String str : COMMAND_ARRAY) {
            if(CheckNumber(str) != -1) { //AMOUNT
                return true;
            }
        }
        return false;
    }

    private long CheckNumber(String str) {
        try {
            return Long.parseLong(str);
        }
        catch (Exception e) {
            return -1;
        }
    }

    private void FindName() {
        StringBuilder stringBuilder = new StringBuilder();
        for(String str : COMMAND_ARRAY) {
            if(!str.contains("/") && !str.contains("-")) {
                if(str.length() != 1 && CheckNumber(str) == -1) {
                    stringBuilder.append(str);
                    stringBuilder.append(" ");
                }
            }
            else {
                FindDate(str);
            }
        }
        NAME = stringBuilder.substring(0, stringBuilder.length() - 1).trim();
    }

    private void FindPhoneNo() {
        String[] data = new String[3];
        int i = 0;
        for (String str : COMMAND_ARRAY) {
            if (CheckNumber(str) != -1) {
                data[i] = str;
                i++;
            }
        }
        if (i <= MAXIMUM_NUMBER_IN_STRING) {
            String a = data[0];
            String b = data[1];
            if (a.length() < b.length() && b.length() >= 8 && b.charAt(0) >= 54) {
                PHONE_NO = b;
                AMOUNT = a;
            } else if (a.length() > b.length() && a.length() >= 8 && a.charAt(0) >= 54) {
                PHONE_NO = a;
                AMOUNT = b;
            }
        }
    }

    private void FindCommandTo() {
        char c = 9;
        for(String str : COMMAND_ARRAY) {
            if(str.length() == 1) {
                c = str.charAt(0);
            }
        }
        if(c == 'i' || c == 'd') {
            COMMAND_TO = "DEBIT";
        }
        else if(c == 'o' || c == 'c') {
            COMMAND_TO = "CREDIT";
        }
    }

    private void FindDate(String str) {
        if(!str.equals("")) {
            String[] arr;
            if (str.contains("/")) {
                arr = str.split("/");
            }
            else {
                arr = str.split("-");
            }

            int[] ThirtyOneDateMonths = {1, 3, 5, 7, 8, 10, 12};
            try {
                int date = Integer.parseInt(arr[0]);
                int month = Integer.parseInt(arr[1]);
                int year = Integer.parseInt(arr[2]);
                if(!(
                        arr.length != 3
                        || date > 31
                        || month > 12
                        || Arrays.stream(ThirtyOneDateMonths).noneMatch(a -> a == month) && date == 31
                        || year % 4 != 0 && month == 2 && date >= 29
                        || year < 2000
                )) {
                    DATE = str.replace("/", "-");
                }
            }
            catch (Exception ignored) {}
        }
    }
}
