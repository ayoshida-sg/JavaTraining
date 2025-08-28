package com.s_giken.training.batch;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {
	private static final String[] args = null;
	private final Logger logger = LoggerFactory.getLogger(BatchApplication.class);
	private final JdbcTemplate jdbcTemplate;
	private final BillingService billingService;

	/**
	 * SpringBoot エントリポイント
	 * 
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param jdbcTemplate SpringBootから注入される JdbcTemplate オブジェクト
	 */
	public BatchApplication(JdbcTemplate jdbcTemplate, BillingService billingService) {
		this.jdbcTemplate = jdbcTemplate;
		this.billingService = billingService;
	}

	/**
	 * コマンドラインプログラムのエントリ―ポイント
	 * 
	 * @param args コマンドライン引数
	 */
	@Override
	public void run(String... args) throws Exception {
		logger.info("-".repeat(40));

		if (args.length != 1) {
			throw new IllegalArgumentException("コマンドライン引数が不正です。対象年月を1つだけ指定してください。");
		}

		String input = args[0];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

		LocalDate targetDate;
		try {
			YearMonth billingYm = YearMonth.parse(input, formatter);
			targetDate = billingYm.atDay(1);
		} catch (DateTimeParseException e) {
			logger.info("コマンドライン引数の書式が不正です。対象年月はyyyyMM形式で指定してください。");
			return;
		}

		DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");

		logger.info("対象年月:{}", targetDate.format(jpFormatter));

		billingService.processBillingData(targetDate);
	}

}

// TODO: ここにバッチ処理のコードを記述する
// - データベースからデータを取得する
// - データを加工する
// - 加工したデータをデータベースに登録する

@Service
class BillingService {

	private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 請求データ処理
	 * 
	 * @param targetDate 対象年月の初日(LocalDate型)
	 */

	@Transactional
	public void processBillingData(LocalDate targetDate) {
		DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");
		String billingYmStrJp = targetDate.format(jpFormatter);
		logger.info("{}分の請求書を確認しています。", billingYmStrJp);

		java.sql.Date sqlDate = java.sql.Date.valueOf(targetDate);

		String sql = "SELECT COUNT(*) FROM T_BILLING_STATUS WHERE billing_ym = ? AND is_commit = TRUE";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, sqlDate);

		if (count != null && count > 0) {
			logger.info("既に請求データは確定済みなため処理を中断します。");
			return;
		}

		//対象年月に一致するレコードの削除
		String deleteDetailDataSql = "DELETE FROM T_BILLING_DETAIL_DATA WHERE billing_ym = ?";
		String deleteDataSql = "DELETE FROM T_BILLING_DATA WHERE billing_ym = ?";
		String deleteStatusSql = "DELETE FROM T_BILLING_STATUS WHERE billing_ym = ?";

		jdbcTemplate.update(deleteDetailDataSql, sqlDate);
		jdbcTemplate.update(deleteDataSql, sqlDate);
		jdbcTemplate.update(deleteStatusSql, sqlDate);

		logger.info("データベースから{}分の未確定請求情報を削除しました。", billingYmStrJp);

		//「請求データ状況」テーブルにレコードの追加
		String insertStatusSql = "INSERT INTO T_BILLING_STATUS (billing_ym, is_commit) VALUES (?, FALSE)";
		int insertedStatus = jdbcTemplate.update(insertStatusSql, sqlDate);

		logger.info("{}分の請求ステータス情報を追加しています。", billingYmStrJp);
		logger.info("{}件追加しました。", insertedStatus);

		//「請求データ」テーブルの追加
		String insertBillingDataSql = """
					INSERT INTO T_BILLING_DATA (
					  billing_ym, member_id, mail, name, address, start_date, end_date,
					  payment_method, amount, tax_ratio, total
					)
					SELECT
					  :billing_ym AS billing_ym,
					  m.member_id,
					  m.mail,
					  m.name,
					  m.address,
					  m.start_date,
					  m.end_date,
					  m.payment_method,
					  SUM(c.amount) AS amount,
					  0.10 AS tax_ratio,
					  FLOOR(SUM(c.amount) * 1.1) AS total
					FROM T_MEMBER m
					  CROSS JOIN T_CHARGE c
					WHERE
					  m.start_date <= :end_of_month
					  AND (m.end_date IS NULL OR m.end_date >= :start_of_month)
					  AND c.start_date <= :end_of_month
					  AND (c.end_date IS NULL OR c.end_date >= :start_of_month)
					GROUP BY
					  m.member_id,
					  m.mail,
					  m.name,
					  m.address,
					  m.start_date,
					  m.end_date,
					  m.payment_method
				""";

		String billingYmStr = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String startOfMonthStr = targetDate.withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);
		String endOfMonthStr = targetDate.withDayOfMonth(targetDate.lengthOfMonth()).format(DateTimeFormatter.ISO_DATE);

		String replacedBillingDataSql = insertBillingDataSql
				.replace(":billing_ym", "'" + billingYmStr + "'")
				.replace(":start_of_month", "'" + startOfMonthStr + "'")
				.replace(":end_of_month", "'" + endOfMonthStr + "'");
		int insertedBilling = jdbcTemplate.update(replacedBillingDataSql);

		logger.info("{}分の請求データ情報を追加しています。", billingYmStrJp);
		logger.info("{}件追加しました。", insertedBilling);

		//「請求明細データ」テーブルの追加
		String insertBillingDetailSql = """
						INSERT INTO T_BILLING_DETAIL_DATA (
						  billing_ym, member_id, charge_id , name, amount, start_date, end_date
						)
						SELECT
						  :billing_ym AS billing_ym,
						  m.member_id,
						  c.charge_id,
						  c.name,
						  c.amount,
						  c.start_date,
						  c.end_date
						FROM
						  T_MEMBER m
						  CROSS JOIN T_CHARGE c
						WHERE
						  m.start_date <= :end_of_month
						  AND (m.end_date IS NULL OR m.end_date >= :start_of_month)
						  AND c.start_date <= :end_of_month
						  AND (c.end_date IS NULL OR c.end_date >= :start_of_month)
				""";
		String replacedBillingDetailSql = insertBillingDetailSql
				.replace(":billing_ym", "'" + billingYmStr + "'")
				.replace(":start_of_month", "'" + startOfMonthStr + "'")
				.replace(":end_of_month", "'" + endOfMonthStr + "'");
		int insertedDetail = jdbcTemplate.update(replacedBillingDetailSql);

		logger.info("{}分の請求明細データ情報を追加しています。", billingYmStrJp);
		logger.info("{}件追加しました。", insertedDetail);

		logger.info("-".repeat(40));

	}

}
