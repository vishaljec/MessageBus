package com.android.messagebusexample;


public final class Messages {
    @SuppressWarnings("PublicInnerClass")
    public static final class Destinations {
        public static final String SYSTEM_SHUT_DOWN = "com.android.messagebusexample.SYSTEM_SHUT_DOWN";


        private Destinations() {
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static final class Actions {

        public static final String START = "start";
        public static final String END = "end";
        public static final String FINISH = "finish";


        private Actions() {
        }
    }

    private Messages() {
        //static class
    }
}
