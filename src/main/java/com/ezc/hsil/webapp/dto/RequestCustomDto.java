package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezc.hsil.webapp.model.EzcRequestHeader;

import lombok.extern.slf4j.Slf4j;
 
@Repository
@Slf4j
public class RequestCustomDto {

	@Autowired
	EntityManager em;
	//findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual
	    public List<EzcRequestHeader> findRequestList(ListSelector listSelector) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EzcRequestHeader> cq = cb.createQuery(EzcRequestHeader.class);
        Root<EzcRequestHeader> header = cq.from(EzcRequestHeader.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(listSelector.getType() != null)
        	predicates.add(cb.equal(header.get("erhReqType"), listSelector.getType()));
        if(listSelector.getFromDate() != null && listSelector.getToDate() != null)
        	predicates.add(cb.between(header.get("erhRequestedOn"), listSelector.getFromDate(), listSelector.getToDate()));
        if(listSelector.getStatus() != null)
        	predicates.add(cb.equal(header.get("erhStatus"), listSelector.getStatus()));
        if(listSelector.getUser() != null && listSelector.getUser().size() > 0)
        {
        	Expression<String> exp = header.get("erhRequestedBy");
        	predicates.add(exp.in(listSelector.getUser()));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<EzcRequestHeader> query = em.createQuery(cq);
        return query.getResultList();
    }
	    
	    public long findRequestListCnt(ListSelector listSelector) {
	    	log.debug(":::::listSelector:::"+listSelector.getStatus());
	    	CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	        Root<EzcRequestHeader> header = cq.from(EzcRequestHeader.class);
	        List<Predicate> predicates = new ArrayList<Predicate>();
	        if(listSelector.getType() != null)
	        	predicates.add(cb.equal(header.get("erhReqType"), listSelector.getType()));
	        if(listSelector.getStatus() != null)
	        	predicates.add(cb.equal(header.get("erhStatus"), listSelector.getStatus()));
	        if(listSelector.getFromDate() != null && listSelector.getToDate() != null)
	        	predicates.add(cb.between(header.get("erhRequestedOn"), listSelector.getFromDate(), listSelector.getToDate()));
	        if(listSelector.getUser() != null && listSelector.getUser().size() > 0)
	        {
	        	Expression<String> exp = header.get("erhRequestedBy");
	        	predicates.add(exp.in(listSelector.getUser()));
	        }
	        cq.select(cb.count(header));
	        cq.where(predicates.toArray(new Predicate[0]));
	        return em.createQuery(cq).getSingleResult();
	        
	        
	        

	    }   

	    public List<EzcRequestHeader> findRequestList(ReportSelector reportSelector) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<EzcRequestHeader> cq = cb.createQuery(EzcRequestHeader.class);
	        Root<EzcRequestHeader> header = cq.from(EzcRequestHeader.class);
	        List<Predicate> predicates = new ArrayList<Predicate>();
	        if(reportSelector.getType() != null)
	        	predicates.add(cb.equal(header.get("erhReqType"), reportSelector.getType()));
	        if(reportSelector.getFromDate() != null && reportSelector.getToDate() != null)
	        	predicates.add(cb.between(header.get("erhRequestedOn"), reportSelector.getFromDate(), reportSelector.getToDate()));
	        if(reportSelector.getStatus() != null && !"null".equals(reportSelector.getStatus()) && !"".equals(reportSelector.getStatus()))
	        	predicates.add(cb.equal(header.get("erhStatus"), reportSelector.getStatus()));
	        if(reportSelector.getUserGrp() != null && reportSelector.getUserGrp().size() > 0)
	        {
	        	List<String> userList = new ArrayList<String>(); 
	        	reportSelector.getUserGrp().forEach(obj->userList.add((String) obj[0]));
	        	Expression<String> exp = header.get("erhRequestedBy");
	        	predicates.add(exp.in(userList));
	        }
	        cq.where(predicates.toArray(new Predicate[0]));
	        TypedQuery<EzcRequestHeader> query = em.createQuery(cq);
	        return query.getResultList();
	    }
	
}
