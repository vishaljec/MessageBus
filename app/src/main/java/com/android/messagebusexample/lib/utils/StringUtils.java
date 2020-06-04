package com.android.messagebusexample.lib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.messagebusexample.lib.utils.Assert;
import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnnecessaryParentheses")
public final class StringUtils {
    private static final int HEX_RADIX = 16;
    private static final String ALPHA_NUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final char[] AMBIGUOUS_CHARACTERS = new char[]{
            // Looks like SPACE character
            '\u00A0',    //NO-BREAK SPACE
            '\u2000',    //EN QUAD
            '\u2001',    //EM QUAD
            '\u2002',    //EN SPACE (nut)
            '\u2003',    //EM SPACE (mutton)
            '\u2004',    //THREE-PER-EM SPACE (thick space)
            '\u2005',    //FOUR-PER-EM SPACE (mid space)
            '\u2006',    //SIX-PER-EM SPACE
            '\u2007',    //FIGURE SPACE
            '\u2008',    //PUNCTUATION SPACE
            '\u2009',    //THIN SPACE
            '\u200A',    //HAIR SPACE
            '\u202F',    //NARROW NO-BREAK SPACE
            '\u205F',    //MEDIUM MATHEMATICAL SPACE
            '\u3000',    //IDEOGRAPHIC SPACE
            // Looks like HYPHEN-MINUS character
            '\u2010',    //HYPHEN
            '\u2011',    //NON-BREAKING HYPHEN
            '\u2012',    //FIGURE DASH
            '\u2013',    //EN DASH
            '\u2014',    //EM DASH (nut)
            '\u2015',    //HORIZONTAL BAR
            '\uFE58',    //SMALL EM DASH
            '\uFE63',    //SMALL HYPHEN-MINUS
            '\uFF0D',    //FULLWIDTH HYPHEN-MINUS
            // Looks like QUOTATION MARK character
            '\u201C',    //LEFT DOUBLE QUOTATION MARK
            '\u201D',    //RIGHT DOUBLE QUOTATION MARK
            // Looks like APOSTROPHE character
            '\u0060',    //GRAVE ACCENT
            '\u00B4',    //ACUTE ACCENT
            '\u2018',    //LEFT SINGLE QUOTATION MARK
            '\u2019'     //RIGHT SINGLE QUOTATION MARK
    };
    private static final CharMatcher AMBIGUOUS_CHARACTER_MATCHER = CharMatcher.anyOf(new String(AMBIGUOUS_CHARACTERS));

    private StringUtils() {
    }

    /**
     * Replacement for TextUtils.isEmpty.
     * Does not require Robolectric when used in tests
     */
    public static boolean isEmpty(@Nullable CharSequence text) {
        return text == null || text.length() == 0;
    }

