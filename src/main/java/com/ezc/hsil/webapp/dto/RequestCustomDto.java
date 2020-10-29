package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezc.hsil.webapp.model.EzcMktGiveAway;
import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

import lombok.extern.slf4j.Slf4j;
 
@Repository
@Slf4j
public class RequestCustomDto {

	@PersistenceContext
	EntityManager em;
	//findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual
	    public List<EzcRequestHeader> findRequestList(ListSelector listSelector) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EzcRequestHeader> cq = cb.createQuery(EzcRequestHeader.class);
        Root<EzcRequestHeader> header = cq.from(EzcRequestHeader.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(listSelector.getType() != null && !"null".equals(listSelector.getType()) && !"".equals(listSelector.getType()))
        	predicates.add(cb.equal(header.get("erhReqType"), listSelector.getType()));
        if(listSelector.getFromDate() != null && listSelector.getToDate() != null)
        	predicates.add(cb.between(header.get("erhRequestedOn"), listSelector.getFromDate(), listSelector.getToDate()));
        if(listSelector.getStatus() != null && !"null".equals(listSelector.getStatus()) && !"".equals(listSelector.getStatus()))
        	predicates.add(cb.equal(header.get("erhStatus"), listSelector.getStatus()));
        if(listSelector.getDispStatus() != null)
        	predicates.add(cb.equal(header.get("erhDispatchFlag"), listSelector.getDispStatus()));
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
	        if(listSelector.getType() != null && !"null".equals(listSelector.getType()) && !"".equals(listSelector.getType()))
	        	predicates.add(cb.equal(header.get("erhReqType"), listSelector.getType()));
	        if(listSelector.getStatus() != null && !"null".equals(listSelector.getStatus()) && !"".equals(listSelector.getStatus()))
	        	predicates.add(cb.equal(header.get("erhStatus"), listSelector.getStatus()));
	        if(listSelector.getDispStatus() != null)
	        	predicates.add(cb.equal(header.get("erhDispatchFlag"), listSelector.getDispStatus()));
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
	        if(reportSelector.getSelDist() != null && !"null".equals(reportSelector.getSelDist()) && !"".equals(reportSelector.getSelDist()))
	        	predicates.add(cb.equal(header.get("erhDistrubutor"), reportSelector.getSelDist()));
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
	    
	    public List<Object[]> findRequestListJoinDealer(ListSelector reportSelector) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery cq = cb.createQuery();
	        Root<EzcRequestHeader> header = cq.from(EzcRequestHeader.class);
	        Join<EzcRequestHeader, EzcRequestDealers>  dealerJoin = header.join ("ezcRequestDealers");
	        List<Predicate> predicates = new ArrayList<Predicate>();
	        if(reportSelector.getType() != null) 
	        	predicates.add(cb.equal(header.get("erhReqType"), reportSelector.getType()));
	        if(reportSelector.getUser() != null) 
	        	predicates.add(cb.equal(header.get("erhRequestedBy"), reportSelector.getUser()));
	        if(reportSelector.getFromDate() != null && reportSelector.getToDate() != null)
	        	predicates.add(cb.between(header.get("erhRequestedOn"), reportSelector.getFromDate(), reportSelector.getToDate()));
	        if(reportSelector.getStatus() != null && !"null".equals(reportSelector.getStatus()) && !"".equals(reportSelector.getStatus()))
	        	predicates.add(cb.equal(header.get("erhStatus"), reportSelector.getStatus()));
	        if(reportSelector.getDispStatus() != null)
	        	predicates.add(cb.equal(header.get("erhDispatchFlag"), reportSelector.getDispStatus()));
	        cq.where(predicates.toArray(new Predicate[0]));
	        cq.multiselect(header.get("id"),header.get("erhReqType"),header.get("erhConductedOn"),header.get("erhRequestedBy"),header.get("erhReqName"),header.get("erhDistrubutor"),header.get("erhDistName"),header.get("erhCity"),header.get("erhNoOfAttendee"),header.get("erhStatus"),dealerJoin.get("erdMeetId"),dealerJoin.get("erdMeetDate"),dealerJoin.get("erdInstructions"),dealerJoin.get("erdNoOfAttendee"),dealerJoin.get("erdDealerName"),dealerJoin.get("erdMeetStatus"));
	        Query query = em.createQuery(cq);
	        return query.getResultList();
	    }
	
	    public List<EzcMktGiveAway> findMktGiveAwayList(ListSelector listSelector) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<EzcMktGiveAway> cq = cb.createQuery(EzcMktGiveAway.class);
	        Root<EzcMktGiveAway> header = cq.from(EzcMktGiveAway.class);
	        List<Predicate> predicates = new ArrayList<Predicate>();
	        
	        if(listSelector.getFromDate() != null && listSelector.getToDate() != null)
	        	predicates.add(cb.between(header.get("createdOn"), listSelector.getFromDate(), listSelector.getToDate()));
	        
	        if(listSelector.getSentTo() != null && !"null".equals(listSelector.getSentTo()) && !"".equals(listSelector.getSentTo()))
	        	predicates.add(cb.equal(header.get("sentTo"), listSelector.getSentTo()));
	        
	        if(listSelector.getStatus() != null && !"null".equals(listSelector.getStatus()) && !"".equals(listSelector.getStatus()))
	        	predicates.add(cb.equal(header.get("status"), listSelector.getStatus()));
	        
	        if(listSelector.getUser() != null && listSelector.getUser().size() > 0)
	        {
	        	Expression<String> exp = header.get("createdBy");
	        	predicates.add(exp.in(listSelector.getUser()));
	        }
	        cq.where(predicates.toArray(new Predicate[0]));
	        TypedQuery<EzcMktGiveAway> query = em.createQuery(cq);
	        return query.getResultList();
	    }
	    
}
