package com.infy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.infy.dao.CandidateDAOImpl;
import com.infy.exception.InfyAcademyException;
import com.infy.model.Candidate;
import com.infy.model.CandidateReport;
import com.infy.validator.Validator;

public class CandidateServiceImpl implements CandidateService {
	CandidateDAOImpl d = new CandidateDAOImpl();
//	String s;
	// can have status as 'P' only if all 3 marks are 50 and above
	public String addCandidate(Candidate candidate) throws InfyAcademyException {
		Validator v = new Validator();

			v.validate(candidate);
			//if(candidate.getMark1()<50 || candidate.getMark1()<50 || candidate.getMark3()<50)
			//candidate.setResult('F');
			if((candidate.getMark1()<50 || candidate.getMark1()<50 || candidate.getMark2()<50) && candidate.getResult()=='P') {
				throw new InfyAcademyException("Service.INCORRECT_RESULT");
			}
	
		return d.addCandidate(candidate);
	}

	// calculating grade for candidate based on his marks and result
	public String calculateGrade(CandidateReport candidateReportTO) {
		String grade = null;
		if (candidateReportTO.getResult() == 'F') {
			grade = "NA";
		} else {
			float average = (candidateReportTO.getMark1() + candidateReportTO.getMark2() + candidateReportTO.getMark3())
					/ 3f;
			if(average >= 85.0)
				grade= "A";
			else if(average >= 75.0 && average < 85.0)
				grade= "B";
			else
				grade="C";
		}
		return grade;
	}

	// populating map<CandidateId, Grade> by calling
	// calculateGrade(candidateReportTO) and returning the same.
	public Map<Integer, String> getGradesForAllCandidates() throws InfyAcademyException {
	List<CandidateReport> list = d.getAllCandidates(); 
		Map<Integer, String> newMap = new TreeMap<>();
		for(CandidateReport c: list) {
			newMap.put(c.getCandidateId(),this.calculateGrade(c));
		}
		return newMap;
	}
}
