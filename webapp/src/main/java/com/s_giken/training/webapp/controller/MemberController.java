package com.s_giken.training.webapp.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.s_giken.training.webapp.controller.editor.PaymentMethodEditorSupport;
import com.s_giken.training.webapp.exception.NotFoundException;
import com.s_giken.training.webapp.model.PaymentMethod;
import com.s_giken.training.webapp.model.entity.Member;
import com.s_giken.training.webapp.model.entity.MemberSearchCondition;
import com.s_giken.training.webapp.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

/**
 * 加入者管理機能のコントローラークラス
 */
@Controller // コントローラークラスであることを示す
@RequestMapping("/member") // リクエストパスを指定
public class MemberController {
	private final MemberService memberService;

	/**
	 * 加入者管理機能のコントローラークラスのコンストラクタ
	 * 
	 * @param memberService 加入者管理機能のサービスクラス(SpringのDIコンテナから渡される)
	 */
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * コントローラで受けっとったリクエストの型変換方法をカスタマイズする。
	 * 
	 * 主に、独自で定義した型を利用している場合、デフォルトの方法では対応できないときに利用する。
	 * 
	 * @param binder リクエストパラメータ
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// PaymentMethod列挙型
		// リクエスト → PaymentMethod : Paymentmethod.fromCodeメソッドを利用して PaymentMethod列挙型へ変換
		// Paymentmethod → リクエスト : Paymentmethod.getCodeメソッドを利用して、数値の文字列へ変換
		binder.registerCustomEditor(PaymentMethod.class, new PaymentMethodEditorSupport());
	}

	/**
	 * 加入者検索条件画面を表示する
	 * 
	 * @param model Thymeleafに渡すデータ
	 * @return 加入者検索条件画面のテンプレート名
	 */
	@GetMapping("/search")
	public String showSearchCondition(Model model) {
		var memberSearchCondition = new MemberSearchCondition();
		model.addAttribute("memberSearchCondition", memberSearchCondition);
		return "member_search_condition";
	}

	/**
	 * 加入者検索結果画面を表示する
	 * 
	 * @param memberSearchCodition 加入者検索条件画面で入力された検索条件
	 * @param model                Thymeleafに渡すデータ
	 * @return 加入者検索結果画面のテンプレート名
	 */
	@PostMapping("/search")
	public String searchAndListing(
			@ModelAttribute("memberSearchCondition") MemberSearchCondition memberSearchCodition,
			Model model) {
		var result = memberService.findByConditions(memberSearchCodition);
		model.addAttribute("result", result);
		return "member_search_result";
	}

	/**
	 * 加入者編集画面を表示する
	 * 
	 * @param id    URLに指定された加入者ID
	 * @param model Thymeleafに渡すデータ
	 * @return 加入者編集画面のテンプレート名
	 */
	@GetMapping("/edit/{id}")
	public String editMember(
			@PathVariable Long id,
			Model model) {
		var member = memberService.findById(id);
		if (!member.isPresent()) {
			throw new NotFoundException(String.format("指定したmemberId(%d)の加入者情報が存在しません。", id));
		}
		model.addAttribute("isAddMode", false);
		model.addAttribute("member", member.get());
		return "member_edit";
	}

	/**
	 * 加入者追加画面を表示する
	 * 
	 * @param model Thymeleafに渡すデータ
	 * @return 加入者追加画面のテンプレート名
	 */
	@GetMapping("/add")
	public String formAddMember(Model model) {
		var member = new Member();
		model.addAttribute("isAddMode", true);
		model.addAttribute("member", member);
		return "member_edit";
	}

	/**
	 * 加入者情報を登録する
	 * 
	 * @param member             加入者編集画面で入力された加入者情報
	 * @param bindingResult      入力チェック結果
	 * @param redirectAttributes リダイレクト先の画面に渡すデータ
	 * @return リダイレクト先のURL
	 */
	@PostMapping("/add")
	@Transactional
	public String addMember(
			@Validated Member member,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "member_edit";
		}
		memberService.add(member);
		redirectAttributes.addFlashAttribute("message", "保存しました。");
		return "redirect:/member/edit/" + member.getMemberId();
	}

	/**
	 * 加入者情報を更新する
	 * 
	 * @param member             加入者編集画面で入力された加入者情報
	 * @param bindingResult      入力チェック結果
	 * @param redirectAttributes リダイレクト先の画面に渡すデータ
	 * @return リダイレクト先のURL
	 */
	@PostMapping("/update")
	@Transactional
	public String saveMember(
			@Validated Member member,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "member_edit";
		}
		memberService.update(member);
		redirectAttributes.addFlashAttribute("message", "保存しました。");
		return "redirect:/member/edit/" + member.getMemberId();
	}

	/**
	 * 加入者情報を削除する
	 * 
	 * @param id                 URLに指定された加入者ID
	 * @param redirectAttributes リダイレクト先の画面に渡すデータ
	 * @return リダイレクト先のURL
	 */
	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteMember(
			@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		var member = memberService.findById(id);
		if (!member.isPresent()) {
			throw new NotFoundException(String.format("指定したmemberId(%d)の加入者情報が存在しません。", id));
		}

		memberService.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "削除しました。");
		return "redirect:/member/search";
	}
}