    /**
     * Copied directly from android.text.TextUtils.isDigitsOnly()
     * Copyright (C) 2006-2015 The Android Open Source Project
     */
    @SuppressWarnings({"PMD.OneDeclarationPerLine", "squid:ForLoopCounterChangedCheck"})
    // third-party code
    public static boolean isDigitsOnly(CharSequence str) {
        final int len = str.length();
        for (int cp, i = 0; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(str, i);
            if (!Character.isDigit(cp)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhitespacesOnly(@NotNull CharSequence str) {
        boolean isEmpty = true;

        for (int i = 0; i < str.length(); i++) {
            isEmpty &= Character.isWhitespace(str.charAt(i));
        }

        return isEmpty;
    }

    @Nullable
    public static String removeQuotes(@Nullable String str) {
        String result = str;

        if ((str != null) && (str.length() > 0) && (str.charAt(0) == '"')) {
            int pos = str.indexOf('"', 1);
            result = str.substring(1, (pos > 0) ? pos : (str.length() - 1));
        }

        return result;
    }

    @NotNull
    public static List<String> split(@Nullable String str, @NotNull String separatorRegex) {
        List<String> list = new ArrayList<String>();
        if (str == null) {
            return list;
        }

        String[] split = str.split(separatorRegex);
        Collections.addAll(list, split);

        return list;
    }

    /**
     * splits the string and includes only non-empty and unique elements
     */
    public static List<String> splitForValidTokens(String s, String separator) {
        Assert.hasLength(separator, "separator parameter can't be null or empty.");

        List<String> list = new ArrayList<String>();
        if (s == null) {
            return list;
        }

        for (String token : s.split(separator)) {
            if ((isValid(token, 1)) && (!list.contains(token))) {
                list.add(token);
            }
        }

        return list;
    }

    /**
     * validates if the string is non-empty (i.e. contains other than space characters) and has a proper length
     */
    public static boolean isValid(String text, int minLength) {
        return (text != null) && (text.trim().length() >= minLength);
    }

    public static String joinAppendLast(Iterable<?> list, String separator) {
        StringBuilder buf = new StringBuilder();

        for (Object item : list) {
            buf.append(item);
            buf.append(separator);
        }

        return buf.toString();
    }


    @Deprecated
    public static String join(Iterable<?> list, String separator) {
        String joined = joinAppendLast(list, separator);

        if (list.iterator().hasNext()) {
            StringBuilder buf = new StringBuilder(joined);
            buf.replace(buf.length() - separator.length(), buf.length(), "");
            return buf.toString();
        }

        return joined;
    }

    @Nullable
    public static String byteArrayToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(data.length * 2);
        for (byte b : data) {
            buf.append(String.format("%02X", b));
        }
        return buf.toString();
    }

    public static Optional<byte[]> hexToByteArray(@Nullable String hex) {
        if ((hex != null) && ((hex.length() % 2) == 0)) {
            int length = hex.length();

            byte[] result = new byte[length / 2];
            int k = 0;
            for (int i = 0; i < length; i += 2) {
                String hexNum = hex.substring(i, i + 2);

                Optional<Integer> n = parseInteger(hexNum, HEX_RADIX);
                if (!n.isPresent()) {
                    return Optional.absent();
                }
                result[k] = n.get().byteValue();
                k++;
            }
            return Optional.of(result);
        } else {
            return Optional.absent();
        }
    }

    public static Optional<Integer> parseInteger(@Nullable String string, int radix) {
        if (string == null) {
            return Optional.absent();
        }

        try {
            return Optional.of(Integer.parseInt(string, radix));
        } catch (NumberFormatException e) {
            return Optional.absent();
        }
    }

    public static String getSha1(String textToSha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String text = Optional.fromNullable(textToSha).isPresent() ? textToSha : "";
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        return byteArrayToHex(md.digest());
    }

    /**
     * Never throws exception, may not return SHA1 of the text
     */
    public static String safeSha1(String textToSafeSha) {
        String text = Optional.fromNullable(textToSafeSha).isPresent() ? textToSafeSha : "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            return byteArrayToHex(md.digest());
        } catch (Exception ex) {
            return String.valueOf(text.hashCode());
        }
    }

    @Nullable
    public static String removeSingleQuotes(@Nullable String text) {
        String result = text;
        if ((text != null) && (text.length() > 0) && (text.charAt(0) == '\'') && (text.charAt(text.length() - 1) == '\'')) {
            result = text.substring(1, text.length() - 1);
        }
        return result;
    }

    @NotNull
    public static String fixNull(@Nullable String str) {
        return (str == null) ? "" : str;
    }

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static boolean isChanged(CharSequence oldStr, CharSequence newStr) {
        if (isEmpty(oldStr) && isEmpty(newStr)) {
            return false;
        } else {    //  at least one of strings is not empty ...
            if (isEmpty(oldStr) || isEmpty(newStr)) {
                //  ... and if another string is empty it means they are different
                return true;
            } else {
                return !oldStr.equals(newStr);
            }
        }
    }

    public static String convertToQuotedString(String text) {
        if (isEmpty(text)) {
            return "";
        }

        int lastPos = text.length() - 1;
        if ((text.charAt(0) == '"') && (text.charAt(lastPos) == '"')) {
            return text;
        }

        return '"' + text + '"';
    }

    public static boolean isBlank(CharSequence text) {
        return !isNotBlank(text);
    }

    public static boolean isNotBlank(CharSequence text) {
        if (isEmpty(text)) {
            return false;
        }

        int length = text.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public static String getRandomAlphaNumeric(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ALPHA_NUMERIC.charAt(SECURE_RANDOM.nextInt(ALPHA_NUMERIC.length())));
        }
        return sb.toString();
    }

    public static boolean containsIgnoreCase(@NotNull String src, @NotNull String part) {
        return src.toLowerCase().contains(part.toLowerCase());
    }

    public static String uncapitalise(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static boolean hasAmbiguousCharacter(String text) {
        return AMBIGUOUS_CHARACTER_MATCHER.matchesAnyOf(text);
    }
}
