package com.s_giken.training.webapp.controller.editor;

import java.beans.PropertyEditorSupport;

import com.s_giken.training.webapp.model.PaymentMethod;

/**
 * PaymentMethod 列挙型に対する型変換を行うクラス
 * 
 * 主にHTTPリクエスト文字列からPaymentMetho列挙型への変換、またはその逆を行う。
 */
public class PaymentMethodEditorSupport extends PropertyEditorSupport {
    /**
     * 数値の文字列(支払方法コード)から PaymentMethod列挙型へ変換する
     * 
     * リクエスト → モデルオブジェクトのプロパティへ変換する際に利用される。
     * 
     * @param text 数値の文字列(支払方法コード)
     */
    @Override
    public void setAsText(String text) {
        try {
            setValue(PaymentMethod.fromCode(Byte.valueOf(text)));
        } catch (IllegalArgumentException e) {
            setValue(PaymentMethod.UNKNOWN);
        }
    }

    /**
     * PaymentMethod列挙型オブジェクトから数値の文字列(支払方法コード)へ変換する
     * 
     * @return 数値の文字列(支払方法コード)
     */
    @Override
    public String getAsText() {
        PaymentMethod pm = (PaymentMethod) getValue();
        return pm != null ? pm.getCode().toString() : null;
    }
}
