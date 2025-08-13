package com.s_giken.training.webapp.repository;

import java.util.List;
import java.util.Optional;
import com.s_giken.training.webapp.model.entity.Member;

public interface MemberRepository {
    /**
     * 加入者情報をすべて取得する。
     * 
     * @return Memberオブジェクトのリスト
     */
    public List<Member> findAll();

    /**
     * 指定した加入者IDの加入者情報を取得する。
     * 
     * @return Optional型の Memberオブジェクト
     */
    public Optional<Member> findById(Long id);

    /**
     * メールアドレスの一部にマッチするの加入者情報リストを取得する。
     * 
     * @return Optional型の Memberオブジェクト
     */
    public List<Member> findByMailLike(String mail);

    /**
     * 加入者情報をデータベースへ登録する。
     * 
     * @param member 追加するMemberオブジェクト。 memberIdプロパティの値は null としなくてはならない
     * @return 処理した件数
     */
    public int add(Member member);

    /**
     * データベースの加入者情報を更新する。
     * 
     * @param member 更新するMemberオブジェクト。 memberIdプロパティには値が設定されている必要がある。
     * @return 処理した件数
     */
    public int update(Member member);

    /**
     * データベースから指定した加入者IDの加入者情報を削除する。
     * 
     * @param id 加入者ID
     * @return 処理した件数
     */
    public int deleteById(Long id);
}
