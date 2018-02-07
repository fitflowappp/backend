package com.magpie.yoga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.service.ChallengeService;

@Service
public class ChallengeServiceImpl implements ChallengeService {
	@Autowired
	ChallengeDao challengeDao;
}
