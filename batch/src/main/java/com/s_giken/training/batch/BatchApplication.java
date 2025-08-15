package com.s_giken.training.batch;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(BatchApplication.class);
	private final JdbcTemplate jdbcTemplate;

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
	public BatchApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * コマンドラインプログラムのエントリ―ポイント
	 * 
	 * @param args コマンドライン引数
	 */
	@Override
	public void run(String... args) throws RuntimeException {
		logger.info("-".repeat(40));
		
		
		if (args.length != 1) {
			throw new IllegalArgumentException("コマンドライン引数が不正です。対象年月を1つだけ指定してください。");
		}
			
		String input = args[0];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		
		try {
			YearMonth yearMonth = YearMonth.parse(input, formatter);
			LocalDate targetDate = yearMonth.atDay(1);
			System.out.println("対象年月:" + targetDate);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("コマンドライン引数の書式が不正です。対象年月はyyyyMM形式で指定してください。");
		}
	}
		
    	// TODO: ここにバッチ処理のコードを記述する
		// - データベースからデータを取得する
		// - データを加工する
		// - 加工したデータをデータベースに登録する


		logger.info("-".repeat(40));
	}
}
