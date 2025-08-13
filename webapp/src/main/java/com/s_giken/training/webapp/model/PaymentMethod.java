package com.s_giken.training.webapp.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支払い方法列挙型
 * 
 * 支払方法コードと表示名をセットにした列挙型
 */
public enum PaymentMethod {
    CREDIT((byte) 1, "クレジット"),
    BANK((byte) 2, "銀行振込"),
    UNKNOWN((byte) -1, "(不明)");

    private final Byte code;
    private final String displayName;

    // コードからPaymentMethod列挙型を取得するためのテーブルマップ
    // 毎回、この型の内部をすべてサーチするとO(N)となるが、Mapに変換しておくことで O(1) にできる。
    private static final Map<Byte, PaymentMethod> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(PaymentMethod::getCode, Function.identity()));

    /**
     * コンストラクタ
     * 
     * 列挙型の今合うトラクタは、アクセサを何も指定しないかprivateとする。
     * 
     * @param code
     * @param displayName
     */
    private PaymentMethod(Byte code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * 支払方法コードを返す
     * 
     * @return 被払い方法コード
     */
    public Byte getCode() {
        return code;
    }

    /**
     * 支表示名を返す
     * 
     * @return 表示名
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 被払い方法コードからPaymentMethod列挙型オブジェクトを返す
     * 
     * @param code 支払い方法コード
     * @return PaymentMethod列挙型オブジェクト
     */
    public static PaymentMethod fromCode(Byte code) {
        PaymentMethod pm = CODE_MAP.get(code);
        if (pm == null) {
            throw new IllegalArgumentException("不明な支払いコードです: " + code.toString());
        }
        return pm;
    }
}
