package com.example.springbootpractice.controller;

import com.example.springbootpractice.model.dto.PagingRequest;
import com.example.springbootpractice.model.dto.UpdateWebtoonCoinRequest;
import com.example.springbootpractice.model.dto.WebtoonEvaluationRequest;
import com.example.springbootpractice.model.dto.WebtoonResponse;
import com.example.springbootpractice.model.dto.WebtoonTop3Response;
import com.example.springbootpractice.model.dto.WebtoonViewHistoryResponse;
import com.example.springbootpractice.service.WebtoonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webtoon")
public class WebtoonController {

    private final WebtoonService webtoonService;

    /**
     * 웹툰 평가 API (일반)
     *
     * @param webtoonSeq
     * @param request
     */
    @PostMapping("/{webtoonSeq}/evaluation")
    public void requestEvaluation(
        @PathVariable Long webtoonSeq,
        @RequestBody @Valid WebtoonEvaluationRequest request
    ) {
        webtoonService.createWebtoonEvaluationInfo(webtoonSeq, request);
    }

    /**
     * 웹툰 좋아요/싫어요 TOP3 조회 API (일반)
     *
     * @return
     */
    @GetMapping("/top3")
    public WebtoonTop3Response requestTop3Webtoons() {
        return webtoonService.getTop3Webtoons();
    }


    /**
     * 웹툰 조회 API (일반)
     *
     * @param webtoonSeq
     * @return
     */
    @GetMapping("/{webtoonSeq}")
    public WebtoonResponse requestWebtoon(@PathVariable Long webtoonSeq) {
        return webtoonService.getWebtoon(webtoonSeq);
    }

    /**
     * 웹툰 조회내역 조회 API (일반)
     *
     * @param webtoonSeq
     * @param request
     * @return
     */
    @GetMapping("/{webtoonSeq}/view-history")
    public Page<WebtoonViewHistoryResponse> requestWebtoonViewHistory(
        @PathVariable Long webtoonSeq,
        @ModelAttribute PagingRequest request
    ) {
        return webtoonService.getWebtoonViewHistory(webtoonSeq, request.toPageable("seq"));
    }

    /**
     * 웹툰 금액 변경 API (관리자용)
     *
     * @param webtoonSeq
     * @param request
     */
    @PutMapping("/{webtoonSeq}/coin")
    public void requestUpdateWebtoonCoin(
        @PathVariable Long webtoonSeq,
        @RequestBody @Valid UpdateWebtoonCoinRequest request
    ) {
        webtoonService.updateWebtoonCoin(webtoonSeq, request);
    }


}
