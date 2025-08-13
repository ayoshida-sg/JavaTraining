package com.s_giken.training.webapp.repository.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.s_giken.training.webapp.model.PaymentMethod;
import com.s_giken.training.webapp.model.entity.Member;

/**
 * データベースからのT_USERデータをMemberオブジェクトにマッピングする
 * 
 * @Autowired で注入できるように、DIコンテナのコンポーネントとする。
 */
@Component
public class MemberRowMapper implements RowMapper<Member> {
    /**
     * マッピング処理を行うメソッド
     * 
     * @param rs     データベースからのレコードセット
     * @param rowNum 処理行数
     * 
     * @return Memberオブジェクト
     */
    @Override
    public Member mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();

        member.setMemberId(rs.getLong("member_id"));
        member.setMail(rs.getString("mail"));
        member.setName(rs.getString("name"));
        member.setAddress((rs.getString("address")));

        // データベースから取得した DATE 型は、Javaでは、java.sql.Date型となるので、
        // それを、java.util.time.LocalDate形に変換する。
        Date startDate = rs.getDate("start_date");
        member.setStartDate((startDate != null) ? startDate.toLocalDate() : null);

        Date endDate = rs.getDate("end_date");
        member.setEndDate((endDate != null) ? endDate.toLocalDate() : null);

        // 被払い方法は、TINYINT型で格納されている。その値から PaymentMethod列挙型に変換する。
        Byte paymentMethod = rs.getByte("payment_method");
        if (paymentMethod != null) {
            try {
                member.setPaymentMethod(PaymentMethod.fromCode(paymentMethod));
            } catch (IllegalArgumentException e) {
                member.setPaymentMethod(PaymentMethod.UNKNOWN);
            }
        }

        member.setCreatedAt(rs.getTimestamp("created_at"));
        member.setModifiedAt(rs.getTimestamp("modified_at"));

        return member;
    }
}
