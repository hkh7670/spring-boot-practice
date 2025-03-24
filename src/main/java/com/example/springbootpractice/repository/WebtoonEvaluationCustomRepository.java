package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.dto.WebtoonTop3Info;
import com.example.springbootpractice.model.enums.WebtoonEvaluationType;
import java.util.List;

public interface WebtoonEvaluationCustomRepository {

    List<WebtoonTop3Info> findTop3Webtoons(WebtoonEvaluationType evaluationType);
}
